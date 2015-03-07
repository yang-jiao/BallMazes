package com.yangjiao.ballmazes;

import java.util.LinkedList;
import java.util.List;

import com.yangjiao.ballmazes.ui.MazeDesign;

import static com.yangjiao.ballmazes.ui.Wall.*;

public final class PuzzleExemples {

	public static final List<MazeDesign> designList = new LinkedList<MazeDesign>();
	
	static {
		/**
		 *  _ _ _ _ _
           |  _  |   |
           |        _|
           | |  _    |
           |  _     _|
           |_ _ _|_ _|
		 */
		designList.add(new MazeDesign(
		    "Puzzle-1",
		    5, 5,
		    new int[][] {
		            {LEFT|TOP, TOP|BOTTOM, TOP|RIGHT, TOP, TOP|RIGHT},
		            {LEFT, 0, 0, 0, RIGHT|BOTTOM},
		            {LEFT|RIGHT, 0, BOTTOM, 0, RIGHT},
		            {LEFT, BOTTOM, 0, 0, RIGHT|BOTTOM},
		            {LEFT|BOTTOM, BOTTOM, BOTTOM|RIGHT, BOTTOM, RIGHT|BOTTOM}
		    },
		    new int[][] {
		            {0, 0, 0, 0, 0},
		            {0, 0, 0, 0, 0},
		            {0, 0, 0, 0, 0},
		            {0, 0, 0, 0, 0},
		            {0, 0, 0, 0, 1}
		    },
		    0, 0
		));
		
		/**
		 *   _ _ _ _ _ _
            | |  _    | |
            |           |
            | | |_|_   _|
            |_    | |   |
            |          _|
            |_ _|_|_ _ _|
           
		 */

		designList.add(new MazeDesign(
			"Puzzle-2",
	        6, 6,
	        new int[][] {
	                {LEFT|TOP|RIGHT, TOP, TOP|BOTTOM, TOP, TOP|RIGHT, TOP|RIGHT},
	                {LEFT, 0, 0, 0, BOTTOM, RIGHT},
	                {LEFT|RIGHT, RIGHT, RIGHT|BOTTOM, BOTTOM, 0, RIGHT|BOTTOM},
	                {LEFT|BOTTOM, 0, RIGHT, RIGHT, 0, RIGHT},
	                {LEFT, 0, 0, 0, 0, RIGHT|BOTTOM},
	                {LEFT|BOTTOM, BOTTOM|RIGHT, BOTTOM|RIGHT, BOTTOM, BOTTOM, RIGHT|BOTTOM}
	        },
	        new int[][] {
	                {0, 0, 0, 0, 0, 0},
	                {0, 0, 0, 0, 0, 0},
	                {0, 0, 0, 0, 0, 0},
	                {0, 0, 0, 1, 0, 0},
	                {0, 0, 0, 0, 0, 0},
	                {0, 0, 0, 0, 0, 0}
	        },
	        2, 2
		));
		
		/**
		 *   
		     _ _ _ _ _ _ _
            |      _  |   | 
            |_  |    _   _|        
            |_    |       |
            |_       _|  _|
            |  _  |      _|
            |_  |      _  |
            |_ _ _|_ _|_ _|
            
		 */
        
        designList.add(new MazeDesign(
    		"Puzzle-3",
            7, 7,
            new int[][] {
                    {LEFT|TOP, TOP, TOP, TOP|BOTTOM, TOP|RIGHT, TOP, TOP|RIGHT},
                    {LEFT|BOTTOM, RIGHT, 0, 0, BOTTOM, 0, RIGHT|BOTTOM},
                    {LEFT|BOTTOM, 0, RIGHT, 0, 0, 0, RIGHT},
                    {LEFT, 0, 0, 0, RIGHT|BOTTOM, 0, RIGHT|BOTTOM},
                    {LEFT, BOTTOM, RIGHT, 0, 0, 0, BOTTOM|RIGHT},
                    {LEFT|BOTTOM, RIGHT, 0, 0, 0, BOTTOM, RIGHT},
                    {LEFT|BOTTOM, BOTTOM, BOTTOM|RIGHT, BOTTOM, BOTTOM|RIGHT, BOTTOM, BOTTOM|RIGHT}
            },
            new int[][] {
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 1},
                    {0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0}
            },
            0, 2
        ));
	}
	
	private PuzzleExemples() {
	    throw new AssertionError();
	}
}
