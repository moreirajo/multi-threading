package com.demo.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

class LoginQueueUsingSemaphore {

  private Semaphore semaphore;

  public LoginQueueUsingSemaphore(int slotLimit) {
    semaphore = new Semaphore(slotLimit);
  }

  boolean tryLogin() {
    return semaphore.tryAcquire();
  }

  void logout() {
    semaphore.release();
  }

  int availableSlots() {
    return semaphore.availablePermits();
  }
}

/**
 * Demonstrates the usage of a Semaphore to control concurrent access to a login queue. This example
 * creates a login queue with a limit of 2 concurrent users and attempts to log in 3 users
 * simultaneously. After a delay, one user is logged out, allowing another user to log in.
 */
public class SemaphoreExample {

  public static void main(String[] args) {

    LoginQueueUsingSemaphore loginQueue = new LoginQueueUsingSemaphore(2);

    ExecutorService executor = Executors.newFixedThreadPool(3);

    executor.submit(
        () -> {
          if (loginQueue.tryLogin()) System.out.println("User 1 logged in");
        });
    executor.submit(
        () -> {
          if (loginQueue.tryLogin()) System.out.println("User 2 logged in");
        });
    executor.submit(
        () -> {
          if (loginQueue.tryLogin()) System.out.println("User 3 logged in");
        });

    System.out.println("Wait 3 seconds and logout one user");
    try {
      Thread.sleep(3000);
      loginQueue.logout();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    executor.submit(
        () -> {
          if (loginQueue.tryLogin()) System.out.println("User 3 logged in");
        });

    executor.shutdown();
  }
}
