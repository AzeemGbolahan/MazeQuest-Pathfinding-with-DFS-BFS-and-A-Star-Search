// PathLengthAnalyzer - Analyzing the Relationship Between Path Lengths of DFS, BFS, and A*
/*
 * File Name:    PathLengthAnalyzer.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Runs BFS, DFS, and A* on the same randomly generated maze (single run)
 *               and prints the path lengths each algorithm finds. This is a one-shot
 *               comparison (not an average across trials).
 *
 * How to Run:   javac *.java
 *               java -ea PathLengthAnalyzer
 */

 public class PathLengthAnalyzer {
    public static void main(String[] args) {
        // Single run for each algorithm
        Maze myMaze = new Maze(20, 20, 0.0); // 20x20 maze with 0.0 density (no obstacles)
                                             // Note: with 0.0 density, a path should always exist

        // Run BFS search
        LinkedList<Cell> Bpath = new MazeBreadthFirstSearch(myMaze)
            .search(myMaze.getStart(), myMaze.getTarget(), false, 500); // no visualization, no delay
        int bfsPathLength = (Bpath == null) ? 0 : Bpath.size();       // If a path is found, use its size; else 0

        myMaze.reset();                                               // Clear prev links before next search

        // Run DFS search
        LinkedList<Cell> Dpath = new MazeDepthFirstSearch(myMaze)
            .search(myMaze.getStart(), myMaze.getTarget(), false, 0); // same maze instance after reset
        int dfsPathLength = (Dpath == null) ? 0 : Dpath.size();       // DFS path length or 0 if none

        myMaze.reset();                                               // Clear prev links before next search

        // Run A* search
        LinkedList<Cell> Apath = new MazeAStarSearch(myMaze)
            .search(myMaze.getStart(), myMaze.getTarget(), false, 0); // A* with Manhattan heuristic
        int aStarPathLength =(Apath == null) ? 0 : Apath.size();     // A* path length or 0 if none

        // Print the results to the console
        System.out.println("BFS Path Length: " + bfsPathLength);      // Expected: shortest path in unweighted grid
        System.out.println("DFS Path Length: " + dfsPathLength);      // May be longer than BFS
        System.out.println("A* Path Length: " + aStarPathLength);     // Typically optimal like BFS on unweighted grid
    }
}
