# 🧩 Maze Quest Searcher: Pathfinding with DFS, BFS & A*

This project implements multiple search algorithms to solve mazes, including **Depth-First Search (DFS)**, **Breadth-First Search (BFS)**, and **A\***. Built in Java, the program visualizes how different algorithms explore the maze, compare efficiency, and analyze solution quality.  

---

## 🎯 Project Objective  

The goal of this project is to:  

- Explore **search strategies** for navigating mazes  
- Compare **algorithm efficiency** (time, space, and path quality)  
- Provide a **visual representation** of the search process  
- Build **custom data structures** to support algorithm design  

---

## 🛠 Features  

- ✅ Multiple search algorithms: DFS, BFS, and A*  
- 🎨 Graphical visualization with `MazeSearchDisplay`  
- 🔁 Step-wise exploration of maze search paths  
- 🧱 Custom data structures: `LinkedList`, `Queue`, `Stack`, `Heap`, `PriorityQueue`  
- 📊 Path length analysis with `PathLengthAnalyzer`  
- 🧪 Unit tests for data structures and search algorithms  
- ⚙️ Modular design for adding new search methods  

---


## 📁 File Structure
```
Project3/
├── Project7Report.pdf # Detailed project write-up
├── src/ # Source code folder
│   ├── Maze.java # Maze representation
│   ├── Cell.java # Cell representation
│   ├── CellType.java # Defines wall, path, etc.
│   ├── AbstractMazeSearch.java # Base class for search algorithms
│   ├── MazeDepthFirstSearch.java # DFS implementation
│   ├── MazeBreadthFirstSearch.java# BFS implementation
│   ├── MazeAStarSearch.java # A* implementation
│   ├── PathLengthAnalyzer.java # Analyzes the length of the solution paths
│   ├── MazeSearchDisplay.java # GUI visualization
│   ├── LinkedList.java # Custom linked list
│   ├── Queue.java # Custom queue
│   ├── Stack.java # Custom stack
│   ├── Heap.java # Custom heap
│   ├── PriorityQueue.java # Custom priority queue
│   ├── MazeTester.java # Runs maze tests and visualizations
│   ├── SearchTests.java # Unit tests for search algorithms
│   └── HeapTests.java # Unit tests for heap
├── extension/                    # Alternative implementation or enhancements
    ├── (Same structure as src/)
```



---

## 🚀 How to Run  

### Step 1: Compile the source files

```bash
cd src
javac *.java
```

### Step 2: Run the simulation for DFS, BFS, and A* respectively - Remember to parse in the command line arguments!

```bash
java MazeTester <number of rows of the maze> <number of columns for the maze> <density ( how blocked the maze shoudl be in probability (0-1) > [algorithm to be used ( "bfs " for BreadthFirestSearch, "dfs" for DepthFirstSearch, "astar" for AStarSearch ) [display=false (true if you want the display to be shown)] [delay=0(set it to how fast/slow you'd like the display to run)]
```


This will open a graphical window displaying the whole maze and the search running in real time.

---

## 🧪 Tests

This project includes unit tests for:

- Search correctness (SearchTests.java)
- Custom Data structures (e.g., HeapTests.java, Linked List, Queue, Stack)
  
To run the tests, use any Java IDE with JUnit support or compile and run the `*Tests.java` files manually.

---

## 🧠 Concepts Explored

- Graph Search Algorithms (DFS, BFS, A*)
- Pathfinding in grids and mazes
- Algorithm performance and path length comparison
- GUI visualization of algorithm progress
- Data structures: stacks, queues, heaps, linked lists
- Object-Oriented Programming and inheritance

## 📄 Project Report

The project includes a detailed write-up of algorithms, design choices, comparisons, and results:

📘 [Project7Report.pdf](Project7Report.pdf)

## 👨‍💻 Author

**Azeem Gbolahan**  
Student of Computer Science & Economics  
Colby College

---

## 🌐 Let’s Connect

Interested in pathfinding algorithms, AI, or interactive visualizations?
Feel free to fork the repo, raise issues, or suggest improvements!






