package org.geekbang.time.commonmistakes.RateLimiter;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author xialh
 * @Date 2021/1/3 9:22 AM
 */
public class RateLimiterDemo {


    public static void main(String[] args) {

        //限流器流速：2个请求/秒
        RateLimiter limiter = RateLimiter.create(2.0);
        //执行任务的线程池
        ExecutorService es = Executors.newFixedThreadPool(1);
        //记录上一次执行时间
        final long prev = System.nanoTime();
        //测试执行20次
        for (int i=0; i<20; i++){
            //限流器限流,在向线程池提交任务之前，调用 acquire() 方法就能起到限流的作用。
            limiter.acquire();
            //提交任务异步执行
            es.execute(()->{
                long cur=System.nanoTime();
                //打印时间间隔：毫秒
                System.out.println((cur-prev)/1000_000);
//                prev = cur;
            });
        }

    }
}
