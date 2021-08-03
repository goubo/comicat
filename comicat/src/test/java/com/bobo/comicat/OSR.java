package com.bobo.comicat;

/**
 * @author h_vk
 * @since 2021/7/16
 */
public class OSR {
  static long counter;

  public static void main(String[] args) throws InterruptedException {
    Thread.sleep(10*1000);
    System.out.println("Main start");
    startBusinessThread();
    startProblemThread();
    // 等待线程启动执行
    Thread.sleep(500);
    // 执行 GC
    System.gc();
    System.out.println("Main end");
  }

  public static void startBusinessThread() {
    new Thread(
      () -> {
        System.out.println("业务线程-1 start");
        for (; ; ) {
          System.out.println("执行业务1");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      })
      .start();

    new Thread(
      () -> {
        System.out.println("业务线程-2 start");
        for (; ; ) {
          System.out.println("执行业务2");
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      })
      .start();
  }

  public static void startProblemThread() {
    new Thread(new MyRun()).start();
  }

  public static class MyRun implements Runnable {
    @Override
    public void run() {
      System.out.println("Problem start");
      for (int i = 0; i < 100000000; i++) {
        for (int j = 0; j < 1000; j++) {
          counter += i % 33;
          counter += i % 333;
        }
      }
      System.out.println("Problem end");
    }
  }
}
