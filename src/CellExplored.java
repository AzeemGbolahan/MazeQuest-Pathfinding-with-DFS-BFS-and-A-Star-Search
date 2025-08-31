// CellExplore - Analyzing the Relationship Between the Number of Cells Explored by DFS, BFS, and A*
/*
 * File Name:    CellExplored.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Runs DFS, BFS, and A* on the same randomly generated maze and reports the
 *               number of cells each algorithm explored (via Maze.countVisitedCells()).
 *               This is a single-trial snapshot, not a statistical average over many runs.
 *
 * How to Run:   javac *.java
 *               java -ea CellExplored
 */

public class CellExplored {
    public static void main(String[] args) {
        Maze myMaze = new Maze(30, 30, 0.2); // Create a 20x20 maze with 30% obstacle density
        // Random rand = new Random();
        // Note: Using a relatively higher obstacle density to illustrate cases where a path might not exist
        //       yet exploration still occurs (visited cells are counted regardless of success).
        
        // Cell start = myMaze.get(rand.nextInt(myMaze.getRows()) , rand.nextInt(myMaze.getCols()));          // Select start cell at (1,1)
		// start.setType(CellType.FREE);            // Ensure start is not an obstacle
		// Cell target = myMaze.get( rand.nextInt(myMaze.getRows()) , rand.nextInt(myMaze.getCols()) );         // Select target cell at (8,8)
		// target.setType(CellType.FREE);           // Ensure target is not an obstacle
        // ---- DFS ----
        LinkedList<Cell> Dpath = new MazeDepthFirstSearch(myMaze)          // Construct a DFS searcher bound to this maze
            .search(myMaze.getStart(), myMaze.getTarget(), false, 500);      // Run search with no visualization, no delay
        int dfsAvgCellsExplored = myMaze.countVisitedCells();               // Count how many cells DFS visited
        myMaze.reset();                                                     // Reset prev links before the next run

        // ---- BFS ----
        LinkedList<Cell> Bpath = new MazeBreadthFirstSearch(myMaze)        // Construct a BFS searcher for same maze
            .search(myMaze.getStart(), myMaze.getTarget(), false, 500);      // Run BFS on the (reset) maze
        int bfsAvgCellsExplored = myMaze.countVisitedCells();               // Count BFS visited cells
        myMaze.reset();                                                     // Reset again before A*

        // ---- A* ----
        LinkedList<Cell> Apath = new MazeAStarSearch(myMaze)               // Construct an A* searcher (Manhattan heuristic)
            .search(myMaze.getStart(), myMaze.getTarget(), false, 500);      // Run A* on the (reset) maze
        int aStarAvgCellsExplored = myMaze.countVisitedCells();             // Count A* visited cells

        // ---- Report ----
        System.out.println("DFS Average Cells Explored: " + dfsAvgCellsExplored);     // Print DFS explored count
        System.out.println("A* Average Cells Explored: " + aStarAvgCellsExplored);    // Print A* explored count
        System.out.println("BFS Average Cells Explored: " + bfsAvgCellsExplored);     // Print BFS explored count
    }
}
