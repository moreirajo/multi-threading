package com.demo.multithreading;

// Changed from MessageQueue to BlockingQueue.

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class ProducerThread2 extends Thread {
  BlockingQueue<String> queue;

  public ProducerThread2(BlockingQueue<String> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      String msg = "Hello-" + i;
      try {
        queue.put(msg);
        System.out.println("Produced - " + msg);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

class ConsumerThread2 extends Thread {
  BlockingQueue<String> queue;

  public ConsumerThread2(BlockingQueue<String> queue) {
    this.queue = queue;
  }

  @Override
  public void run() {
    for (int i = 1; i <= 10; i++) {
      String message = null;
      try {
        message = queue.take();
        System.out.println("Consumed - " + message);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

public class BlockingQueueExample {
  public static void main(String[] args) throws InterruptedException {
    BlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
    new ProducerThread2(queue).start();
    new ConsumerThread2(queue).start();
  }
}
