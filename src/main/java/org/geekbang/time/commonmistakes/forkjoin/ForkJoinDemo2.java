package org.geekbang.time.commonmistakes.forkjoin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @Author xialh
 * @Date 2020/12/28 10:37 AM
 *統計單詞
 *
 */
public class ForkJoinDemo2 {


    public static void main(String[] args) {
        String[] fc = {"hello    world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};
        //创建ForkJoin线程池
        ForkJoinPool fjp = new ForkJoinPool(3);
        //创建任务
        MR mr = new MR(fc, 0, fc.length);
        //启动任务
        Map<String, Long> result = fjp.invoke(mr);
        //输出结果
        result.forEach((k, v)-> System.out.println(k+":"+v));
    }

    /**
     * MR模拟类
     *  RecursiveTask<V> 这里是一个泛型，如果想让compute方法返回什么类型
     *  这里就定义什么类型
     */
    static class MR extends RecursiveTask<Map<String, Long>> {
        private String[] fc;
        private int start, end;
        //构造函数
        MR(String[] fc, int fr, int to){
            this.fc = fc;
            this.start = fr;
            this.end = to;
        }

        /**
         *  fork() 方法会异步地执行一个子任务，而 join() 方法则会阻塞当前线程来等待子任务的执行结果
         * fork是执行任务，join是任务返回后获取结果，只有先执行任务才能从任务中获取结果
         * 这里mr2为什么没有小fork后join 而是直接调用compute方法？
         * @return
         */
        @Override protected
        Map<String, Long> compute(){
            if (end - start == 1) {
                return calc(fc[start]);
            } else {
                int mid = (start+end)/2;
                //分成mr1和mr2兩個子任務
                MR mr1 = new MR(fc, start, mid);
                //任务一执行
                mr1.fork();
                MR mr2 = new MR(fc, mid, end);
                //计算子任务，并返回合并的结果
                //mr1.join()等待子任务执行完，并得到其结果
                return merge(mr2.compute(), mr1.join());
            }
        }
        //合并结果
        private Map<String, Long> merge(
                Map<String, Long> r1,
                Map<String, Long> r2) {
            Map<String, Long> result = new HashMap<>();
            result.putAll(r1);
            //合并结果
            r2.forEach((k, v) -> {
                Long c = result.get(k);
                if (c != null) {
                    result.put(k, c + v);
                } else {
                    result.put(k, v);
                }

            });
            return result;
        }
        //统计单词数量
        private Map<String, Long> calc(String line) {
            // ex line = "hello world"

            Map<String, Long> result = new HashMap<>();
            //分割单词 \\s表示 空格,回车,换行等空白符,+号表示一个或多个的意思
            String[] words = line.split("\\s+");
            //统计单词数量
            for (String w : words) {
                Long v = result.get(w);
                if (v != null) {
                    result.put(w, v + 1);
                } else {
                    result.put(w, 1L);
                }
            }
            return result;
        }
    }
}
