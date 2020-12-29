package org.geekbang.time.commonmistakes.java8;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.time.LocalDateTime;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

@Slf4j
public class ParallelTest {

    /**
     * 并行流不确保执行顺序，并且因为每次处理耗时 1 秒，
     * 所以可以看到在 4 核机器上，数组是按照 4 个一组 1 秒输出一次：
     */
    @Test
    public void parallel() {
        IntStream.rangeClosed(1, 100).parallel().forEach(i -> {
            System.out.println(LocalDateTime.now() + " : " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        });
    }

    @Test
    public void allMethods() throws InterruptedException, ExecutionException {
        int taskCount = 10000;
        int threadCount = 20;
        StopWatch stopWatch = new StopWatch();

        stopWatch.start("thread");
        Assert.assertEquals(taskCount, thread(taskCount, threadCount));
        stopWatch.stop();

        stopWatch.start("threadPool");
        Assert.assertEquals(taskCount, threadPool(taskCount, threadCount));
        stopWatch.stop();

        //试试把这段放到forkjoin下面？
        stopWatch.start("stream");
        Assert.assertEquals(taskCount, stream(taskCount, threadCount));
        stopWatch.stop();

        stopWatch.start("forkJoin");
        Assert.assertEquals(taskCount, forkJoin(taskCount, threadCount));
        stopWatch.stop();

        stopWatch.start("completableFuture");
        Assert.assertEquals(taskCount, completableFuture(taskCount, threadCount));
        stopWatch.stop();

        log.info(stopWatch.prettyPrint());
    }

    /**
     * 简单模拟单个任务单线程执行需要 10 毫秒
     * @param atomicInteger
     */
    private void increment(AtomicInteger atomicInteger) {
        atomicInteger.incrementAndGet();
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 第一种方式
     * 使用线程。直接把任务按照线程数均匀分割，
     * 分配到不同的线程执行，使用 CountDownLatch 来阻塞主线程，
     * 直到所有线程都完成操作。这种方式，需要我们自己分割任务：
     * @param taskCount
     * @param threadCount
     * @return
     * @throws InterruptedException
     */
    private int thread(int taskCount, int threadCount) throws InterruptedException {
        //总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        //使用CountDownLatch来等待所有线程执行完成
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        //使用IntStream把数字直接转为Thread
        IntStream.rangeClosed(1, threadCount).mapToObj(i -> new Thread(() -> {
            //手动把taskCount分成taskCount份，每一份有一个线程执行
            IntStream.rangeClosed(1, taskCount / threadCount).forEach(j -> increment(atomicInteger));
            //每一个线程处理完成自己那部分数据之后，countDown一次
            countDownLatch.countDown();
        })).forEach(Thread::start);
        //等到所有线程执行完成
        countDownLatch.await();
        //查询计数器当前值
        return atomicInteger.get();
    }

    /**
     * 第二种方式
     * 使用 Executors.newFixedThreadPool 来获得固定线程数的线程池
     * 使用 execute 提交所有任务到线程池执行，最后关闭线程池等待所有任务执行完成：
     * @param taskCount
     * @param threadCount
     * @return
     * @throws InterruptedException
     */
    private int threadPool(int taskCount, int threadCount) throws InterruptedException {
        //总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        //初始化一个线程数量=threadCount的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        //所有任务直接提交到线程池处理
        IntStream.rangeClosed(1, taskCount).forEach(i -> executorService.execute(() -> increment(atomicInteger)));
        //提交关闭线程池申请，等待之前所有任务执行完成
        executorService.shutdown();
        //查询计数器当前值
        executorService.awaitTermination(1, TimeUnit.HOURS);
        return atomicInteger.get();
    }

    /**
     * 第三种方式
     * 使用 ForkJoinPool 而不是普通线程池执行任务。
     * ForkJoinPool 和传统的 ThreadPoolExecutor 区别在于:
     * 前者对于 n 并行度有 n 个独立队列，后者是共享队列。如果有大量执行耗时比较短的任务，
     * ThreadPoolExecutor 的单队列就可能会成为瓶颈。
     * 这时，使用 ForkJoinPool 性能会更好。
     * 因此，ForkJoinPool 更适合大任务分割成许多小任务并行执行的场景，
     * 而 ThreadPoolExecutor 适合许多独立任务并发执行的场景。
     * @param taskCount
     * @param threadCount
     * @return
     * @throws InterruptedException
     */
    private int forkJoin(int taskCount, int threadCount) throws InterruptedException {
        //总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        //自定义一个并行度=threadCount的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        //所有任务直接提交到线程池处理
        forkJoinPool.execute(() -> IntStream.rangeClosed(1, taskCount).parallel().forEach(i -> increment(atomicInteger)));
        //提交关闭线程池申请，等待之前所有任务执行完成
        forkJoinPool.shutdown();
        forkJoinPool.awaitTermination(1, TimeUnit.HOURS);
        //查询计数器当前值
        return atomicInteger.get();
    }

    /**
     * 第四种方式
     * 直接使用并行流，并行流使
     * 公共的 ForkJoinPool 默认的并行度是 CPU 核心数 -1，
     * 原因是对于 CPU 绑定的任务分配超过 CPU 个数的线程没有意义。
     * 由于并行流还会使用主线程执行任务，也会占用一个 CPU 核心，
     * 所以公共 ForkJoinPool 的并行度即使 -1 也能用满所有 CPU 核心。
     * @param taskCount
     * @param threadCount
     * @return
     */
    private int stream(int taskCount, int threadCount) {
        //设置公共ForkJoinPool的并行度
        //我们通过配置强制指定（增大）了并行数，但因为使用的是公共 ForkJoinPool，所以可能会存在干扰，
        System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", String.valueOf(threadCount));
        //总操作次数计数器
        AtomicInteger atomicInteger = new AtomicInteger();
        //由于我们设置了公共ForkJoinPool的并行度，直接使用parallel提交任务即可
        IntStream.rangeClosed(1, taskCount).parallel().forEach(i -> increment(atomicInteger));
        //查询计数器当前值
        return atomicInteger.get();
    }

    /**
     * 第五种方式
     * 使用 CompletableFuture 来实现。
     * CompletableFuture.runAsync 方法可以指定一个线程池，
     * @param taskCount
     * @param threadCount
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private int completableFuture(int taskCount, int threadCount) throws InterruptedException, ExecutionException {
        AtomicInteger atomicInteger = new AtomicInteger();
        //自定义一个并行度=threadCount的ForkJoinPool
        ForkJoinPool forkJoinPool = new ForkJoinPool(threadCount);
        //使用CompletableFuture.runAsync通过指定线程池异步执行任务
        CompletableFuture.runAsync(() -> IntStream.rangeClosed(1, taskCount).parallel()
                .forEach(i -> increment(atomicInteger)), forkJoinPool).get();
        return atomicInteger.get();
    }
}
