/*
 * File Name:    MazeBreadthFirstSearcher.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Breadth-First Search (BFS) maze searcher. Extends AbstractMazeSearch and
 *               uses a FIFO queue to explore the maze in layers (shortest-path in edge count
 *               for unweighted grids). The concrete methods define how the frontier behaves.
 *
 * How to Run:   Compile with your Maze project sources and use from a driver that constructs
 *               Maze, start/target Cells, and invokes search(start, target, display, delay).
 */

 public class MazeBreadthFirstSearch extends AbstractMazeSearch { // Concrete BFS searcher

    // a queue is needed to implement bfs 
    private Queue<Cell> queue;            // Frontier: FIFO queue of Cells (explored in insertion order)

    public MazeBreadthFirstSearch(Maze maze){ // Constructor wires the BFS frontier
        super(maze);                            // Initialize shared state in AbstractMazeSearch
        queue = new LinkedList<>();             // Use the provided LinkedList<T> as a Queue<Cell> implementation
    }

    /**
     * Returns the next cell to explore which is the first cell added to the queue.
     * For BFS, we dequeue in FIFO order to expand the shallowest frontier first.
     */
    @Override
    public Cell findNextCell(){
        if (queue.size() == 0){                // If the frontier is empty,
            return null;                       // return null (no work remains)
        }
        Cell nextCell = queue.poll();          // Remove and return the head of the queue (oldest enqueued)
        return nextCell;                       // Hand the next cell to the search loop
    }

    /**
     * Adds the given Cell to whatever structure is storing the future Cells to explore.
     * For BFS, enqueuing appends to the tail of the queue (FIFO).
     */
    @Override
    public void addCell(Cell cell){
        this.queue.offer(cell);                // Enqueue neighbor at the back of the queue
    }

    /**
     * Returns the number of future Cells to explore (size of the frontier).
     */
    @Override
    public int numRemainingCells(){
        return this.queue.size();              // Report current queue length
    }

    /**
     * BFS does not reprioritize elements (no weights), so update is a no-op.
     */
    @Override
    public void updateCell(Cell next) {        // Provided for interface symmetry with A*
        // no-op for BFS                         // Intentionally empty
    }
}
