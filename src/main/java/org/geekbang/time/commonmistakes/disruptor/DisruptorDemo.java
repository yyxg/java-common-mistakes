package org.geekbang.time.commonmistakes.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * @Author xialh
 * @Date 2021/1/4 6:50 AM
 */
public class DisruptorDemo {

    public static void main(String[] args) {

        //指定RingBuffer大小,
        //必须是2的N次方
        int bufferSize = 1024;

        //构建Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<>(
                LongEvent::new,
                bufferSize,
                DaemonThreadFactory.INSTANCE);

        //注册事件处理器
        disruptor.handleEventsWith(
                (event, sequence, endOfBatch) ->
                        System.out.println("E: " + event));

        //启动Disruptor
        disruptor.start();

        //获取RingBuffer
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        //生产Event
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            bb.putLong(0, l);
            //生产者生产消息
            ringBuffer.publishEvent(
                    (event, sequence, buffer) ->
                            event.set(buffer.getLong(0)), bb);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


/**
 * 自定义Event
 */
class LongEvent {
    private long value;
    public void set(long value) {
        this.value = value;
    }

}
