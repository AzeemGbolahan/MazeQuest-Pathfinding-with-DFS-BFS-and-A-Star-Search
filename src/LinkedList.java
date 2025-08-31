/*
 * File Name:    LinkedList.java
 * Author:       Azeem Gbolahan
 * Last Modified: 03/12/2025
 * Description:  A generic singly linked list implementation that supports both Queue<T> and Stack<T> interfaces
 *               as well as Iterable<T>. Provides fundamental list operations (add, remove, get, contains),
 *               front/back helpers (push/offer/peek/poll/pop), and equality checks.
 *               
 * How to Run:   Compile with `javac LinkedList.java` and use from a driver class (e.g., JobMaker).
 */

 import java.util.Iterator;                // Import Iterator to allow enhanced-for iteration over the list
 import java.util.NoSuchElementException;  // Import exception used for operations on empty structures
 
 /**
  * A generic singly linked list implementation with basic operations.
  * Supports iteration, element access, addition, removal, and checking list properties.
  */
 public class LinkedList<T> implements Queue<T>,  Stack<T>, Iterable<T> {  // Class header declaring generic type T and implemented interfaces
 
     // Iterator class for traversing the linked list
     private class LLIterator implements Iterator<T> {        // Inner iterator class to traverse list elements in sequence
         private Node current;                                 // Tracks the next node in traversal
 
         public LLIterator(Node head) {                        // Constructor takes the list head as starting point
             this.current = head;                              // Initialize iterator's cursor to head
         }
 
         public boolean hasNext() {                            // Returns whether another element exists
             return current != null;                           // True if cursor is not null (more elements remain)
         }
 
         public T next() {                                     // Returns next element and advances cursor
             if (!hasNext()) {                                 // If no more elements,
                 throw new NoSuchElementException("No more elements in the list."); // signal with exception
             }
             T data = current.getData();                       // Capture current node's data to return
             current = current.next;                           // Advance cursor to the next node
             return data;                                      // Return captured data
         }
 
         public void remove() {                                // Optional remove (unsupported for this iterator)
             throw new UnsupportedOperationException("remove operation is not supported."); // Indicate unsupported
         }
     }
 
     // Returns an iterator for the linked list
     public Iterator<T> iterator() {                           // Iterable<T> implementation
         return new LLIterator(this.head);                     // Return a new iterator starting at the head
     }
 
     // Inner class representing a node in the linked list
     private class Node {                                      // Node stores data and link to next node
         private Node next;                                    // Reference to next node in the list
         private T item;                                       // The data payload of the node
 
         public Node(T item) {                                 // Node constructor with data
             this.item = item;                                 // Store provided data in node
             this.next = null;                                 // Initialize next as null (end of list by default)
         }
 
         public T getData() {                                  // Getter for the node's data
             return this.item;                                 // Return payload
         }
 
         public void setNext(Node n) {                         // Setter for the node's next pointer
             this.next = n;                                    // Update next reference
         }
     }
 
     private Node head;                                        // First node of the linked list (null if empty)
     private Node tail;                                        // Last node of the linked list (null if empty)
     private int size;                                         // Number of elements in the linked list
 
     // Constructor initializes an empty linked list
     public LinkedList() {                                     // Default constructor
         this.head = null;                                     // Start with no head (empty list)
         this.tail = null;                                     // Start with no tail (empty list)
         this.size = 0;                                        // Start with size 0
     }
 
     // Returns the number of elements in the linked list
     public int size() {
        if (this.head == null){
            return 0;
        }                                     // Size accessor
         return this.size;                                     // Return current element count
     }
 
     // Clears the linked list
     public void clear() {                                     // Remove all elements
         this.head = null;                                     // Drop reference to head
         this.tail = null;                                     // Drop reference to tail
         this.size = 0;                                        // Reset size to 0
     }
 
     // Checks if the linked list is empty
     public boolean isEmpty() {                                // Emptiness check
         return size == 0;                                     // True if size equals 0
     }
 
     // Returns a string representation of the linked list
     public String toString() {                                // Convert list to a printable string
         if (this.head == null) {                              // If list is empty
             return "null";                                    // Return literal "null" per current design
         }
         StringBuilder sb = new StringBuilder();               // Builder to efficiently accumulate text
         Node current = this.head;                             // Start from head
         while (current != null) {                             // Traverse until end of list
             sb.append(current.item);                          // Append current node's item
             if (current.next != null) {                       // If not at the last node
                 sb.append(" --> ");                           // Append arrow separator
             }
             current = current.next;                           // Advance to next node
         }
         return sb.toString();                                 // Return the fully built string
     }
 
     // Checks if a specific element exists in the linked list
     public boolean contains(Object o) {                       // Membership check by equality
         Node current = this.head;                             // Begin traversal at head
         while (current != null) {                             // While nodes remain
             if (current.item.equals(o)) {                     // If this node matches target
                 return true;                                  // Report presence
             }
             current = current.next;                           // Otherwise advance to next
         }
         return false;                                         // Not found by end of list
     }
 
     // Adds an element at the end of the linked list
     public void push(T item) {                                // Stack-style push (front by design here)
         this.add(item);                                       // Delegate to add at head (current implementation choice)
     }
 
     // Adds an element at the end of the linked list
     public void offer(T item) {                               // Queue-style offer (enqueue at tail)
         this.addLast(item);                                   // Delegate to addLast (append)
     }
 
     // Returns the last element without removing it
     public T peek() {                                         // Stack-style peek (but returns head here)
         return this.head.item;                                // Return head's item (assumes non-null head)
     }
 
     // Removes and returns the first element of the list / at the top of the stack
     public T pop() {                                          // Stack-style pop
         return this.remove();                                 // Delegate to remove from head
     }
 
     // Removes and returns the first element of the list / at the top of the stack
     public T poll() {                                         // Queue-style poll
         return this.remove();                                 // Delegate to remove from head
     }
 
     // Retrieves the element at the specified index
     public T get(int index) {                                 // Random access by index (O(n))
         if (index < 0 || index >= this.size) {                // Validate index bounds
             return null;                                      // Out-of-bounds returns null per current design
         }
 
         Node current = this.head;                             // Start traversal at head
         for (int i = 0; i < index; i++) {                     // Advance index steps
             current = current.next;                           // Move to next node
         }   
         return current.getData();                             // Return data at target node
     }
 
     // Adds an element at the beginning of the list
     public void add(T item) {                                 // Prepend element to list
         Node newNode = new Node(item);                        // Create a new node with provided item
         newNode.setNext(this.head);                           // Point new node's next to current head
         this.head = newNode;                                  // Update head to new node
         if (this.size == 0) {                                 // If list was previously empty
             this.tail = newNode;                              // Tail must also point to new node
         }
         this.size++;                                          // Increment element count
     }
 
     // Removes and returns the first element of the list
     public T remove() {                                       // Remove from head
         if (this.head == null) {                              // If list is empty
             return null;                                      // Nothing to remove, return null
         }
 
         T removed = this.head.item;                           // Capture the item to return
         this.head = this.head.next;                           // Advance head to next node
         this.size--;                                          // Decrement size
         if (this.size == 0) {                                 // If list became empty
             this.tail = null;                                 // Clear tail as well
         }
         return removed;                                       // Return removed value
     }
 
     // Compares two linked lists for equality
     @Override
     public boolean equals(Object obj) {                       // Structural equality check
         if (this == obj) {                                    // If same reference
             return true;                                      // They are equal
         }
         if (obj == null || getClass() != obj.getClass()) {    // If null or different runtime class
             return false;                                     // They are not equal
         }
 
         LinkedList<?> other = (LinkedList<?>) obj;            // Cast to comparable LinkedList type
 
         if (this.size != other.size) {                        // If sizes differ
             return false;                                     // Not equal
         }
 
         LinkedList<T>.Node currentA = this.head;              // Start traversal at this list's head
         LinkedList<?>.Node currentB = other.head;             // Start traversal at other list's head
 
         while (currentA != null && currentB != null) {        // Traverse in lockstep
             if (!currentA.getData().equals(currentB.getData())) { // If any data mismatch
                 return false;                                  // Lists are not equal
             }
             currentA = currentA.next;                         // Advance this list pointer
             currentB = currentB.next;                         // Advance other list pointer
         }
 
         return true;                                          // All nodes matched
     }
 
     // Adds an element at a specified index
     public void add(int index, T item) {                      // Insert at position (0..size)
         if (index < 0 || index > this.size) {                 // Check valid bounds (inclusive end)
             throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size); // Signal invalid index
         }
 
         Node newNode = new Node(item);                        // Create new node to insert
 
         if (index == 0) {                                     // Special case: insert at head
             newNode.setNext(this.head);                       // New node points to current head
             this.head = newNode;                              // Head becomes new node
             if (this.size == 0) {                             // If list was empty
                 this.tail = newNode;                          // Tail also becomes new node
             }
         } else {                                              // General case: insert after some node
             Node current = this.head;                         // Start at head
             for (int i = 0; i < index - 1; i++) {             // Walk to node just before target index
                 current = current.next;                       // Advance pointer
             }
             newNode.setNext(current.next);                    // New node points to successor
             current.setNext(newNode);                         // Previous node points to new node
             if (index == this.size) {                         // If inserted at end
                 this.tail = newNode;                          // Update tail pointer
             }
         }
         this.size++;                                          // Increase size to reflect insertion
     }
 
     // Removes and returns the element at a specified index
     public T remove(int index) {                              // Remove at position (0..size-1)
         if (index < 0 || index >= this.size) {                // Validate bounds
             throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size); // Signal invalid index
         }
         if (index == 0) {                                     // Special case: remove head
             return this.remove();                             // Delegate to head removal
         }
         if (index == this.size - 1) {                         // Special case: remove tail
             return this.removeLast();                         // Delegate to tail removal
         }
 
         Node current = this.head;                             // Start at head
         for (int i = 0; i < index - 1; i++) {                 // Walk to node before the target
             current = current.next;                           // Advance pointer
         }
         T removed = current.next.getData();                   // Capture data from node to be removed
         current.next = current.next.next;                     // Bypass the removed node
         this.size--;                                          // Decrement size
         return removed;                                       // Return removed element
     }
 
     // Adds an element at the end of the list
     public void addLast(T item) {                             // Append to tail (enqueue)
         Node itemNode = new Node(item);                       // Create a new node with item
         if (this.head == null) {                              // If list is empty
             this.head = itemNode;                             // Head becomes the new node
             this.tail = itemNode;                             // Tail also becomes the new node
         } else {                                              // Otherwise append to existing tail
             this.tail.next = itemNode;                        // Old tail points to new node
             this.tail = itemNode;                             // Update tail to new node
         }
         size++;                                               // Increment element count
     }
 
     // Removes and returns the last element of the list
     public T removeLast() {                                   // Remove from tail (O(n))
         if (this.head == null) {                              // If list is empty
             throw new NoSuchElementException("The list is empty"); // Cannot remove from empty list
         }
         if (this.head == this.tail) {                         // If only one node exists
             T removed = this.head.getData();                  // Capture its data
             this.head = null;                                 // Clear head
             this.tail = null;                                 // Clear tail
             size = 0;                                         // Reset size
             return removed;                                   // Return removed value
         }
 
         Node current = this.head;                             // Start at head
         while (current.next != this.tail) {                   // Traverse to node just before tail
             current = current.next;                           // Advance pointer
         }
 
         T removed = this.tail.getData();                      // Capture tail's data
         this.tail = current;                                  // Move tail back to previous node
         this.tail.next = null;                                // New tail terminates list
         size--;                                               // Decrement size
         return removed;                                       // Return removed value
     }
 
     // Returns the last element without removing it
     public T getLast() {                                      // Tail accessor
         if (this.tail == null) {                              // If list is empty
             throw new NoSuchElementException("The list is empty"); // No tail to return
         }
         return this.tail.getData();                           // Return tail's data
     }
 }
 