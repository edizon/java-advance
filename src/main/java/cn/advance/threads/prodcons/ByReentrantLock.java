package cn.advance.threads.prodcons;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Classname ByReentrantLock
 * @Description 用 ReentrantLock 实现的生产者消费者模式
 * @Date 2019-05-29 13:05
 * @Author zhoueq
 */
public class ByReentrantLock {

    public static void main(String[] args) {
        Resource02 resource = new Resource02();
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                resource.produce();
            }, String.valueOf(i)).start();
        }

        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                resource.consume();
            }, String.valueOf(i)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("搞定...");
    }

}

class Resource02 {
    private ReentrantLock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    private Integer count = 0;

    void produce() {
        lock.lock();
        try {
            while (count.equals(10)) {
                condition.await();
            }
            count++;
            System.out.println(Thread.currentThread().getName() + " 生产者生产, 生产数值=" + count);
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    void consume() {
        lock.lock();
        try {
            while (count.equals(0)) {
                condition.await();
            }
            System.out.println(Thread.currentThread().getName() + " 消费者消费, 消费数值=" + count);
            count--;
            condition.signal();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

