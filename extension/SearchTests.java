/*
 * File Name:    SearchTests.java
 * Author:       Azeem Gbolahan (original tester by Ike Lage)
 * Last Modified: 05/04/2025
 * Description:  Test harness comparing DFS, BFS, and A* maze searchers on the same Maze.
 *               Measures (1) number of cells searched/visited and (2) path length found.
 *               Prints which tests pass based on simple thresholds and returns a score /6.
 *
 * How to Run:   Compile with the Maze project sources and run:
 *                   javac *.java
 *                   java SearchTests
 */

// Ike Lage
// CS231
// Project 7 Tester for Maze Searches

import java.util.Random;


public abstract class SearchTests {                 // Abstract container for static test methods

	public static void stepsAnalysis(){
		int restarts = 1;                           // Number of randomized trials to attempts


        Random rand = new Random();
		int r = 0;                                   // Completed trial counter                      // Repeat until we finish 'restarts' successful runs
		Maze maze = new Maze(30, 30, 0.2 );      // Create a 10x10 maze with 30% obstacles

		MazeDepthFirstSearch dfssearcher = new MazeDepthFirstSearch(maze);         // DFS searcher
		LinkedList<Cell> dfssearched = dfssearcher.search(maze.getStart(), maze.getTarget(), true , 500  ); // Run DFS (no UI)
		int dfsSteps = dfssearcher.getExecutionCost();
		maze.reset();                        // Reset prev-links before BFS


		MazeBreadthFirstSearch bfssearcher = new MazeBreadthFirstSearch(maze); // BFS searcher
		LinkedList<Cell> bfssearched = bfssearcher.search(maze.getStart(), maze.getTarget(), true , 500  ); // Run BFS
		int bfsSteps = bfssearcher.getExecutionCost();
		maze.reset();                        // Reset prev-links before A*

		MazeAStarSearch astarsearcher = new MazeAStarSearch(maze);             // A* searcher
		LinkedList<Cell> astarsearched = astarsearcher.search( maze.getStart(), maze.getTarget(), true , 500 ); // Run A*
		int astarSteps = astarsearcher.getExecutionCost();
		
		r ++ ;                               // Mark this trial as completed
		System.out.println("Total number of steps taken for the bfs to complete this search: " + bfsSteps);
		System.out.println("Total number of steps taken for the dfs to complete this search: " + dfsSteps);
		System.out.println("Total number of steps taken for the astar to complete this search: " + astarSteps);
	}
	public static void main( String[] args ) throws InterruptedException { // Entry point
		stepsAnalysis();
	}

}
