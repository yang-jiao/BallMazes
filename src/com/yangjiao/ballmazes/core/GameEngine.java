package com.yangjiao.ballmazes.core;

import com.yangjiao.ballmazes.PuzzleExemples;
import com.yangjiao.ballmazes.ui.Ball;
import com.yangjiao.ballmazes.ui.Puzzle;
import com.yangjiao.ballmazes.ui.MazeView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.TextView;


public class GameEngine {
	private SensorManager mSensorManager;
	private Vibrator mVibrator;
	private Sensor mAccSensor;

	private static float ACCEL_THRESHOLD = 2;
	private static final int VIBRATOR_GOAL = 100;
	private static final int VIBRATOR_WALL = 20;
	

	private Handler mHandler;

	private Puzzle mMap;
	private Ball mBall;
	private int mCurrentMap = 0;
	private int mMapToLoad = 0;
	private int mStepCount = 0;
	
	private Direction mCommandedRollDirection = Direction.NONE;

	private TextView mMazeNameLabel;
	private TextView mStepsView;
	private MazeView mMazeView;
	
	private final AlertDialog mMazeSolvedDialog;
	
	private final SensorEventListener mSensorAccelerometer = new SensorEventListener() {

		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}

		public void onSensorChanged(SensorEvent arg0) {
            float[] values  = new float[3];
			System.arraycopy(arg0.values, 0, values, 0, 3);
			
			float alpha = 0.8f;
			float[] gravity = new float[3];

			gravity[0] = alpha * gravity[0] + (1 - alpha) * values[0];
			gravity[1] = alpha * gravity[1] + (1 - alpha) * values[1];

			
			float mAccelX = (values[0] - gravity[0])*(-1);
			float mAccelY = (values[1] - gravity[1])*(-1);

			mCommandedRollDirection = Direction.NONE;
			if (Math.abs(mAccelX) > Math.abs(mAccelY)) {
				if (mAccelX < -ACCEL_THRESHOLD) mCommandedRollDirection = Direction.LEFT;
				
				if (mAccelX >  ACCEL_THRESHOLD) mCommandedRollDirection = Direction.RIGHT;
			}
			else {
				if (mAccelY < -ACCEL_THRESHOLD) mCommandedRollDirection = Direction.DOWN;
				
				if (mAccelY >  ACCEL_THRESHOLD) mCommandedRollDirection = Direction.UP;
			}
			

			if (mCommandedRollDirection != Direction.NONE) {
				rollBall(mCommandedRollDirection);
			}
			
		}
	};

	
	public GameEngine(Context context) {
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

		// Register the sensor listener
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(mSensorAccelerometer, mAccSensor, SensorManager.SENSOR_DELAY_GAME);
		mMap = new Puzzle(PuzzleExemples.designList.get(mCurrentMap));
				
		// Create ball
		mBall = new Ball(this, mMap,
				mMap.getInitialPositionX(),
				mMap.getInitialPositionY());

		// Congratulations dialog
		mMazeSolvedDialog = new AlertDialog.Builder(context)
			.setCancelable(true)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("Congratulations!")
			.setPositiveButton("Go to next maze!", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        sendEmptyMessage(Messages.MSG_MAP_NEXT);
                    }
                })
			.create();
		
		// Create message handler
		mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case Messages.MSG_INVALIDATE:
					mMazeView.invalidate();
					return;
				
				case Messages.MSG_REACHED_GOAL:
					vibrate(VIBRATOR_GOAL);
					if (mMap.getGoalCount() == 0) {
						mMazeSolvedDialog.setMessage(
								"You have solved maze "
								+ mMap.getName()
								+ " in " + mStepCount + " steps."
							);
						mMazeSolvedDialog.show();
					}
					return;
				
				case Messages.MSG_REACHED_WALL:
					vibrate(VIBRATOR_WALL);
					return;
				
				case Messages.MSG_RESTART:
					loadMap(mCurrentMap);
					return;
				
				case Messages.MSG_MAP_PREVIOUS:
				case Messages.MSG_MAP_NEXT:
					switch (msg.what) {
					case (Messages.MSG_MAP_PREVIOUS):
						if (mCurrentMap == 0) {
							// Wrap around
							mMapToLoad = PuzzleExemples.designList.size() - 1;
						}
						else {
							mMapToLoad = (mCurrentMap - 1) % PuzzleExemples.designList.size();
						}
						break;
					
					case (Messages.MSG_MAP_NEXT):
						mMapToLoad = (mCurrentMap + 1) % PuzzleExemples.designList.size();
						break;
					}
					
					loadMap(mMapToLoad);
					return;
				}
					
				super.handleMessage(msg);
			}
		};
	}
	
	public void loadMap(int mapID) {
		mCurrentMap = mapID;
		mBall.stop();
		mMap = new Puzzle(PuzzleExemples.designList.get(mCurrentMap));
		mBall.setMap(mMap);
		mBall.setX(mMap.getInitialPositionX());
		mBall.setY(mMap.getInitialPositionY());
		mBall.setXTarget(mMap.getInitialPositionX());
		mBall.setYTarget(mMap.getInitialPositionY());
		mMap.init();
		
		mStepCount = 0;
		
		mMazeNameLabel.setText(mMap.getName());
		mMazeNameLabel.invalidate();
		
		mStepsView.setText("" + mStepCount);
		mStepsView.invalidate();
		
		mMazeView.calculateUnit();
		mMazeView.invalidate();		
	}
	
	public void setMazeNameLabel(TextView mazeNameLabel) {
		mMazeNameLabel = mazeNameLabel;
	}
	
	
	public void setTiltMazesView(MazeView mazeView) {
		mMazeView = mazeView;
		mBall.setMazeView(mazeView);
	}
	
	public void setStepsLabel(TextView stepsView) {
		mStepsView = stepsView;
	}
	
	public void sendEmptyMessage(int msg) {
		mHandler.sendEmptyMessage(msg);
	}

	public void sendMessage(Message msg) {
		mHandler.sendMessage(msg);
	}

	public void registerListener() {
		mSensorManager.registerListener(mSensorAccelerometer, mAccSensor,SensorManager.SENSOR_DELAY_GAME);
	}

	public void unregisterListener() {
		mSensorManager.unregisterListener(mSensorAccelerometer, mAccSensor);
	}

	public void rollBall(Direction dir) {
		if (mBall.roll(dir)) mStepCount++;
		mStepsView.setText("" + mStepCount);
		mStepsView.invalidate();
	}
	
	public Ball getBall() {
		return mBall;
	}
	
	public Puzzle getMap() {
		return mMap;
	}
	
	
	public void vibrate(long milliseconds) {
		mVibrator.vibrate(milliseconds);
	}
	
	public void saveState(Bundle icicle) {
		mBall.stop();
		
		icicle.putInt("map.id", mCurrentMap);
		
		int[][] goals = mMap.getGoals();
		int sizeX = mMap.getSizeX();
		int sizeY = mMap.getSizeY();
		int[] goalsToSave = new int[sizeX * sizeY];
		for (int y = 0; y < sizeY; y++)
			for (int x = 0; x < sizeX; x++)
				goalsToSave[y + x * sizeX] = goals[y][x];
		icicle.putIntArray("map.goals", goalsToSave);
		
		icicle.putInt("stepcount", mStepCount);
		
		icicle.putInt("ball.x", Math.round(mBall.getX()));
		icicle.putInt("ball.y", Math.round(mBall.getY()));
	}
	
	public void restoreState(Bundle icicle) {
		if (icicle != null) {		
			int mapID = icicle.getInt("map.id", -1);
			if (mapID == -1) return;
			loadMap(mapID);
	
			int[] goals = icicle.getIntArray("map.goals");
			if (goals == null) return;
			
			int sizeX = mMap.getSizeX();
			int sizeY = mMap.getSizeY();
			for (int y = 0; y < sizeY; y++)
				for (int x = 0; x < sizeX; x++)
					mMap.setGoal(x, y, goals[y + x * sizeX]);
			
			mBall.setX(icicle.getInt("ball.x"));
			mBall.setY(icicle.getInt("ball.y"));
			
			// We have probably moved the ball, so invalidate the Maze View 
			mMazeView.invalidate();
			
			mStepCount = icicle.getInt("stepcount", 0);
		}
	
		mStepsView.setText("" + mStepCount);
		mStepsView.invalidate();
		
	}
}
