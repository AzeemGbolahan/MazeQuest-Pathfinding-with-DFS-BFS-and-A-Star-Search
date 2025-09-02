/*
 * File Name:    HeapTests.java
 * Author:       Azeem Gbolahan
 * Last Modified: 05/04/2025
 * Description:  Test harness for the Heap<T> priority queue implementation. Runs a series of
 *               correctness and basic performance tests (offer/poll order, size tracking,
 *               randomized ordering, and rough n log n runtime sanity check).
 *
 * How to Run:   Compile with `javac Heap.java HeapTests.java` and run `java HeapTests`.
 */

 import java.util.ArrayList;            // ArrayList used to hold random test data
 import java.util.Collections;               // Random used to generate random integers
 import java.util.Comparator;           // Comparator used to instantiate Heap with ordering
 import java.util.Random;          // Collections utility for shuffling and sorting
 
 public class HeapTests {               // Test class containing static tests and a main method
 
     public static double heapTests() { // Runs all tests and returns a numeric score
                                        // (double to allow fractional/incremental scoring)
 
         double testScore = 1. ;        // Start at 1.0 to verify harness runs (baseline point)
 
         /**
          * Make a small Priority Queue
          */
         {                                                                     // Start local block scope for Test 1
             PriorityQueue<Integer> pq = new Heap<>((Comparator<Integer>)      // Create a min-heap of Integers
                 (Integer a, Integer b) -> a - b);                             // Comparator: natural ascending order
 
             for (int i = 0; i < 5; i++) {                                     // Insert 5 elements: 0..4
                 pq.offer(i);                                                  // Offer each i into the heap
             }
 
             if (pq.size() == 5) {                                             // Validate size is 5 after inserts
                 System.out.println( "Test 1 passed" );                        // Report success for Test 1
                 testScore ++;                                                 // Increment score
             }
         }                                                                     // End Test 1 block
         
         /**
          * Offer a few things in, poll a few things out
          */
         {                                                                     // Start local block scope for Test 2
             PriorityQueue<Integer> pq = new Heap<>((Comparator<Integer>)      // Create a min-heap
                 (Integer a, Integer b) -> a - b);                             // Ascending comparator
 
             for (int i = 0; i < 5; i++) {                                     // Insert 0..4
                 pq.offer(i);                                                  // Offer into heap
             }
 
             boolean testPassed = true;                                        // Assume pass until disproven
             for (int i = 0; i < 5; i++) {                                     // Poll all 5 items
                 if (pq.poll() != i) {                                         // Expect ascending order: 0,1,2,3,4
                     testPassed = false;                                       // Mark failure if any mismatch
                 }
             }
 
             if (testPassed) {                                                 // If all polls matched expectation
                 System.out.println( "Test 2 passed" );                        // Report success for Test 2
                 testScore ++;                                                 // Increment score
             }
         }                                                                     // End Test 2 block
 
         /**
          * Adding and removing a bunch of items (simple)
          */
         {                                                                     // Start local block scope for Test 3
             PriorityQueue<Integer> pq = new Heap<>((Comparator<Integer>)      // Create a min-heap
                 (Integer a, Integer b) -> a - b);                             // Ascending comparator
 
             for (int i = 0; i < 100; i++) {                                   // Insert 0..99
                 pq.offer(i);                                                  // Offer into heap
             }
 
             boolean testPassed = true;                                        // Assume pass
             for (int i = 0; i < 100; i++) {                                   // Poll 100 items
                 if (pq.poll() != i) {                                         // Expect exact ascending order
                     testPassed = false;                                       // Record failure on mismatch
                 }
             }
 
             if (testPassed) {                                                 // If all polled values correct
                 System.out.println( "Test 3 passed" );                        // Report success for Test 3
                 testScore ++;                                                 // Increment score
             }
         }                                                                     // End Test 3 block
 
         /**
          * Adding and removing a bunch of items (a bit more interesting)
          */
         {                                                                     // Start local block scope for Test 4
             PriorityQueue<Integer> pq = new Heap<>((Integer a, Integer b)     // Create a min-heap
                 -> a - b);                                                    // Ascending comparator via lambda
             Random rand = new Random();                                       // RNG for generating numbers
             ArrayList<Integer> nums = new ArrayList<>();                      // List to hold random values
 
             for (int i = 0; i < 100; i++) {                                   // Generate 100 random ints
                 nums.add(rand.nextInt(300));                                  // Range: [0, 300)
             }
 
             Collections.shuffle(nums);                                        // Shuffle to randomize insertion order
             for (int x : nums) {                                              // Insert every number
                 pq.offer(x);                                                  // Offer into heap
             }
             
             nums.sort((Integer a, Integer b) -> a - b);                       // Sort expected order ascending
             boolean testPassed = true;                                        // Assume pass
             for (int i = 0; i < 100; i++) {                                   // Poll 100 values
                 int p = pq.poll();                                            // Poll from heap
                 if ( p != nums.get(i)) {                                      // Compare against sorted baseline
                     testPassed = false;                                       // Mark failure if mismatch
                 }
             }
 
             if (testPassed) {                                                 // If all matches
                 System.out.println( "Test 4 passed" );                        // Report success for Test 4
                 testScore ++;                                                 // Increment score
             }
         }                                                                     // End Test 4 block
 
         /**
          * Making sure the runtime is roughly nlog n
          */
         {                                                                     // Start local block scope for Test 5
             PriorityQueue<Integer> pq = new Heap<>((Integer a, Integer b)     // Create a min-heap
                 -> a - b);                                                    // Ascending comparator
             Random rand = new Random();                                       // RNG for input values
 
             long startTime = System.currentTimeMillis();                      // Start wall clock
             for (int i = 0; i < 1000000; i++)                                 // Insert 1,000,000 values
                 pq.offer(rand.nextInt(Integer.MAX_VALUE));                    // Offer random int each time
             for (int i = 0; i < 1000000; i++)                                 // Remove 1,000,000 values
                 pq.poll();                                                    // Poll from heap
             long elapsedTimeMillis = System.currentTimeMillis() - startTime;  // Compute elapsed time in ms
 
             if ( elapsedTimeMillis < 5000 ) {                                 // Sanity threshold: < 5 seconds
                 System.out.println( "Test 5 passed" );                        // Report success for Test 5
                 testScore ++;                                                 // Increment score
             }
         }                                                                     // End Test 5 block
 
         return testScore ;                                                    // Return the cumulative score
     }
 
     public static void main(String[] args) {                                  // Program entry point
         System.out.println( heapTests() + "/6" );                              // Run tests and print "score/6"
     }
 }
 