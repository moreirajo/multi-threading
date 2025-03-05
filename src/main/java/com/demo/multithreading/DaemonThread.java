package com.demo.multithreading;

class myDaemonThread implements Runnable {

  @Override
  public void run() {
    while (true) {
      System.out.println("Daemon thread performing some task...");
      try {
        Thread.sleep(500);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}

/** Example of Daemon thread, whe program finish daemon thread also finish. */
public class DaemonThread {

  public static void main(String[] args) {
    Thread daemonThread = new Thread(new myDaemonThread());
    daemonThread.setDaemon(true); // Setting daemon thread to true

    System.out.println("Main thread starting...");
    daemonThread.start();

    try {
      Thread.sleep(5000); // allow daemon thread do run some time
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    System.out.println(
        "Main thread completed. Problem will shutdown regardless of daemon thread's status.");
  }
}
