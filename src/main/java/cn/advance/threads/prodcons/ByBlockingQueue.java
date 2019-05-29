package cn.advance.threads.prodcons;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @Classname ByBlockingQueue
 * @Description 用 ByBlockingQueue 实现的生产者消费者模式
 * @Date 2019-05-28 22:39
 * @Author zhoueq
 */
public class ByBlockingQueue {

    public static void main(String[] args) {
        Resource03 resource = new Resource03();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                resource.produce();
            }, String.valueOf(i)).start();
        }

        for (int i = 10; i < 20; i++) {
            new Thread(() -> {
                resource.consume();
            }, String.valueOf(i)).start();
        }
    }

}

class Resource03 {
    private static BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(10);
    private static final Object lock = new Object();

    void produce() {
        int index = 0;
        while (true) {
            try {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                blockingQueue.put(String.valueOf(index));
                System.out.println(Thread.currentThread().getName() + " 生产者生产, 生产数值=" + index);
                index++;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    void consume() {
        while (true) {
            try {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                String v = blockingQueue.take();
                System.out.println(Thread.currentThread().getName() + " 消费者消费, 消费数值=" + v);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


