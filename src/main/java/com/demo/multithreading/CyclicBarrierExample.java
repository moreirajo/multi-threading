package com.demo.multithreading;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/** A and B are two participants in a game where should start only when both are ready. */
public class CyclicBarrierExample {

  public static void main(String[] args) {

    CyclicBarrier barrier =
        new CyclicBarrier(2, () -> System.out.println("Both A and B are ready!"));

    Thread a =
        new Thread(
            () -> {
              try {
                barrier.await();
              } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
              }
              System.out.println("A begins...");
            });
    Thread b =
        new Thread(
            () -> {
              try {
                barrier.await();
              } catch (InterruptedException | BrokenBarrierException e) {
                throw new RuntimeException(e);
              }
              System.out.println("B begins...");
            });

    a.start();

    try {
      Thread.sleep(5000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    b.start();
  }
}
