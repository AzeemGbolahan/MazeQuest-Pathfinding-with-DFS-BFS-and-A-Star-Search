/*
 * File Name:    Heap.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  A generic binary heap implementation that supports both min-heap and max-heap behavior
 *               via a Comparator<T>. Implements a PriorityQueue<T>-like interface with offer, poll, peek,
 *               and size operations. Uses an ArrayList<T> as the underlying storage and maintains the
 *               heap property using bubble-up and bubble-down operations after mutations.
 *
 * How to Run:   Compile with `javac Heap.java` and use from a driver class (e.g., JobMaker).
 */

 import java.util.ArrayList;                 // Import ArrayList used to store heap elements in array form
 import java.util.Comparator;                // Import Comparator to define min- or max-heap ordering
 
 public class Heap<T> implements PriorityQueue<T>{ // Class declaration with generic type T and PriorityQueue interface
     // the instance field 
     private Comparator<T> comparator;       // Comparator used to order elements (defines min- or max-heap behavior)
     private ArrayList<T> heapArray;         // ArrayList backing storage for heap nodes in level-order
     private boolean maxHeap = false;        // Flag indicating max-heap (true) or min-heap (false); default is min-heap
 
     public Heap(Comparator<T> comparator, boolean maxHeap){ // Constructor allowing explicit heap type selection
         this.heapArray = new ArrayList<>(); // Initialize underlying storage as empty ArrayList
 
         if (maxHeap == true){               // If caller requests a max-heap behavior
             this.comparator = new Comparator<T>(){                 // Create an inline Comparator implementation
                 @Override
                 public int compare(T o1, T o2) {                  // Compare function for max-heap
                     return ((Comparable<? super T>) o2).compareTo(o1); // Reverse natural order: larger has higher priority
                 }                                                 // End compare
             };                                                    // End anonymous Comparator
             this.maxHeap = true;                                  // Record that this is a max-heap
         }
         else{                                                     // Otherwise treat as min-heap
             this.comparator = comparator;                         // Use the provided comparator for min-heap logic
         }
     }
 
     /**
      * Returns the bigger (higher-priority) of two elements based on the comparator.
      */
     private T getBigger(T a, T b) {                               // Helper to choose higher-priority of two
         if (this.comparator.compare(a, b) > 0) {                  // If a > b by comparator
             return a;                                             // a is higher-priority
         } else {                                                  // Otherwise
             return b;                                             // b is higher-priority or equal
         }
     }
 
     /**
      * Automatically create a min heap
      */
     public Heap(Comparator<T> comparator){                        // Convenience constructor for a min-heap
         this.comparator = comparator;                             // Store comparator for natural/min ordering
         this.heapArray = new ArrayList<>();                       // Initialize storage
     }
 
     /**
      * Swaps two element depending on the logic 
      */
     private void swap(int idx1, int idx2){                        // Swap elements at two indices
         if (!isValid(idx1)|| !isValid(idx2)){                     // Guard: indices must be within bounds
             System.out.println("Invalid index- Out of the array bound"); // Diagnostic for invalid swap
             return;                                               // Abort swap on invalid indices
         }
         T swapped1 = this.heapArray.get(idx1);                    // Read element at idx1
         T swapped2 = this.heapArray.get(idx2);                    // Read element at idx2
 
         this.heapArray.set(idx1, swapped2);                       // Write element2 into idx1
         this.heapArray.set(idx2, swapped1);                       // Write element1 into idx2
     }
 
     /**
      * Checks if a valid index is valid according to the array size before performing any operation 
      */
     private boolean isValid(int idx){                             // Bounds check helper
         return (idx < this.heapArray.size());                     // Valid if idx is less than size (non-negative assumed)
     }
 
     /**
      * returns the index of the parent of the element at the given index
      */
     private int getParentIdx (int idx){                           // Compute parent index in array heap
         if ((idx == 0)){                                          // Root has no parent
             return -1;                                            // Use -1 to indicate no parent
         }
         else{                                                     // For non-root nodes
             int parentIdx = (idx - 1)/2 ;                         // Parent formula for 0-based heap array
             return parentIdx;                                     // Return computed parent index
         }
     }
 
     /**
      * Returns the index of the left child of the element at the given index
      */
     private int getLeftChildIdx(int idx){                         // Compute left child index
         int LeftChildIdx = (2 * idx) + 1;                         // Left child formula for 0-based heap array
         if (LeftChildIdx >= this.heapArray.size()){               // If beyond end of array
             return -1;                                            // Indicate no left child
         }
         return LeftChildIdx;                                      // Otherwise return index
     }
 
     /**
      * Returns the index of the right child of the element at the given index
      */
     private int getRightChildIdx(int idx){                        // Compute right child index
         int RightChildIdx = (2 * idx) + 2;                        // Right child formula for 0-based heap array
         if (RightChildIdx >= this.heapArray.size()){              // If beyond end of array
             return -1;                                            // Indicate no right child
         }
         return RightChildIdx;                                     // Otherwise return index
     }
 
     /**
      * places an inserted element correctly by bubbling it up the array till it finds the right spot
      */
     private void bubbleUp(int idx){                               // Restore heap order by moving up
         // base case - if our idx is at 0, then it cannot longer go up the array - no space to go up to
         if (idx == 0){                                            // If already at root,
             return;                                               // nothing to do
         }
         else{                                                     // Otherwise,
             while (idx > 0){                                      // While not at root
                 T bubbled = this.heapArray.get(idx);              // Element attempting to bubble up
                 T bubbledParent = this.heapArray.get(this.getParentIdx(idx)); // Parent element
                 if (this.comparator.compare(bubbled,bubbledParent)<0){ // If child has higher priority than parent
                     this.swap(idx, this.getParentIdx(idx));       // Swap child with parent
                     idx = getParentIdx(idx);                      // Continue from the parent's index
                 }
                 else {                                            // If heap property is satisfied
                     break;                                        // Stop bubbling
                 }
             }
         }
     }
 
     /**
      * places an element that is replaced the deleted root element correctly by bubbling it down the array till it finds the right spot
      */
     private void bubbleDown(int idx){                             // Restore heap order by moving down
         if (idx >= this.heapArray.size()-1){                      // If at or beyond last valid index
             return ;                                              // No bubbling needed
         }
         else{                                                     // Otherwise perform bubbling
             T bubbled = this.heapArray.get(idx);                  // Current element to potentially move down
             while(idx < this.heapArray.size()){                   // Iterate while inside array bounds
                 int LeftChildIdx = this.getLeftChildIdx(idx);     // Compute left child index
                 int RightChildIdx = this.getRightChildIdx(idx);   // Compute right child index
                 if(LeftChildIdx == -1){                           // If no left child (and thus no children)
                     return;                                       // Done bubbling
                 }
                 else if (LeftChildIdx != -1 && RightChildIdx == -1){ // If only left child exists
                     T compared = this.heapArray.get(LeftChildIdx);   // Value of left child
                     if (this.comparator.compare(bubbled,compared)>0){ // If parent lower priority than left child
                         this.swap(idx,LeftChildIdx);              // Swap with left child
                         idx = LeftChildIdx;                       // Continue from new position
                         bubbled = this.heapArray.get(idx);        // Refresh current element reference
                     }else{                                        // Otherwise heap property holds
                         break;                                     // Stop bubbling
                     }
                 }
                 else if (LeftChildIdx != -1 && RightChildIdx != -1){ // If both children exist
                     T leftChild = this.heapArray.get(this.getLeftChildIdx(idx));  // Read left child
                     T rightChild = this.heapArray.get(this.getRightChildIdx(idx)); // Read right child
                     int indexCompared = (this.comparator.compare(leftChild, rightChild) < 0) ? LeftChildIdx : RightChildIdx; // Pick higher-priority child
                     T compared = this.heapArray.get(indexCompared); // Value of chosen child
                     if (this.comparator.compare(bubbled,compared)>0){ // If parent lower priority than chosen child
                         this.swap(idx,indexCompared);            // Swap parent with chosen child
                         idx = indexCompared;                     // Continue from child's position
                         bubbled = this.heapArray.get(idx);       // Refresh current element reference
                     }else{                                       // Otherwise heap property holds
                         break;                                    // Stop bubbling
                     }
                 }
             }
         }
     }
 
     public String toString() {                                   // Public toString to visualize heap as a tree
         int depth = 0 ;                                          // Start at depth 0 (root)
         return toString( 0 , depth );                            // Delegate to recursive helper from root index
     }
     
     private String toString( int idx , int depth ) {             // Recursive helper to print heap sideways
         if (idx >= this.heapArray.size() ) {                     // If index out of bounds,
             return "";                                           // return empty string
         }
         String left = toString(getLeftChildIdx( idx ) , depth + 1 );  // Recurse left subtree, increasing depth
         String right = toString(getRightChildIdx( idx ) , depth + 1 );// Recurse right subtree, increasing depth
 
         String myself = "\t".repeat(depth) + this.heapArray.get( idx ) + "\n"; // Indent by depth and print current node
         return right + myself + left;                              // Print right subtree above, then node, then left (visual tree)
     }
 
     /**
      * Returns the number of items in the heap
      */
     public int size(){                                           // Size accessor
         return this.heapArray.size();                            // Delegate to ArrayList size
     }
 
     /**
      * Returns the element of the highest priority in the queue
      */
     public T peek(){                                             // Read-only access to top of heap
         if (this.heapArray.isEmpty()){                           // If heap is empty
             return null;                                         // Return null (no element)
         }
         return this.heapArray.get(0);                            // Return root element (highest priority)
     }
 
     /**
      * Adds a specified item into the heap then calls bubbleup to take it to the right spot
      */
     public void offer(T item){                                   // Insert an item into the heap
         this.heapArray.add(item);                                // Append item at end (next available leaf position)
         this.bubbleUp(this.heapArray.size() - 1);                // Restore heap order by bubbling up from last index
     }
 
     /**
      * Returns and removes an item of highest priority in the heap 
      */
     public T poll(){                                             // Remove and return highest-priority item
         if(this.heapArray.isEmpty()){                            // If heap has no elements
             return null;                                         // Return null (nothing to remove)
         }
         T removed = this.heapArray.get(0);                       // Save current root to return later
         T toBeBubbled = this.heapArray.remove(this.heapArray.size() - 1); // Remove last element (end of array)
         if (!this.heapArray.isEmpty()) {                         // If heap still has elements after removal
             this.heapArray.set(0, toBeBubbled);                  // Move last element to root position
             this.bubbleDown(0);                                  // Restore heap order by bubbling down from root
         }
         return removed;                                          // Return the originally removed root
     }
 
     /**
      * Updates the priority of the given item by restoring the heap property.
      * Assumes all other items in the heap have unchanged priorities.
      */
     public void updatePriority(T item) {                         // Re-heapify around a specific item whose priority changed
         // Linearly search for the item's index
         int index = -1;                                          // Initialize index as not found
         for (int i = 0; i < heapArray.size(); i++) {             // Iterate over all heap positions
             if (heapArray.get(i).equals(item)) {                 // If we find the item by equals
                 index = i;                                       // Record its index
                 break;                                           // Stop searching
             }
         }
 
         // If item is not found, do nothing
         if (index == -1) return;                                 // Exit if the item is not in the heap
 
         // Compare with parent to determine if we need to bubble up or down
         int parentIdx = getParentIdx(index);                     // Compute parent index
         if (parentIdx >= 0 && comparator.compare(heapArray.get(index), heapArray.get(parentIdx)) < 0) {
             bubbleUp(index);                                     // If item has higher priority than parent, bubble up
         } else {
             bubbleDown(index);                                   // Otherwise (or root), bubble down to restore order
         }
     }
 }
 