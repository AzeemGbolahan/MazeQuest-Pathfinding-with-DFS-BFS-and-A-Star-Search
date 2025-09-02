/*
 * File Name:    Maze.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Defines a grid-based Maze composed of Cell objects. Each Cell knows its position,
 *               type (FREE/OBSTACLE), and predecessor discovered during search. The Maze supports:
 *               - Construction by random density of obstacles or by a provided 2D array of Cells
 *               - Iteration over all Cells (row-major order)
 *               - Neighbor retrieval excluding obstacles
 *               - Resetting per-Cell transient state (prev links)
 *               - Basic drawing and text visualization
 *
 * How to Run:   Compile with `javac Maze.java Cell.java CellType.java LinkedList.java` and invoke from a driver.
 */

 import java.awt.Graphics;          // Graphics for drawing the maze and its cells
import java.util.HashMap;
import java.util.Iterator;         // Iterator to enable for-each iteration over Cells
import java.util.Map;
import java.util.Random;           // Random for stochastic maze generation
 
 public class Maze implements Iterable<Cell> { // Maze implements Iterable<Cell> to support for-each over its cells
 
     /**
      * An iterator which iterates through all the Cells in the Maze row by row and
      * column by column.
      */
     public Iterator<Cell> iterator() {        // Expose an iterator in row-major order
         return new Iterator<Cell>() {         // Anonymous Iterator<Cell> instance
             int r, c;                         // Current iteration position: row r, column c (default 0,0)
 
             public boolean hasNext() {        // Determine if another Cell exists
                 return r < getRows();         // True while row index is within bounds
             }
 
             public Cell next() {              // Return the next Cell and advance the cursor
                 Cell next = get(r, c);        // Read the current cell at (r,c)
                 c++;                          // Advance column
                 if (c == getCols()) {         // If we just moved past the last column
                     r++;                      // advance to next row
                     c = 0;                    // and reset column to 0
                 }
                 return next;                  // Return the previously captured cell
             }
         };
     }
 
     /**
      * The number of rows and columns in this Maze.
      */
     private int rows, cols;                   // Maze dimensions
 
     /**
      * The density of this Maze. Each Cell independently has probability
      * {@code density} of being an OBSTACLE.
      */
     private double density;                   // Probability in [0,1] used during random generation
 
     /**
      * The 2-D array of Cells making up this Maze.
      */
     private Cell[][] landscape;               // Backing grid of Cell references
 
     private Cell start, target;               // Special Cells: start and target of searches
 
     /**
      * Constructs a Maze with the given number of rows and columns. Each Cell
      * independently has probability {@code density} of being an OBSTACLE.
      * 
      * @param rows    the number of rows.
      * @param columns the number of columns.
      * @param density the probability of any individual Cell being an OBSTACLE.
      */
     public Maze(int rows, int columns, double density) { // Randomized constructor
         this.rows = rows;                                 // Store number of rows
         this.cols = columns;                              // Store number of columns
         this.density = density;                           // Store obstacle density
         landscape = new Cell[rows][columns];              // Allocate 2D array
         reinitialize();                                   // Populate with random FREE/OBSTACLE cells
        

         Random rand = new Random();
         start = landscape[rand.nextInt(rows)][rand.nextInt(cols)]; // Pick a random start cell
         while (start.getType() == CellType.OBSTACLE) {    // Ensure start is FREE
             start = landscape[rand.nextInt(rows)][rand.nextInt(cols)];
         }  
         target = landscape[rand.nextInt(rows)][rand.nextInt(cols)]; // Pick a random target
         while (target.getType() == CellType.OBSTACLE || start == target || this.manhattan(start, target) < rows/2) { // Ensure target is FREE and distinct
             target = landscape[rand.nextInt(rows)][rand.nextInt(cols)];
         }

        

     }
 
     /**
      * Constructs a Maze with the given 2-D array of Cells. The starting and target
      * Cells are set to the given start and target Cells,
      * respectively.
      * 
      * @param maze   the 2-D array of Cells making up this Maze.
      * @param start  the starting Cell of this Maze.
      * @param target the target Cell of this Maze.
      */
     public Maze(Cell[][] maze, Cell start, Cell target){ // Explicit grid constructor
         this.landscape = maze;                            // Use provided grid
         this.start = start;
         this.target = target;
     }

     /**
      * Initializes every Cell in the Maze.
      */
     public void reinitialize() {                          // Rebuild all cells with random types
         Random rand = new Random();                       // RNG to sample density
         for (int r = 0; r < rows; r++) {                  // For each row
             for (int c = 0; c < cols; c++) {              // For each column
                 landscape[r][c] = new Cell(               // Create a new Cell at (r,c)
                     r,                                    // row coordinate
                     c,                                    // column coordinate
                     rand.nextDouble() < density           // coin flip: OBSTACLE with prob=density
                         ? CellType.OBSTACLE               // mark as OBSTACLE
                         : CellType.FREE                   // otherwise mark as FREE
                 );
             }
         }
     }
 
     /**
      * Calls {@code reset} on every Cell in this Maze.
      */
     public void reset() {                                 // Clear transient state for all cells
         for (Cell cell : this)                            // Iterate over cells via Iterable<Cell>
             cell.reset();                                 // Reset each one (clears prev)
     }
 
     /**
      * Returns the number of rows in the Maze.
      * 
      * @return the number of rows in the Maze.
      */
     public int getRows() {                                // Accessor for rows
         return rows;                                      // Return stored row count
     }
 
     public Cell getStart() {                              // Accessor for start cell
         return this.start;                               // NOTE: returns stored start
     }
 
     public Cell getTarget() {                             // Accessor for target cell
         return this.target;                               // Return stored target
     }
 
     /**
      * Returns the number of columns in the Maze.
      * 
      * @return the number of columns in the Maze.
      */
     public int getCols() {                                // Accessor for columns
         return cols;                                      // Return stored column count
     }


     // helper function to determine the distance between 2 cells
    public int manhattan(Cell a, Cell b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }
 
     /**
      * Returns the Cell at the specified row and column in the Maze.
      * 
      * @param row the row
      * @param col the column
      * @return the Cell at the specified row and column in the Maze.
      */
     public Cell get(int row, int col) {                   // Random access into the grid
         return landscape[row][col];                       // Return reference at (row,col)
     }
 
     /**
      * Returns a LinkedList of the non-OBSTACLE Cells neighboring the specified
      * Cell.
      * 
      * @param c the Cell to explore around.
      * @return a LinkedList of the non-OBSTACLE Cells neighboring the specified
      *         Cell.
      */
     public LinkedList<Cell> getNeighbors(Cell c) {        // Compute 4-neighborhood (N,S,E,W) excluding obstacles
         LinkedList<Cell> cells = new LinkedList<Cell>();  // Accumulator list
         int[][] steps = new int[][] { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } }; // Relative displacements: up, down, right, left
         for (int[] step : steps) {                        // For each direction
             int nextRow = c.getRow() + step[0];           // Candidate neighbor row
             int nextCol = c.getCol() + step[1];           // Candidate neighbor col
             if (nextRow >= 0 && nextRow < getRows() &&    // In-bounds row?
                 nextCol >= 0 && nextCol < getCols() &&    // In-bounds col?
                 get(nextRow, nextCol).getType() != CellType.OBSTACLE) // Non-obstacle?
                 cells.offer(get(nextRow, nextCol));       // Enqueue neighbor into result list
         }
         return cells;                                     // Return all valid, free neighbors
     }
 
     public int countVisitedCells() {                      // Utility: count cells with non-null prev (i.e., visited)
         int count = 0;                                    // Initialize counter
         for (int i = 0; i < rows; i++) {                  // Loop through all rows
             for (int j = 0; j < cols; j++) {              // Loop through all columns
                 if (get(i, j).getPrev() != null) {        // If the cell has been visited (prev set)
                     count++;                               // Increment count
                 }
             }
         }
         return count;                                      // Return the total number of visited cells
     }

     
     
     public String toString() {                             // ASCII visualization of the maze
         StringBuilder output = new StringBuilder();        // Build output efficiently
         output.append("-".repeat(cols + 3) + "\n");        // Top border line (cols + padding)
         for (Cell[] cells : landscape) {                   // For each row of cells
             output.append("| ");                           // Left border + space
             for (Cell cell : cells) {                      // For each cell in the row
                 output.append(cell.getType() == CellType.OBSTACLE ? 'X' : ' '); // 'X' for obstacle, ' ' for free
             }
             output.append("|\n");                          // Right border + newline
         }
         return output.append("-".repeat(cols + 3)).toString(); // Bottom border and finalize string
     }
 
     /**
      * Calls {@code drawType} on every Cell in this Maze.
      * @param g     graphics context to draw on
      * @param scale pixel width/height of each Maze cell
      */
     public void draw(Graphics g, int scale) {              // Render each cell with its type styling
         for (Cell cell : this)                             // For-each over all cells
             cell.drawType(g, scale);                       // Delegate drawing of type styling to Cell
     }
 
     public static void main(String[] args) {               // Simple manual test / demo
         Maze ls = new Maze(7, 7, .2);                      // Create a 7x7 maze with 20% obstacles
         System.out.println(ls);                            // Print ASCII representation
     }
 }
 