package com.demo.multithreading;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class UserMigrateTask implements Runnable {

  @Override
  public void run() {
    int recordCount = 220;
    int batchSize = 100;
    int nPages = (int) Math.ceil((double) recordCount / batchSize);

    ExecutorService executor = Executors.newFixedThreadPool(2);
    CountDownLatch latch = new CountDownLatch(nPages);

    for (int i = 1; i <= nPages; i++) {
      final int pageNo = i;
      executor.submit(
          () -> {
            System.out.println("Migrating page - " + pageNo);
            latch.countDown();
          });
    }

    executor.shutdown();
    boolean success = false;

    try {
      success = latch.await(2, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    if (success) {
      System.out.println("User migration completed successfully.");
    } else {
      System.out.println("User migration failed after 2 seconds. Please try again later.");
    }
  }
}

/**
 * This class demonstrates the use of CountDownLatch for coordinating multiple threads in a user
 * migration task. It creates and starts a new thread that executes the UserMigrateTask, which
 * handles the migration of user records in batches using a thread pool and CountDownLatch for
 * synchronization.
 */
public class CountDownLatchExample {
  public static void main(String[] args) {
    new Thread(new UserMigrateTask()).start();
  }
}
