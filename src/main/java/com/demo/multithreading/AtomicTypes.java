package com.demo.multithreading;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

class SharedService {

  static AtomicBoolean initialized = new AtomicBoolean(false);

  public void init() {

    if (!initialized.compareAndSet(false, true)) {
      System.out.println("already initialized");
      return;
    }

    Thread.yield();
    System.out.println("initializing...");
  }

  public void service() {
    init();
    System.out.println("perform some task");
  }
}

/**
 * Demonstrates the use of atomic types for thread-safe operations. This class shows how
 * AtomicBoolean can be used to ensure a service is initialized only once across multiple threads.
 */
public class AtomicTypes {

  public static void main(String[] args) {

    SharedService sharedService = new SharedService();

    ExecutorService executor = Executors.newFixedThreadPool(3);

    for (int i = 1; i <= 3; i++) {
      executor.submit(() -> sharedService.service());
    }

    executor.shutdown();
  }
}
