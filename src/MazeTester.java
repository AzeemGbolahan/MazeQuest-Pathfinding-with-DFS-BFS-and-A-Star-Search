/*
file name:      MazeTester.java
Authors:        Azeem Gbolahan
last modified:  05/04/2025
finds out the probaility of finding a solution on varying desnisty obstacles
*/
public class MazeTester {

    /**
     * Entry point: runs multiple randomized trials to estimate the probability
     * of finding a path in a maze with a given obstacle density.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
    
        // Exploration1 
        int successCount = 0;                 // counts how many runs successfully find a path
        int totalRuns = 50;                   // how many independent random mazes to test

        //Random rand = new Random();
        for (int i = 0; i < totalRuns; i++) { // repeat experiment totalRuns times

            // 1. Create the Maze
            //    rows=30, cols=30, density=0.5 (each cell has 50% chance to be an obstacle)
            Maze myMaze = new Maze(30, 30, 0.3);
            




            // 2. Create the search object
            //    Uncomment exactly one of the search strategies below as desired.
            //MazeAStarSearch searcher = new MazeAStarSearch(myMaze);          // A* searcher
            MazeBreadthFirstSearch searcher = new MazeBreadthFirstSearch(myMaze); // BFS searcher
            //MazeDepthFirstSearch searcher = new MazeDepthFirstSearch(myMaze); // DFS searcher
  
            // 3. Start the search (visualization disabled here with 'false', delay=0 ms)
            //    Returns null if no path found, otherwise a LinkedList<Cell> representing the path.
            LinkedList<Cell> foundTarget = searcher.search(myMaze.getStart(),myMaze.getTarget(), false, 500);
            // (true = show visualization, e.g., delay 50 milliseconds between steps)
            
            // If a non-null path is returned, we count this run as a success.
            if (foundTarget != null) {
                successCount++;               // increment success counter
            }
        }

        // Compute success percentage across all trials
        double successRate = ((double) successCount / totalRuns) * 100;

        // Report results to stdout
        System.out.println("Out of " + totalRuns + " random mazes, the target was found " + successCount + " times");
        System.out.println("Success rate = " + successRate + "%");
    }
}
