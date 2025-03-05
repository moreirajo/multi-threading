package com.demo.multithreading;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/** Below utility searches the given pattern in the file. */
class PatternFinder {

  /**
   * Looks for the given pattern in the file, and returns the list of line numbers in which the
   * pattern is found.
   */
  public List<Integer> find(File file, String pattern) {

    List<Integer> lineNumbers = new ArrayList<Integer>();

    // Open the file for reading.
    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      int lineNo = 1;
      String line;

      // for each line in the file.
      while ((line = br.readLine()) != null) {

        if (line.contains(pattern)) {
          // capture the lineNo where the pattern is found.
          lineNumbers.add(lineNo);
        }

        lineNo++;
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    // Just introduced the delay for demo.
    try {
      Thread.sleep(1000);
    } catch (Exception e) {
    }

    return lineNumbers;
  }
}

/**
 * Reads all files in a directory and searches for a given pattern in them.
 *
 * <ul>
 *   <li>Serial approach, reads one file at a time, each file takes 1s to read, total processing
 *       time is 3s
 *   <li>Parallel approach, reads 3 files at the same time, each file takes 1s to read, total
 *       processing time is 1s
 * </ul>
 */
public class PatternSearchFolder {

  public static void main(String[] args) throws Exception {

    serialApproach();
    parallelApproach();
  }

  private static void serialApproach() {
    // pattern to search
    String pattern = "public";

    // Directory or folder to search
    File dir = new File("./src/main/resources/sample");

    // list all the files present in the folder.
    File[] files = dir.listFiles();

    PatternFinder finder = new PatternFinder();

    long startTime = System.currentTimeMillis();

    // for each file in the list of files

    for (File file : files) {

      List<Integer> lineNumbers = finder.find(file, pattern);

      if (!lineNumbers.isEmpty()) {
        System.out.println(
            pattern + "; found at " + lineNumbers + " in the file - " + file.getName());
      }
    }

    System.out.println(
        "Serial Approach Time taken for search - " + (System.currentTimeMillis() - startTime));
  }

  private static void parallelApproach() throws Exception {
    // pattern to search
    String pattern = "public";

    // Directory or folder to search
    File dir = new File("./src/main/resources/sample");

    // list all the files present in the folder.
    File[] files = dir.listFiles();

    PatternFinder finder = new PatternFinder();

    // Fixed thread pool of size 3.
    ExecutorService executor = Executors.newFixedThreadPool(3);

    // Map to store the Future object against each
    // file search request, later once the result is obtained
    // the Future object will be
    // replaced with the search result.

    Map<String, Object> resultMap = new HashMap<String, Object>();

    long startTime = System.currentTimeMillis();

    // for each file in the list of files

    for (File file : files) {

      Future<List<Integer>> future = executor.submit(() -> finder.find(file, pattern));

      // Save the future object in the map for
      // fetching the result.
      resultMap.put(file.getName(), future);
    }

    // Wait for the requests to complete.
    waitForAll(resultMap);

    // Display the result.
    for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
      System.out.println(
          pattern + " found at - " + entry.getValue() + " in file " + entry.getKey());
    }

    System.out.println(
        "Parallel Time taken for search - " + (System.currentTimeMillis() - startTime));

    executor.shutdown();
  }

  private static void waitForAll(Map<String, Object> resultMap) throws Exception {

    Set<String> keys = resultMap.keySet();

    for (String key : keys) {
      Future<List<Integer>> future = (Future<List<Integer>>) resultMap.get(key);

      while (!future.isDone()) {

        // Passing the CPU to other
        // threads so that they can
        // complete the operation.
        // With out this we are simply
        // keeping the CPU in loop and
        // wasting its time.

        Thread.yield();
      }

      // Replace the future object with the obtained result.
      resultMap.put(key, future.get());
    }
  }
}
