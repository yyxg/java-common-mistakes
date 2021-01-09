package org.geekbang.time.commonmistakes.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author xialh
 * @Date 2021/1/9 4:34 PM
 */
public class ActorDemo2 {


    public static void main(String[] args) throws InterruptedException {
        //1、创建Actor系统
        ActorSystem actorSystem = ActorSystem.create("HelloSystem");

        //2、创建CounterActor
        ActorRef counterActor = actorSystem.actorOf(Props.create(CounterActor.class));

        //四个线程生产消息
        ExecutorService es = Executors.newFixedThreadPool(4);
        //生产4*100000个消息
        for (int i=0; i<4; i++) {
            es.execute(()->{
                for (int j=0; j<10; j++) {
                    //3、调用 counterActor的tell方法
                    counterActor.tell(1, ActorRef.noSender());
                }
            });
        }
        //关闭线程池
        es.shutdown();
        //等待CounterActor处理完所有消息
        Thread.sleep(1000);
        //打印结果
        counterActor.tell("", ActorRef.noSender());
        //关闭Actor系统
        actorSystem.shutdown();
    }
}


/**
 * 累加器
 */
class CounterActor extends UntypedActor {
    private int counter = 0;
    @Override
    public void onReceive(Object message){
        //如果接收到的消息是数字类型，执行累加操作，
        //否则打印counter的值
        if (message instanceof Number) {
            counter += ((Number) message).intValue();
        } else {
            System.out.println(counter);
        }
    }
}