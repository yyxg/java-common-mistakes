package org.geekbang.time.commonmistakes.java8.completablefuture;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * https://time.geekbang.org/column/article/91292
 * @Author xialh
 * @Date 2020/12/26 10:59 AM
 */
public class FutureDemo {


    static class Result{

        List<String> list = new ArrayList<String>(2);

        String message;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public static void main(String[] args) {

        Future future;



        ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("dem-%d").build();
        ExecutorService executorService =  new ThreadPoolExecutor(4, 4,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),threadFactory);


        Runnable task = new Runnable() {
            @Override
            public void run() {

                System.out.println("I am running !");
            }
        };


        Future<?> future1 = executorService.submit(task);


        Callable callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return "hello";
            }
        };

        Future<Object> future2 = executorService.submit(callable);

        try {
            System.out.println(future2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Result result = new FutureDemo.Result();
        Future<Object> future3 = executorService.submit(task, result);
        try {
            //返回值就是传入submit的result
             result = (Result)future3.get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        // 创建Result对象r
        Result r = new Result();

        String message = "hello";
        r.setMessage(message);
        // 提交任务
        Future<Result> future4 =
                executorService.submit(new Task(r), r);
        Result fr = null;
        try {
            fr = future4.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // 下面等式成立
        System.out.println(fr == r);;





        // 创建FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(()-> 1+2);
        // 创建线程池
        ExecutorService es = Executors.newCachedThreadPool();
        // 提交FutureTask
        //由于实现了 Runnable 接口，所以可以将 FutureTask 对象作为任务提交给 ThreadPoolExecutor 去执行，
        //也可以直接被 Thread 执行
        Future<?> future5 = es.submit(futureTask);
        // 获取计算结果
        try {

            System.out.println("futureTask result 1: "+future5.get());
            //又因为实现了 Future 接口，所以也能用来获得任务的执行结果
            Integer re = futureTask.get();
            System.out.println("futureTask result 2: "+re);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }



        // 创建并启动线程
        Thread T1 = new Thread(futureTask);
        T1.start();

        try {
            // 获取计算结果
            Integer re2 = futureTask.get();
            System.out.println("futureTask result 3: "+re2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    static class Task implements Runnable{

        Result r;

        public Task(Result r) {
            this.r = r;
        }

        @Override
        public void run() {
            //可以操作result

            r.getMessage();
        }
    }
}
