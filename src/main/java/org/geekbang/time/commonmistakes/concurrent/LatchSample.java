package org.geekbang.time.commonmistakes.concurrent;

import java.util.concurrent.CountDownLatch;

/**
 * @author xialihui
 *
 * 计数器为6，第一批5个线程中每个线程执行后countDown减去一个，总计减了5个还有1个
 * 所以第二批5个线程只能在await等待。在第一批执行完后 再countDown一次，等待的第二批线程被
 * 唤醒，继而执行
 */
public class LatchSample {
  public static void main(String[] args) throws InterruptedException {
      CountDownLatch latch = new CountDownLatch(6);
           for (int i = 0; i < 5; i++) {
                Thread t = new Thread(new FirstBatchWorker(latch));
                t.start();
      }
      for (int i = 0; i < 5; i++) {
              Thread t = new Thread(new SecondBatchWorker(latch));
              t.start();
      }
      // 注意这里也是演示目的的逻辑，并不是推荐的协调方式
      while ( latch.getCount() != 1 ){
              Thread.sleep(100L);
      }
      System.out.println("Wait for first batch finish");
      latch.countDown();
  }
}
class FirstBatchWorker implements Runnable {
  private CountDownLatch latch;
  public FirstBatchWorker(CountDownLatch latch) {
      this.latch = latch;
  }
  @Override
  public void run() {
          System.out.println("First batch executed!");
          latch.countDown();
  }
}
class SecondBatchWorker implements Runnable {
  private CountDownLatch latch;
  public SecondBatchWorker(CountDownLatch latch) {
      this.latch = latch;
  }
  @Override
  public void run() {
      try {
          latch.await();
          System.out.println("Second batch executed!");
      } catch (InterruptedException e) {
          e.printStackTrace();
      }
  }
}
