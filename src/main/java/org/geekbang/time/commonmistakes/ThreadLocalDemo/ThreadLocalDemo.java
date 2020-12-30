package org.geekbang.time.commonmistakes.ThreadLocalDemo;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author xialh
 * @Date 2020/12/30 9:49 AM
 */

public class ThreadLocalDemo {

    static final AtomicLong nextId = new AtomicLong(0);
    /**
     * 定义ThreadLocal变量
     */
    static final ThreadLocal<Long> tl = ThreadLocal.withInitial(() -> nextId.getAndIncrement());

    /**
     * 此方法会为每个线程分配一个唯一的Id
     *
     * @return
     */
    static long get() {
        return tl.get();
    }


    public static void main(String[] args) {

        Thread thread = new Thread(){

            @Override
            public void run() {
                System.out.println(ThreadLocalDemo.get());

            }
        };
        thread.start();
    }
}