package org.geekbang.time.commonmistakes.ThreadLocalDemo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * @Author xialh
 * @Date 2020/12/30 9:52 AM
 * 线程安全的SimpleDateFormat
 *
 * simpledateformat会打印出一样名称的对象，，但其实是simpledateformat对象的toString()方法搞得鬼，
 * 该类是继承object类的tostring方法，如下有个hashcode()方法，
 * 但该类重写了hashcode方法，pattern.hashcode(),pattern就是我们的yyyy-MM-dd,这个是一直保持不变的
 */
public  class SafeDateFormatDemo {



    public static void main(String[] args) throws Exception{
        System.out.println(SafeDateFormat.get());
        System.out.println(Thread.currentThread().getName());
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
                System.out.println(SafeDateFormat.get());
            }
        }).start();

    }

    static class SafeDateFormat{
        static final ThreadLocal<SimpleDateFormat> sdf =
                ThreadLocal.withInitial(()->new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        static SimpleDateFormat get(){
            return sdf.get();
        }
    }
}





