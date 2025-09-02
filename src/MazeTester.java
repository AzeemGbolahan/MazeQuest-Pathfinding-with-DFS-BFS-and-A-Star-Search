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
        if (args.length < 3) {
          System.err.println("Usage: java MyMain <rows> <cols> <density> [algo=bfs] [display=false] [delay=0]");
          System.exit(1);
        }
        try {
          int rows = Integer.parseInt(args[0]);
          int cols = Integer.parseInt(args[1]);
          double density = Double.parseDouble(args[2]);
          String algo = (args.length > 3 ? args[3] : "bfs").toLowerCase();
          boolean display = (args.length > 4) && Boolean.parseBoolean(args[4]);
          int delay = (args.length > 5) ? Integer.parseInt(args[5]) : 0;
    
          Maze maze = new Maze(rows, cols, density);
    
          AbstractMazeSearch searcher = 
            switch (algo) {
              case "dfs" -> new MazeDepthFirstSearch(maze);
              case "astar", "a*" -> new MazeAStarSearch(maze);
              default -> new MazeBreadthFirstSearch(maze); // bfs
            };
    
          LinkedList<Cell> path = searcher.search(maze.getStart(), maze.getTarget(), display, delay);
          System.out.println("Path " + (path == null ? "NOT found" : "length = " + path.size()));
        } catch (NumberFormatException e) {
          System.err.println("rows/cols must be ints; density and delay numeric. " + e.getMessage());
          System.exit(2);
        }
      }
}
