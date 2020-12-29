package org.geekbang.time.commonmistakes.java8.CompletionService;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author xialh
 * @Date 2020/12/28 8:24 AM
 */
public class CompletionServiceDemo {

    static AtomicInteger atomicInteger = new AtomicInteger();


    public static void main(String[] args) {



        ExecutorService executor =   Executors.newFixedThreadPool(3);
        // 创建CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);

        // 异步向电商S1询价
        cs.submit(()->getPriceByS1());
        // 异步向电商S2询价
        cs.submit(()->getPriceByS2());
        // 异步向电商S3询价
        cs.submit(()->getPriceByS3());
        // 将询价结果异步保存到数据库
        for (int i=0; i<3; i++) {
            Integer r = null;
            try {
                r = cs.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            Integer finalR = r;
            executor.execute(()->save(finalR));
        }

        System.out.println(atomicInteger);
    }

    private static Integer save(Integer i) {
        return atomicInteger.addAndGet(i);
    }
    private static Integer getPriceByS3() {
        return 3;
    }
    private static Integer getPriceByS2() {
        return 2;
    }

    public static Integer getPriceByS1() {
        return 1;
    }
}
