package cn.advance.threads.prodcons;

import java.util.concurrent.TimeUnit;

/**
 * @Classname ByReentrantLock
 * @Description 用 synchronized 实现的生产者消费者模式
 * @Date 2019-05-28 23:05
 * @Author zhoueq
 */
class Resource {
    private static int count = 0;
    private static final Object lock = new Object();
    private static final Object lock2 = new Object();

    void produce() {
        synchronized (lock) {
            while (count == 10) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count++;
            System.out.println(Thread.currentThread().getName() + " 生产者生产中，生产的数值=" + count);
            lock.notify();
            /*
             * 此处用notify或者notifyAll的效果是一样的，
             * notify: Wakes up a single thread that is waiting on this object's monitor
             * notifyAll: Wakes up all threads that are waiting on this object's monitor
             * 区别是notify唤醒一个，而且是有jvm指定的某一个，具有随机性；
             * notifyAll唤醒所有在等在线程，由他们去抢，谁先抢到，谁可以继续执行；
             */
        }
    }

    void consume() {
        synchronized (lock2) {
            while (count == 0) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            count--;
            System.out.println(Thread.currentThread().getName() + " 消费者消费中，消费的数值=" + count);
            lock.notify();
        }
    }
}

public class BySynchronized {

    public static void main(String[] args) {
        Resource resource = new Resource();

        for (int i = 0; i < 2; i++) {
            new Thread(() -> resource.produce(), String.valueOf(i)).start();
        }

        for (int i = 4; i < 6; i++) {
            new Thread(() -> resource.consume(), String.valueOf(i)).start();
        }

        sleep();
        System.out.println("搞定...");
    }

    private static void sleep() {
        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
