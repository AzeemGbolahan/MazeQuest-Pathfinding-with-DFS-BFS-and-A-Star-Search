/*
 * File Name:    MazeDepthFirstSearcher.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Depth-First Search (DFS) maze searcher. Extends AbstractMazeSearch and
 *               uses a LIFO stack to explore as deep as possible before backtracking.
 *               Defines how the DFS frontier behaves (push/pop/size) for the shared
 *               search loop in AbstractMazeSearch.
 *
 * How to Run:   Compile with your Maze project sources and invoke a driver that constructs
 *               Maze, start/target Cells, and calls search(start, target, display, delay).
 */

 public class MazeDepthFirstSearch extends AbstractMazeSearch { // Concrete DFS searcher; strategy = stack (LIFO)
    // a stack is needed to implement dfs 
    private Stack<Cell> stack;                 // Frontier: LIFO stack of Cells (deepest-first expansion)

    public MazeDepthFirstSearch(Maze maze){    // Constructor wires the DFS frontier
        super(maze);                           // Initialize shared state in AbstractMazeSearch
        this.stack = new LinkedList<>();       // Use LinkedList<T> as a Stack<Cell> implementation
    }

    /**
     * Returns the next cell to explore which is just the last cell in the stack.
     * DFS expands the most recently discovered (deepest) cell first.
     */
    public Cell findNextCell(){
        return this.stack.pop();               // Pop and return top of stack (last pushed Cell)
    }

    /**
     * Adds the given Cell to whatever structure is storing the future Cells to explore.
     * For DFS, we push onto the stack so it will be explored before older entries.
     */
    public void addCell(Cell next){
        this.stack.push(next);                 // Push neighbor to top of stack
    }

    /**
     * Returns the number of future Cells to explore (size of the DFS frontier).
     */
    public int numRemainingCells(){
        return this.stack.size();              // Report current stack depth
    }

    @Override
    public void updateCell(Cell next) {        // DFS has no notion of reprioritization; no-op
    }
}
