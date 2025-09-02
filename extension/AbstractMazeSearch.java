import java.awt.Color;    // Import Color for drawing colored cells/paths
import java.awt.Graphics; // Import Graphics for rendering the maze and overlays
import java.util.HashMap;
import java.util.Map;

/*
 * File Name:    AbstractMazeSearch.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Abstract base class for maze search algorithms (e.g., BFS, DFS, A*).
 *               It holds shared state (maze, start, target, current cell) and provides
 *               the main search loop that delegates data-structure behavior to subclasses
 *               via abstract hooks (findNextCell, addCell, updateCell, numRemainingCells).
 *
 * How to Run:   Extend this class with a concrete searcher (e.g., DFSMazeSearch, BFSMazeSearch, AStarSearch)
 *               that implements the abstract methods. Instantiate the subclass and call search(...).
 */

abstract class AbstractMazeSearch {      // Begin abstract base class; concrete subclasses provide strategy

    /**
     * Maze object that is used to represent the maze
     */
    private Maze maze;                   // The Maze being searched (grid of Cells)

    /**
     * The starting cell of the maze
     */
    private Cell start;                  // Reference to the starting Cell (set during search)

    /**
     * The target cell of the maze
     */
    private Cell target;                 // Reference to the target Cell (set during search)

    /**
     * The current cell being explored
     */
    private Cell cur;                    // Cell currently being expanded in the loop


    // track where the agent currently "stands" and total walking cost
    private Cell agentPos = null;    //this records the current position of the search agent
    private int executionCost = 0;   //tracks the number of steps the algorithm takes to find a solution or exhaust all possibilities 


    /**
     * Constructor for the AbstractMazeSearch class.
     * 
     * @param maze the Maze to be searched
     */
    public AbstractMazeSearch(Maze maze){ // Constructor sets up shared maze reference and clears state
        this.maze = maze;                 // Store provided maze reference
        this.start = null;                // Initialize start cell as unset
        this.target = null;               // Initialize target cell as unset
        this.cur = null;                  // Initialize current cell as unset
        this.agentPos = this.start;       // the search agents begins from the start
        this.executionCost = 0; //the execution cost(number of steps walked ) is zero at the beginning of the search
    }

    /**
     * Returns the next Cell to explore and removes it from the data structure.
     * Subclasses define how the "frontier" behaves (stack, queue, heap, etc.).
     * 
     * @return the next Cell to explore
     */
    public abstract Cell findNextCell();  // Hook: pop/dequeue/remove-min from the frontier

    /**
     * Adds the given Cell to the data structure.
     * Subclasses define how the frontier enqueues/ pushes/ inserts.
     * 
     * @param next the Cell to add
     */
    public abstract void addCell(Cell next); // Hook: push/enqueue/insert a neighbor into the frontier

    /**
     * Updates the given Cell priority in the priority queue (used only in A* search).
     * For non-priority-queue strategies (DFS/BFS), this may be a no-op.
     * 
     * @param next the Cell to update
     */
    public abstract void updateCell(Cell next); // Hook: decrease-key / reprioritize (A*) or no-op otherwise

    /**
     * Returns the number of cells remaining in the data structure.
     * 
     * @return the number of cells remaining in the data structure
     */
    public abstract int numRemainingCells(); // Hook: size() of the frontier (0 means done)

    /**
     * Return the maze object.
     * 
     * @return the maze object 
     */
    public Maze getMaze() {               // Simple accessor for the maze
        return maze;                      // Return stored maze reference
    }

    /**
     * Returns the starting Cell of the maze.
     * 
     * @return the starting Cell of the maze
     */
    public Cell getStart() {              // Accessor for start cell
        return start;                     // Return start reference
    }

    /**
     * Returns the target Cell of the maze.
     * 
     * @return the target Cell of the maze
     */
    public Cell getTarget() {             // Accessor for target cell
        return target;                    // Return target reference
    }

    /**
     * Returns the current Cell being explored.
     * 
     * @return the current Cell being explored
     */
    public Cell getCur() {                // Accessor for current cell
        return cur;                       // Return current reference
    }

    public int getExecutionCost(){
        return this.executionCost;
    }

    /**
     * Sets the current Cell being explored.
     * 
     * @param cur the current Cell being explored
     */
    public void setCur(Cell cur) {        // Mutator for current cell
        this.cur = cur;                   // Update current reference
    }

    /**
     * Resets the search by setting the starting and target cells to null.
     * Also clears the current cell pointer.
     */
    public void reset(){                  // Reset high-level search state (not per-cell prev links)
        this.start = null;                // Clear start
        this.target = null;               // Clear target
        this.cur = null;                  // Clear current
        this.agentPos = null;   // the search agent is initialized as null in a new search
        this.executionCost = 0; // no walk is made in a new search / starting search
    }

    /**
     * Returns the path from the starting cell to the target cell if it exists.
     * Follows prev links backward from the provided cell to start (exclusive of start).
     * 
     * @param cell the target cell (or any cell to trace back from)
     * @return a LinkedList path from the provided cell back toward start (not including start)
     */
    public LinkedList<Cell> traceback(Cell cell){ // Build the path by walking prev pointers
        LinkedList<Cell> path = new LinkedList<Cell>(); // Initialize path container
        while (cell!=start){                  // Continue until reaching the start sentinel
            path.add(cell);                   // Append current cell to path
            cell = cell.getPrev();            // Step backward along the prev chain
        }
        return path;                          // Return collected path (start not included)
    }

    /**
     * Searches the maze for the target cell using the given starting cell.
     * The frontier behavior is defined by the subclass via the abstract hooks.
     *
     * Control flags:
     *  - display: whether to show a GUI stepping visualization
     *  - delay:   sleep milliseconds between GUI repaints
     * 
     * @param start  the starting cell
     * @param target the target cell
     * @param display whether to render a visualization window
     * @param delay   milliseconds between frames if display==true
     * @return the path from the starting cell to the target cell if found, null otherwise
     */
    public LinkedList<Cell> search(Cell start, Cell target, boolean display, int delay){
        // Set the starting and target cells
        this.start = start;                 // Record start reference for this search
        this.target = target;               // Record target reference for this search
        
        setCur(start);                      // Initialize current to start

        // This line is just to make the drawing work correctly
        start.setPrev(start);               // Mark start as "visited" with its own prev set to itself

        addCell(start);                     // Seed the frontier with the start cell

        // Create a null display window
        MazeSearchDisplay displayWindow = null; // Placeholder for optional GUI

        // If display is true, create a new display window
        if(display){                        // If visualization requested,
            displayWindow = new MazeSearchDisplay(this, 20); // open a window with fixed cell scale (20)
        }

        // While there are still cells to explore
        while(numRemainingCells()>0){       // Iterate until frontier is empty
            // Set the current cell to the next cell to explore
            setCur(findNextCell());         // Strategy-specific pop/dequeue/extract-min

            int walk = ropeDistance(agentPos, getCur());   // steps along prev-tree from where we are to cur
            if (walk > 0) executionCost += walk;  // execution cost is the sum total of all walks in the search
            agentPos = getCur();  // the agent now is the current position ; the agent changes according to the current cell being explored and the most recently explored one 


            for(Cell neighbor: maze.getNeighbors(cur)){ // For each valid, non-obstacle neighbor
                // if the neighbor has not been visited set its previous cell to the current cell
                // and add it to the data structure
                if (neighbor.getPrev() == null){ // Unvisited check (prev==null implies undiscovered)
                    neighbor.setPrev(cur);       // Set tree/parent pointer to current
                    addCell(neighbor);           // Push/enqueue/insert neighbor into frontier
                }
                // if the neighbor has been visited and the we find a shorter path to it
                // set its previous cell to the current cell and update its position in the data structure
                else if (traceback(neighbor).size()>traceback(cur).size()){ // Found better path length via cur
                    neighbor.setPrev(cur);       // Update parent pointer to reflect shorter path
                    updateCell(neighbor);        // Reprioritize in frontier (A*), or no-op for DFS/BFS
                }

                // if the neighbor is the target cell, we found the target
                if(neighbor == target){          // Target reached condition
                    return traceback(target);    // Return reconstructed path (start not included)
                }
            }

            // if display is true, repaint the display window
            if(display){                         // On visualization mode,
                try {
                    Thread.sleep(delay);         // Throttle frame rate by sleeping
                } catch (InterruptedException e) {} // Ignore interruption for demo simplicity
                displayWindow.repaint();         // Repaint the display window to show progress
            }
        }

        return null;                             // Frontier exhausted without reaching target
    }

    /**
     * Draws the maze and the path taken by the searcher.
     * Renders: the maze, all prev-tree edges, and highlights start/target/current.
     * If the target was found (has a prev), it overlays the shortest path in blue/green.
     * 
     * @param g     the Graphics object used to draw the maze
     * @param scale the scale at which to draw the maze
     */
    public void draw(Graphics g, int scale) {
        // Draws the base version of the maze
        getMaze().draw(g, scale);                              // Render grid with obstacles and visited marking
        // Draws the paths taken by the searcher
        getStart().drawAllPrevs(getMaze(), g, scale, Color.RED); // Draw spanning tree of discovered edges in red
        // Draws the start cell
        getStart().draw(g, scale, Color.BLUE);                 // Start highlighted in blue
        // Draws the target cell
        getTarget().draw(g, scale, Color.RED);                 // Target highlighted in red
        // Draws the current cell
        getCur().draw(g, scale, Color.MAGENTA);                // Current exploration cell in magenta
    
        // If the target has been found, draws the path taken by the searcher to reach
        // the target sans backtracking.
        if (getTarget().getPrev() != null) {                   // Only if target was reached
            Cell traceBackCur = getTarget().getPrev();         // Start backtracking from target's parent
            while (!traceBackCur.equals(getStart())) {         // Walk until reaching the start
                traceBackCur.draw(g, scale, Color.GREEN);      // Shade path cells in green
                traceBackCur = traceBackCur.getPrev();         // Step backward
            }
            getTarget().drawPrevPath(g, scale, Color.BLUE);    // Draw the actual prev path as a blue polyline
        }
    }

    /**
     * Returns the number of walking steps to move from cell 'from' to cell 'to'
     * by using only the already-discovered prev-links (the "rope" tree).
     *
     * Intuition:
     *   1) Walk upward from 'from' via getPrev(), recording each ancestor and
     *      how many steps it is from 'from'.
     *   2) Walk upward from 'to' until we hit one of those recorded ancestors.
     *      That meeting point is the lowest common ancestor (LCA).
     *   3) Distance = steps(from -> LCA) + steps(to -> LCA).
     *
     * If either input is null, or if the two cells are not connected in the
     * current discovered tree, this returns -1.
     */
    public int ropeDistance(Cell from, Cell to) {
        if (from == null || to == null) {
            return -1; // invalid inputs
        }

        // 1) Record every ancestor of 'from' with its distance from 'from'.
        Map<Cell, Integer> distFromFrom = new HashMap<>();
        int stepsUpFrom = 0;
        for (Cell cur = from; cur != null; cur = parentInRope(cur)) {
            distFromFrom.put(cur, stepsUpFrom);

            // If we've reached the root (start), stop climbing
            if (isRopeRoot(cur)) {
                break;
            }
            stepsUpFrom++;
        }

        // 2) Climb up from 'to' until we meet an ancestor recorded above.
        int stepsUpFromTo = 0;
        for (Cell cur = to; cur != null; cur = parentInRope(cur)) {
            Integer stepsFromSide = distFromFrom.get(cur);
            if (stepsFromSide != null) {
                // 'cur' is the LCA. Total rope distance is sum of both climbs.
                return stepsFromSide + stepsUpFromTo;
            }

            if (isRopeRoot(cur)) {
                break;
            }
            stepsUpFromTo++;
        }

        // Not connected in the discovered tree (shouldn't happen for frontier nodes).
        return -1;
    }

    /**
     * Helper: returns true if this cell is the root of the discovered rope tree.
     * Works with either convention:
     *   - start.setPrev(start)  (self-pointer)
     *   - start.getPrev() == null
     */
    private boolean isRopeRoot(Cell c) {
        return c.getPrev() == null || c.getPrev() == c;
    }

    /**
     * Helper: move one step "up" the rope tree.
     * If prev points to self (root sentinel), we return null to stop.
     */
    private Cell parentInRope(Cell c) {
        if (c == null) return null;
        Cell p = c.getPrev();
        return (p == c) ? null : p;
    }
}
