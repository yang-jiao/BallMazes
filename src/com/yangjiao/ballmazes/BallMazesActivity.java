package com.yangjiao.ballmazes;

import com.yangjiao.ballmazes.core.GameEngine;
import com.yangjiao.ballmazes.ui.MazeView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;


public class BallMazesActivity extends Activity {
	
	private MazeView mMazeView;
	
	
	private TextView mMazeNameLabel;
	private TextView mStepsLabel;

	private GameEngine mGameEngine;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.main);
		
		mGameEngine = new GameEngine(BallMazesActivity.this);
		mMazeView = (MazeView) findViewById(R.id.maze_view);
		mGameEngine.setTiltMazesView(mMazeView);
		mMazeView.setGameEngine(mGameEngine);
		mMazeView.calculateUnit();
		
		mMazeNameLabel = (TextView) findViewById(R.id.maze_name);
		mGameEngine.setMazeNameLabel(mMazeNameLabel);
		mMazeNameLabel.setText(mGameEngine.getMap().getName());
		mMazeNameLabel.invalidate();

		mStepsLabel = (TextView) findViewById(R.id.steps);
		mGameEngine.setStepsLabel(mStepsLabel);
		
		mGameEngine.restoreState(savedInstanceState);
		
	}
    
    @Override
	protected void onPause() {
		super.onPause();
		mGameEngine.unregisterListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mGameEngine.registerListener();
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		mGameEngine.saveState(bundle);
		mGameEngine.unregisterListener();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);		
		mGameEngine.restoreState(savedInstanceState);
					
	}

}