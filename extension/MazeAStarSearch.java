import java.util.Comparator;   // Import Comparator for ordering cells by A* f-score
/*
 * File Name:    MazeAStarSearch.java
 * Author:       Azeem Gbolahan
 * Last Modified: 03/12/2025
 * Description:  A* maze searcher that extends AbstractMazeSearch. It maintains a priority
 *               queue (min-heap) ordered by f(n) = g(n) + h(n), where:
 *                 - g(n) is the path cost from start to the cell (computed via traceback size)
 *                 - h(n) is the Manhattan distance to the target cell
 *               The class defines how to add, pop, update, and count frontier cells.
 *
 * How to Run:   Compile with your Maze project sources and invoke from a driver that
 *               constructs Maze, start/target Cells, and calls search(start, target, ...).
 */

public class MazeAStarSearch extends AbstractMazeSearch { // Concrete A* searcher specializing AbstractMazeSearch
    // a priority queue is needed to implement bfs 
    private PriorityQueue<Cell> priorityQueue;            // Frontier: min-heap keyed by A* f-score

    public MazeAStarSearch(Maze maze){                    // Constructor wires up min-heap with A* comparator
        super(maze);                                      // Initialize superclass (stores maze, clears state)
        this.priorityQueue = new Heap<>(new Comparator<Cell>() { // Create a Heap<Cell> with custom comparator
            @Override
            public int compare(Cell cell1, Cell cell2) {  // Define ordering: smaller f-score = higher priority
                if (MazeAStarSearch.this.getStart() == null || MazeAStarSearch.this.getTarget()  == null){
                    return 0;                             // If start/target not set yet, treat as equal priority
                } else {
                    int pathCost1 = traceback(cell1).size() - 1; // g(n) for cell1: steps from start (exclude start)
                    int pathCost2 = traceback(cell2).size() - 1; // g(n) for cell2: steps from start (exclude start)

                    int estToTarget1 = Math.abs(cell1.getRow() - MazeAStarSearch.this.getTarget().getRow())
                                     + Math.abs(cell1.getCol() - MazeAStarSearch.this.getTarget().getCol()); // h(n) Manhattan
                    int estToTarget2 = Math.abs(cell2.getRow() - MazeAStarSearch.this.getTarget().getRow())
                                     + Math.abs(cell2.getCol() - MazeAStarSearch.this.getTarget().getCol()); // h(n) Manhattan

                    int totalCost1 = pathCost1 + estToTarget1; // f(n) for cell1
                    int totalCost2 = pathCost2 + estToTarget2; // f(n) for cell2

                    return totalCost1 - totalCost2;            // Negative => cell1 before cell2 (min-heap on f)
                }
            }
        }, false); // false because we want a min-heap (lower f-score dequeues first)
    }

    /**
     * Returns the next cell to explore (highest priority under A*, i.e., smallest f-score).
     */
    @Override
    public Cell findNextCell(){
        return this.priorityQueue.poll();     // Pop the min f-score cell from the heap
    }

    /**
     * Adds the given Cell to the frontier.
     */
    @Override
    public void addCell(Cell next){
        this.priorityQueue.offer(next);       // Insert cell into heap; bubbleUp restores order
    }

    /**
     * Returns the number of Cells remaining in the frontier (heap size).
     */
    @Override
    public int numRemainingCells(){
        return this.priorityQueue.size();     // Delegate to heap size
    }

    /**
     * Updates the priority (f-score) of a Cell already present in the frontier.
     * Used when we discover a shorter path to an already-seen Cell.
     */
    @Override
    public void updateCell(Cell next) {
        this.priorityQueue.updatePriority(next); // Heap re-heapifies around this element
    }
}
