package org.geekbang.time.commonmistakes.actor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;

/**
 * @Author xialh
 * @Date 2021/1/9 4:23 PM
 */
public class ActorDemo {



    public static void main(String[] args) {
        //创建Actor系统
        ActorSystem system = ActorSystem.create("HelloSystem");
        //创建HelloActor
        ActorRef helloActor = system.actorOf(Props.create(HelloActor.class));
        //发送消息给HelloActor
        helloActor.tell("Actor", ActorRef.noSender());

        system.shutdown();
    }
}

/**
 *该Actor当收到消息message后，会打印Hello message
 */
class HelloActor extends UntypedActor {
    @Override
    public void onReceive(Object message) {
        System.out.println("Hello " + message);
    }
}



