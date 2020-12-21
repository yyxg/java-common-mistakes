package org.geekbang.time.commonmistakes.java8;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
@Slf4j
public class Product {
    private Long id;
    private String name;
    private Double price;


    public static List<Product> getData() {
        return Arrays.asList(
                new Product(1L, "苹果", 1.0),
                new Product(2L, "桔子", 2.0),
                new Product(3L, "香蕉", 3.0),
                new Product(4L, "芒果", 4.0),
                new Product(5L, "西瓜", 5.0),
                new Product(6L, "葡萄", 6.0),
                new Product(7L, "桃子", 7.0),
                new Product(8L, "椰子", 8.0),
                new Product(9L, "菠萝", 9.0),
                new Product(10L, "石榴", 10.0));
    }

    public static void main(String[] args) throws InterruptedException {
        Thread currentThread = Thread.currentThread();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
        System.out.println("主线程被join前"+currentThread.getState());
//        thread.join();
        currentThread.wait();
        System.out.println("主线程被join后"+currentThread.getState());
        currentThread.interrupt();
        System.out.println(currentThread.getState());



//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                log.info("{} begin run",Thread.currentThread().getName());
//                try {
//                    log.info("子线程开始沉睡 30 s");
//                    Thread.sleep(3000L);
//                } catch (InterruptedException e) {
//                    log.info("子线程被打断");
//                    e.printStackTrace();
//                }
//                log.info("{} end run",Thread.currentThread().getName());
//            }
//        });
//        // 开一个子线程去执行
//        thread.start();
//        Thread.sleep(1000L);
//        log.info("主线程等待 1s 后，发现子线程还没有运行成功，打断子线程");
//        thread.interrupt();
//        System.out.println("子线程的状态是"+thread.getState());
    }
}
