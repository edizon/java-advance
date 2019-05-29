package cn.advance.threads.prodcons;

import java.util.concurrent.TimeUnit;

/**
 * @Classname ByBlockingQueue
 * @Description 用synchronized实现的生产者消费者模式
 * @Date 2019-05-28 22:39
 * @Author zhouenquan
 */
public class ByBlockingQueue {

    private static Integer count = 0;
    private static final Integer FULL = 10;
    private static final Object lock = new Object();

    public static void main(String[] args) {
        ByBlockingQueue service = new ByBlockingQueue();
        //new Thread(new Producer()).start();
        //new Thread(new Consumer()).start();

        while (Thread.activeCount() > 2) {
            Thread.yield();
        }
        System.out.println("finished...");
    }


    class Producer implements Runnable {

        @Override
        public void run() {
            System.out.println("produce start...");

            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock) {
                    while (count.equals(FULL)) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count++;
                    System.out.println(Thread.currentThread().getName() + "producing... count=" + count);
                    lock.notifyAll();
                }
            }
        }
    }

    class Consumer implements Runnable {

        @Override
        public void run() {
            System.out.println("consume start...");
            for (int i = 0; i < 10; i++) {
                try {
                    TimeUnit.SECONDS.sleep(1L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                synchronized (lock) {
                    while (count.equals(0)) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    count--;
                    System.out.println(Thread.currentThread().getName() + "consuming... count=" + count);
                    lock.notifyAll();
                }
            }
        }
    }

}

class Index {

    private Integer count;
    private Object lock = new Object();
}


