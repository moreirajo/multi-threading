package com.demo.multithreading;

class Writer1 extends Thread {

  Object book;
  Object pen;

  public Writer1(Object book, Object pen) {
    this.book = book;
    this.pen = pen;
  }

  @Override
  public void run() {

    synchronized (book) {
      try {
        Thread.sleep(10);
      } catch (Exception e) {
      }
      synchronized (pen) {
        System.out.println("Writer1 writing");
      }
    }
  }
}

class Writer2 extends Thread {

  Object book;
  Object pen;

  public Writer2(Object book, Object pen) {
    this.book = book;
    this.pen = pen;
  }

  @Override
  public void run() {

    synchronized (pen) {
      try {
        Thread.sleep(10);
      } catch (Exception e) {
      }
      synchronized (book) {
        System.out.println("Writer2 writing");
      }
    }
  }
}

public class Deadlock {

  public static void main(String[] args) {
    Object book = new Object();
    Object pen = new Object();

    new Writer1(book, pen).start();
    new Writer2(book, pen).start();

    System.out.println(
        "Main is done but program still runs waiting for Writer1 and Writer2 to finish. They are in deadlock. To fix that we need to do the lock operations in the same sequence.");
  }
}
