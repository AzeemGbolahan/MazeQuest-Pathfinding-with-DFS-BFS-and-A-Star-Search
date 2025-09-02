/*
 * File Name:    Cell.java
 * Author:       Azeem Gbolahan
 * Last Modified: 03/12/2025
 * Description:  Represents a single cell in a grid/maze. Each Cell knows its (row, col) position,
 *               its type (FREE or OBSTACLE), and a pointer to the previous Cell that discovered it
 *               during a traversal (e.g., BFS/DFS). Includes drawing utilities for rendering the cell,
 *               paths to predecessors, and predecessor trees.
 *
 * How to Run:   Compile with `javac Cell.java` and use from a Maze/GUI driver that provides a Graphics context.
 */

 import java.awt.Color;         // Color class for drawing with specific colors
 import java.awt.Graphics;      // Graphics class for drawing shapes/lines on a component
 
 public class Cell {            // Public class representing a single grid cell
     /**
      * Specifies the row and column of this Cell
      */
     private int row, col;      // Grid coordinates: row (y) and col (x)
 
     /**
      * Specifies the Cell which, when explored, revealed this Cell for the first
      * time
      */
     private Cell prev;         // Pointer to the predecessor Cell in a search (null if none)
 
     /**
      * Specifies the CellType of this Cell (either FREE or OBSTACLE)
      */
     private CellType type;     // The semantic type of the cell (FREE or OBSTACLE)
 
     //private boolean visited = false; // Optional visited flag (unused in this implementation)
 
     /**
      * Constructs a Cell from the given parameters.
      * 
      * @param r    the row of the Cell
      * @param c    the column of the Cell
      * @param type the CellType of the Cell (either FREE or OBSTACLE)
      */
     public Cell(int r, int c, CellType type) { // Constructor initializes coordinates and type
         row = r;                               // Assign provided row
         col = c;                               // Assign provided column
         this.type = type;                      // Assign provided type
     }
 
     /**
      * Sets the previous Cell of this to the given {@code Cell prev}.
      * 
      * This means that when {@code prev} was explored, this Cell was found for the
      * first time.
      * 
      * @param prev the previous Cell of this one.
      */
     public void setPrev(Cell prev) {           // Setter for predecessor link
         this.prev = prev;                      // Update internal predecessor pointer
     }
 
     /**
      * Returns the previous Cell of this one.
      * 
      * @return the previous Cell of this one.
      */
     public Cell getPrev() {                    // Getter for predecessor link
         return prev;                           // Return predecessor reference (may be null)
     }
 
     /**
      * Resets this Cell back to its initial state (which just sets prev to null)
      */
     public void reset() {                      // Clear transient traversal state
         setPrev(null);                         // Reset predecessor to null
     }
 
     /**
      * Returns the CellType of this Cell (either FREE or OBSTACLE).
      * 
      * @return the CellType of this Cell (either FREE or OBSTACLE).
      */
     public CellType getType() {                // Getter for cell type
         return type;                           // Return current type
     }
 
     public void setType( CellType type ) {     // Setter for cell type
         this.type = type;                      // Update current type
     }
 
     /**
      * Returns the row of this Cell.
      * 
      * @return the row of this Cell.
      */
     public int getRow() {                      // Getter for row coordinate
         return row;                            // Return row
     }
 
     /**
      * Returns the column of this Cell.
      * 
      * @return the column of this Cell.
      */
     public int getCol() {                      // Getter for column coordinate
         return col;                            // Return col
     }
 
     public boolean equals(Object o) {          // Structural equality check for Cells
         if (!(o instanceof Cell))              // If compared object is not a Cell
             return false;                      // They are not equal
         Cell c = (Cell) o;                     // Safe cast to Cell
         return row == c.row && col == c.col && type == c.type; // Equal if row, col, and type match
     }
 
     public String toString() {                 // String representation for debugging/logging
         return "(" + row + ", " + col + ", " + type + ")"; // Format: (row, col, TYPE)
     }
 
     /**
      * Draws this Cell to the given Graphics object.
      * 
      * If this Cell is FREE, then it will be drawn with yellow if it has been
      * visited, otherwise gray.
      * 
      * If this Cell is an OBSTACLE, it will be drawn black.
      * 
      * @param g     the Graphics object on which to draw.
      * @param scale the scale at which to draw this Cell.
      */
     public void drawType(Graphics g, int scale) {                 // Draw border + fill according to type/visited
         g.setColor(Color.BLACK);                                  // Use black for the outer border
         g.drawRect(getCol() * scale, getRow() * scale,            // Draw rectangle outline at (x, y)
                    scale, scale);                                 // with width=scale and height=scale
         switch (getType()) {                                      // Decide fill color based on cell type
             case FREE:                                            // FREE cells are navigable
                 draw(g, scale, getPrev() != null ? Color.YELLOW   // If discovered (prev != null), fill yellow
                                                 : Color.GRAY);    // Otherwise, fill gray
                 break;                                            // End FREE case
             case OBSTACLE:                                        // OBSTACLE cells are walls/blocked
                 draw(g, scale, Color.BLACK);                      // Fill solid black
                 break;                                            // End OBSTACLE case
         }
     }
 
     /**
      * Recursively draws lines from each visited Cell to the Cell that they revealed
      * by exploration.
      * 
      * @param maze  the Maze in which this Cell resides.
      * @param g     the Graphics object on which to draw.
      * @param scale the scale at which to draw.
      * @param c     the Color to draw with.
      */
     public void drawAllPrevs(Maze maze, Graphics g, int scale, Color c) { // Draw full predecessor tree outward
         g.setColor(c);                                                    // Set stroke color for lines
         for (Cell neighbor : maze.getNeighbors(this)) {                   // Iterate over all neighbors of this cell
             if (neighbor.getPrev() == this) {                             // If this cell is the neighbor's predecessor
                 g.drawLine(getCol() * scale + scale / 2,                  // Draw line from this cell center (x1, y1)
                            getRow() * scale + scale / 2,
                            neighbor.getCol() * scale + scale / 2,         // to neighbor cell center (x2, y2)
                            neighbor.getRow() * scale + scale / 2);
                 neighbor.drawAllPrevs(maze, g, scale, c);                 // Recurse to continue drawing from neighbor
             }
         }
     }
 
     /**
      * Recursively draws lines from this Cell until reaching a Cell whose
      * {@code prev} is {@code null} or itself.
      * 
      * @param g     the Graphics object on which to draw.
      * @param scale the scale by which to draw.
      * @param c     the Color to drawn with.
      */
     public void drawPrevPath(Graphics g, int scale, Color c) {    // Draw a chain back through prev links
         g.setColor(c);                                            // Set line color
         if (getPrev() != null && getPrev() != this) {             // Continue until prev is null or self (guard)
             g.drawLine(getCol() * scale + scale / 2,              // Line from this center
                        getRow() * scale + scale / 2,
                        getPrev().getCol() * scale + scale / 2,    // to previous cell center
                        getPrev().getRow() * scale + scale / 2);
             getPrev().drawPrevPath(g, scale, c);                  // Recurse to previous cell
         }
     }

     
 
     /**
      * Draws this Cell onto the given Graphics object with the given Color.
      * 
      * @param g     the Graphics object on which to draw.
      * @param scale the scale by which to draw.
      * @param c     the Color to draw with.
      */
     public void draw(Graphics g, int scale, Color c) {            // Fill the interior of the cell
         g.setColor(c);                                            // Set the fill color
         g.fillRect(getCol() * scale + 2,                          // Draw slightly inset rectangle (x)
                    getRow() * scale + 2,                          // (y)
                    scale - 4,                                     // width (inset leaves a border)
                    scale - 3);                                    // height (minor asymmetry preserves visible border)
     }
 }
 