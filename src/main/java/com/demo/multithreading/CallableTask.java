package com.demo.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

class MyMath implements Callable<Double> {

  @Override
  public Double call() throws Exception {
    return Math.random();
  }
}

/** Example of a task that returns a result asynchronously. */
public class CallableTask {

  public static void main(String[] args) {

    ExecutorService executor = Executors.newFixedThreadPool(2);

    Future<Double> number1 = executor.submit(new MyMath());
    Future<Double> number2 = executor.submit(new MyMath());

    while (!number1.isDone() && !number2.isDone()) {}

    Future<Double> result = executor.submit(() -> number1.get() + number2.get());

    try {
      System.out.println("The sum of two random numbers is: " + result.get(5, TimeUnit.SECONDS));
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } catch (ExecutionException e) {
      throw new RuntimeException(e);
    } catch (TimeoutException e) {
      throw new RuntimeException(e);
    }

    executor.shutdown();
  }
}
