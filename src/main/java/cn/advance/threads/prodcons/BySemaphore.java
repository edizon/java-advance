package cn.advance.threads.prodcons;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author zhoueq
 */
public class BySemaphore {

    public static void main(String[] args) {
        Resource04 resource = new Resource04();

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                resource.produce();
            }, String.valueOf(i)).start();
        }

        for (int i = 0; i < 10; i++) {
            new Thread(() -> {
                resource.consume();
            }, String.valueOf(i)).start();
        }

        try {
            TimeUnit.SECONDS.sleep(3L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("搞定...");
    }
}

class Resource04 {
    final Semaphore res = new Semaphore(10);
    final Semaphore notEmpty = new Semaphore(0);
    final Semaphore mutex = new Semaphore(1);
    static Integer count = 0;

    void produce() {
        for (int i = 0; i < 10; i++) {
            try {
                res.acquire();
                mutex.acquire();
                count++;
                System.out.println(Thread.currentThread().getName() + " 生产者生产, 生产数值=" + count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
                notEmpty.release();
            }
        }
    }

    void consume() {
        for (int i = 0; i < 10; i++) {
            try {
                notEmpty.acquire();
                mutex.acquire();
                count--;
                System.out.println(Thread.currentThread().getName() + " 消费者消费, 消费数值=" + count);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mutex.release();
                res.release();

            }
        }

    }
}
