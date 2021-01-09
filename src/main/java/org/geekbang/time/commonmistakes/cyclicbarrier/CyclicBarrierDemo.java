package org.geekbang.time.commonmistakes.cyclicbarrier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * @Author xialh
 * @Date 2021/1/4 11:37 AM
 */
public class CyclicBarrierDemo {

    // 订单队列
    static Vector pos = new Vector();
    // 派送单队列
    static Vector dos = new Vector();

    static Executor executor = Executors.newFixedThreadPool(1);
    final static CyclicBarrier barrier = new CyclicBarrier(2, ()->{ executor.execute(()->check()); });

    public static void main(String[] args) {

        checkAll();
    }

    private static void check() {
        Collections. synchronizedList(new ArrayList());
        System.out.println(System.currentTimeMillis()/1000+"======="+Thread.currentThread().getName());

//        P p = pos.remove(0);
//        D d = dos.remove(0);
        // 执行对账操作
//        diff = check(p, d);
        // 差异写入差异库
//        save(diff);

        pos.remove(0);
        dos.remove(0);
    }


    static void checkAll(){

        // 循环查询订单库
        Thread T1 = new Thread(()->{
            while(true){
                // 查询订单库
                pos.add(getPOrders());
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                try {
                    barrier.await();   // 等待
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });


        T1.start();
        // 循环查询运单库
        Thread T2 = new Thread(()->{
            while(true){
                // 查询运单库
                dos.add(getDOrders());
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 等待
                try {
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
        T2.start();
    }

    private static Object getDOrders() {
        return  new Object();
    }

    private static Object getPOrders() {
        return  new Object();
    }
}
