package com.demo.multithreading;

import java.util.Comparator;

class Student {
  String name;
  int rank;

  public Student(String name, int rank) {
    this.name = name;
    this.rank = rank;
  }

  public int getRank() {
    return this.rank;
  }

  public String toString() {
    return String.format("name : %s, rank : %d", name, rank);
  }
}

// Compares the Student objects based on the rank field value.
class StudentComparator implements Comparator<Student> {
  @Override
  public int compare(Student o1, Student o2) {
    return o1.getRank() - o2.getRank();
  }
}

public class PriorityBlockingQueue {

  public static void main(String[] args) {
    java.util.concurrent.PriorityBlockingQueue<Integer> queue =
        new java.util.concurrent.PriorityBlockingQueue<>();
    queue.add(10);
    queue.add(2);
    queue.add(5);

    System.out.println(queue.poll());
    System.out.println(queue.poll());
    System.out.println(queue.poll());

    java.util.concurrent.PriorityBlockingQueue<Student> queue1 =
        new java.util.concurrent.PriorityBlockingQueue<>(5, new StudentComparator());

    queue1.add(new Student("a", 12));
    queue1.add(new Student("b", 1));
    queue1.add(new Student("c", 4));

    System.out.println(queue1.poll());
    System.out.println(queue1.poll());
    System.out.println(queue1.poll());
  }
}
