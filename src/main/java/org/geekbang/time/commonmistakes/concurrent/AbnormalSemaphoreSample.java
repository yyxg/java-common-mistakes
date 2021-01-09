package org.geekbang.time.commonmistakes.concurrent;

import java.util.concurrent.Semaphore;
public class AbnormalSemaphoreSample {
  public static void main(String[] args) throws InterruptedException {

      //1、初始许可为0，那么10个工作线程尝试获取许可都获取不到而等待
      Semaphore semaphore = new Semaphore(0);
      for (int i = 0; i < 10; i++) {
          Thread t = new Thread(new MyWorker(semaphore));
          t.start();
      }
      System.out.println("Action...GO!");
      //2、一次性释放5个，那就有5个线程可以获取许可执行  0
      System.out.println("目前许可个数："+ semaphore.availablePermits());
      semaphore.release(5);
      // 释放后许可个数5个
      System.out.println("释放后许可个数："+ semaphore.availablePermits());
      System.out.println("Wait for permits off");
      // 如果还有许可就休眠100毫秒

      while (semaphore.availablePermits()!=0) {
          Thread.sleep(1000L);
      }

      System.out.println("Action...GO again!");
      //再次释放5个
      semaphore.release(5);
  }
}
class MyWorker implements Runnable {
  private Semaphore semaphore;
  public MyWorker(Semaphore semaphore) {
      this.semaphore = semaphore;
  }
  @Override
  public void run() {
      try {
          semaphore.acquire();
          System.out.println("Executed!");
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
}
