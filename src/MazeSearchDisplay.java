/*
 * File Name:    MazeSearchDisplay.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Swing-based visualization window for maze search algorithms.
 *               Hosts a JFrame and a custom JPanel (inner class Panel) that
 *               calls AbstractMazeSearch.draw(...) to render the maze, the
 *               frontier tree, and the current/solution path. Also supports
 *               saving the current window contents to an image file.
 *
 * How to Run:   Construct with an AbstractMazeSearch instance and a cell scale:
 *                   MazeSearchDisplay d = new MazeSearchDisplay(searcher, 20);
 *               Then periodically call d.repaint() from the search loop or timer.
 */

 import java.awt.BorderLayout;        // BorderLayout for placing the drawing panel in the frame
 import java.awt.Color;               // Color constants for backgrounds/graphics
 import java.awt.Component;           // Component base class used for snapshotting the UI
 import java.awt.Dimension;           // Dimension for preferred panel size
 import java.awt.Graphics;            // Graphics context for custom painting
 import java.awt.image.BufferedImage; // BufferedImage used when saving a snapshot
 import java.io.File;                 // File for image output
 import java.io.IOException;          // IOException handling during ImageIO.write
 import javax.imageio.ImageIO;        // ImageIO for writing image files
 import javax.swing.JFrame;           // JFrame window container
 import javax.swing.JPanel;           // JPanel drawing surface
 
 public class MazeSearchDisplay {      // Display class that owns a window and a drawing panel
     JFrame win;                       // Top-level Swing window
     protected AbstractMazeSearch searcher; // The search object that knows how to draw itself
     private Panel canvas;             // Inner Panel subclass instance that does the actual painting
     private int gridScale;            // Width/height in pixels for each maze cell
 
     /**
      * Initializes a display window for a MazeSearcher.
      * 
      * @param searcher the AbstractMazeSearch to visualize
      * @param scale    the pixel size of each maze cell
      */
     public MazeSearchDisplay(AbstractMazeSearch searcher, int scale) {
         // setup the window
         this.win = new JFrame("Maze-Search");               // Create a new application window titled "Maze-Search"
         this.win.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit program when the window is closed
 
         this.searcher = searcher;                           // Keep a reference to the search object for drawing
         this.gridScale = scale;                             // Store the per-cell pixel size
 
         // create a panel in which to display the MazeSearcher
         // put a buffer of two rows around the display grid
         this.canvas = new Panel(                            // Create the drawing panel with explicit pixel size
             (int) (this.searcher.getMaze().getCols() + 2) * this.gridScale, // width: cols + 2 border cells
             (int) (this.searcher.getMaze().getRows() + 2) * this.gridScale  // height: rows + 2 border cells
         );
 
         // add the panel to the window, layout, and display
         this.win.add(this.canvas, BorderLayout.CENTER);     // Place the canvas at the center of the frame
         this.win.pack();                                     // Pack to preferred sizes (Panel decides its size)
         this.win.setVisible(true);                           // Show the window on screen
     }
 
     public void setMazeSearch(AbstractMazeSearch searcher) { // Setter to swap the searcher being displayed
         this.searcher = searcher;                            // Replace current searcher reference
     }
 
     public void closeWindow() {                              // Programmatically close the window
         this.win.dispose();                                  // Dispose of the frame and its resources
     }
 
     /**
      * Saves an image of the display contents to a file. The supplied
      * filename should have an extension supported by javax.imageio, e.g.
      * "png" or "jpg".
      *
      * @param filename the name of the file to save
      */
     public void saveImage(String filename) {
         // get the file extension from the filename
         String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length()); // Extract "png"/"jpg", etc.
 
         // create an image buffer to save this component
         Component tosave = this.win.getRootPane();           // Get the root pane (entire window contents)
         BufferedImage image = new BufferedImage(             // Create an offscreen image the same size as the root
                 tosave.getWidth(),
                 tosave.getHeight(),
                 BufferedImage.TYPE_INT_RGB);
 
         // paint the component to the image buffer
         Graphics g = image.createGraphics();                 // Acquire Graphics context for the BufferedImage
         tosave.paint(g);                                     // Ask the component tree to paint itself into the image
         g.dispose();                                         // Release graphics context resources
 
         // save the image
         try {
             ImageIO.write(image, ext, new File(filename));   // Write the image to disk using the given extension
         } catch (IOException ioe) {
             System.out.println(ioe.getMessage());            // Print an error message if writing fails
         }
     }
 
     /**
      * This inner class provides the panel on which MazeSearcher elements
      * are drawn.
      */
     private class Panel extends JPanel {                     // Custom drawing surface
         /**
          * Creates the panel.
          * 
          * @param width  the width of the panel in pixels
          * @param height the height of the panel in pixels
          */
         public Panel(int width, int height) {
             super();                                         // Call JPanel constructor
             this.setPreferredSize(new Dimension(width, height)); // Advertise the preferred panel size to the layout
             this.setBackground(Color.BLACK);                 // Fill any background gaps in black
         }
 
         /**
          * Method overridden from JComponent that is responsible for
          * drawing components on the screen. The supplied Graphics
          * object is used to draw.
          * 
          * @param g the Graphics object used for drawing
          */
         public void paintComponent(Graphics g) {
             // take care of housekeeping by calling parent paintComponent
             super.paintComponent(g);                         // Clear background & run standard setup
             g.translate(gridScale, gridScale);               // Shift origin to leave a one-cell border on all sides
 
             // call the MazeSearcher draw method here
             searcher.draw(g, gridScale);                     // Delegate actual drawing to the search object
         } // end paintComponent
     } // end Panel
 
     public void repaint() {                                  // Expose a simple repaint trigger to callers
         this.win.repaint();                                  // Ask Swing to schedule a repaint of the window
     }
 }
 