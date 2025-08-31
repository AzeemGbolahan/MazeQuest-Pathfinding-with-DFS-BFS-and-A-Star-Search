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

	public static double searchTests(){             // Runs the test suite and returns a score out of 6
		int restarts = 1;                           // Number of randomized trials to attempt

		int bfsPaths = 0;                           // Sum of BFS path lengths across trials
		int dfsPaths = 0;                           // Sum of DFS path lengths across trials
		int astarPaths = 0;                         // Sum of A*  path lengths across trials

		int bfsSearched = 0;                        // Sum of BFS visited-cell counts across trials
		int dfsSearched = 0;                        // Sum of DFS visited-cell counts across trials
		int astarSearched = 0;                      // Sum of A*  visited-cell counts across trials


        Random rand = new Random();
		int r = 0;                                   // Completed trial counter
		while ( r < restarts ){                      // Repeat until we finish 'restarts' successful runs
			Maze maze = new Maze(30, 30, 0.3 );      // Create a 10x10 maze with 30% obstacles
			MazeDepthFirstSearch dfssearcher = new MazeDepthFirstSearch(maze);         // DFS searcher
			LinkedList<Cell> dfssearched = dfssearcher.search(maze.getStart(), maze.getTarget(), false , 500  ); // Run DFS (no UI)
			if ( dfssearched != null ) {             // Only continue if DFS found a path (non-null result)
				dfsPaths += dfssearched.size();      // Accumulate DFS path length
				dfsSearched += maze.countVisitedCells(); // Count cells visited during DFS

				maze.reset();                        // Reset prev-links before BFS
				MazeBreadthFirstSearch bfssearcher = new MazeBreadthFirstSearch(maze); // BFS searcher
				LinkedList<Cell> bfssearched = bfssearcher.search(maze.getStart(), maze.getTarget(), false , 500  ); // Run BFS
				bfsPaths += bfssearched.size();      // Accumulate BFS path length
				bfsSearched += maze.countVisitedCells(); // Count cells visited during BFS

				maze.reset();                        // Reset prev-links before A*
				MazeAStarSearch astarsearcher = new MazeAStarSearch(maze);             // A* searcher
				LinkedList<Cell> astarsearched = astarsearcher.search( maze.getStart(), maze.getTarget(), true , 500 ); // Run A*
				astarPaths += astarsearched.size();  // Accumulate A* path length
				astarSearched += maze.countVisitedCells(); // Count cells visited during A*
				r ++ ;                               // Mark this trial as completed
			}
		}

		int score = 0 ;                               // Total number of passed checks

		// DFS: visited-cell count should be within a reasonable band
		if ( ( dfsSearched / restarts > 60 ) && ( dfsSearched / restarts < 90 ) ) { 
			System.out.println( "Test 1" );          // Report passing Test 1
			score ++;
		}
		// DFS: path length should be below a threshold
		if ( dfsPaths / restarts < 50 ) {
			System.out.println( "Test 2" );          // Report passing Test 2
			score ++;
		}

		System.out.println( bfsSearched / restarts ); // Print BFS visited-cell count (diagnostic)
		// BFS: visited-cell count expected band
		if ( ( bfsSearched / restarts > 80 ) && ( bfsSearched / restarts < 110 ) ) {
			System.out.println( "Test 3" );          // Report passing Test 3
			score ++;
		}

		System.out.println("path"+ bfsPaths / restarts ); // Print BFS path length (diagnostic)
		// BFS: path length threshold
		if ( bfsPaths / restarts < 25 ) {
			System.out.println( "Test 4" );          // Report passing Test 4
			score ++;
		}

		// A*: should visit fewer cells than BFS/DFS with a good heuristic
		if ( astarSearched / restarts < 80 ) {
			System.out.println( "Test 5" );          // Report passing Test 5
			score ++;
		}
		System.out.println( astarPaths / restarts ); // Print A* path length (diagnostic)
		// A*: path length threshold (ideally optimal)
		if ( astarPaths / restarts < 25 ) { 
			System.out.println( "Test 6" );          // Report passing Test 6
			score ++;
		}

		return score ;                                 // Return total score out of 6
	}

	public static void main( String[] args ) throws InterruptedException { // Entry point
		System.out.println( searchTests() + "/6" );     // Run tests and print "score/6"
	}

}
