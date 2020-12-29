package org.geekbang.time.commonmistakes.java8.completablefuture;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import static org.geekbang.time.commonmistakes.java8.completablefuture.CompletableFutureDemo.sleep;

/**
 * https://time.geekbang.org/column/article/91569
 * @Author xialh
 * @Date 2020/12/27 10:27 AM
 */
public class CompletableFutureDemo2 {


    public static void main(String[] args) {


        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(()->{
                    int t = getRandom(5, 10);
                    sleep(t, TimeUnit.SECONDS);
                    return String.valueOf(t);
                });

        try {
            System.out.println("f1 result "+f1.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(()->{
                    int t = getRandom(5, 10);
                    sleep(t, TimeUnit.SECONDS);
                    return String.valueOf(t);
                });

        try {
            System.out.println("f2 result "+f2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        CompletableFuture<String> f3 =
                f1.applyToEither(f2,s -> s);

        System.out.println(f3.join());

    }

    private static int getRandom(int i, int i1) {
        return ThreadLocalRandom.current().nextInt(5);
    }


}
