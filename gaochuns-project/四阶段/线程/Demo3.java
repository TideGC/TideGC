package com.example.threadpro.model;

import lombok.SneakyThrows;

import java.sql.Time;
import java.util.Objects;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 生产者消费者模型的重难点：
 * 1. 仓库中没空位时，生产者进行生产应该阻塞等待。直到仓库有了空位（原有的商品被消费者取走）后才能解除阻塞状态，进行生产；
 * 2. 仓库中没商品时，消费者进行消费应该阻塞等待。直到仓库有了商品（原有的空位被生产者放入了新商品）后才能解除阻塞状态，进行消费。
 *
 * .await(): 阻塞自己（当前线程）
 * .signal()：唤醒他人（另一个线程）
 *******************************************************************************************
 * 从纯理论的角度来讲，任何时刻都有可能发生线程的切换，
 * 即，让当前的 Running 状态的线程退回到 Runnable 状态，再从 Runnable 状态中随机性的选一个来执行。
 *
 * 但是在代码实现层面，大多数操作系统不会这么地随意地、随时随地地切换。
 * 当前线程总是会继续执行下去的，直到它（执行了某条代码）进入了阻塞状态，主动然出 CPU；
 * 或者，当前进程的时间片耗完了，需要发生进程的切换了，然后本进程下次再轮到它执行，操作系统再从本进程下的多个线程中再随机选一个（选中了其它线程）。
 *
 * 简而言之，为了减少不必要的线程切换的开销，当前正在执行的线程，总是会执行下去，只到它执行不下去为止。
 */
public class Demo3 {

    public static Character box = null;

    @SneakyThrows
    public static void main(String[] args) {

        // 起到的作用“同步锁”：用来"造"条件变量的
        Lock lock = new ReentrantLock();

        /*
         * 生产者等待消费者来通知它：你可以生产了；
         * 消费者等待生产者来通知它：你可以消费了。
         */
        Condition canRead = lock.newCondition();    // 消费者用
        Condition canWrite = lock.newCondition();   // 生产者用

        Thread t1 = new Thread(() -> {
            for (char c = 'a'; c <= 'z'; c++) {
                quietlySleep(300, TimeUnit.MILLISECONDS);
                lock.lock();
                if (box != null) {
                    try {
                        canWrite.await();   // 仓库有东西，我就等
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                box = c;
                canRead.signal();       // 生产完之后，通知消费者来消费
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 26; i++) {
                lock.lock();
                if (box == null) {
                    try {
                        canRead.await();    // 仓库没东西，我就等
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Character c = box;
                box = null;
                System.out.print(c + " ");
                canWrite.signal();      // 消费完之后，通知生产者来生产。
                lock.unlock();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();
    }

    // 工具方法
    public static void quietlySleep(long n, TimeUnit timeUnit) {
        try {
            timeUnit.sleep(n);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
