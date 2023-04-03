package com.example.threadpro.model;

import lombok.SneakyThrows;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * 生产者消费者模型的重难点：
 * 1. 仓库中没空位时，生产者进行生产应该阻塞等待。直到仓库有了空位（原有的商品被消费者取走）后才能解除阻塞状态，进行生产；
 * 2. 仓库中没商品时，消费者进行消费应该阻塞等待。直到仓库有了商品（原有的空位被生产者放入了新商品）后才能解除阻塞状态，进行消费。
 *
 * Semaphore 在代码中"影射"资源数量。acquire 和 release 方法就是在表示生产和消费资源。
 *  - 生产者要生产，需要有"空位"；消费者要消费，需要有"商品"。
 *  - 生产者是消耗空位，造出商品；消费者是消耗商品，造出空位。
 *  - 生产者和消费者是相互需要的。
 */
public class Demo2 {

    public static Character box = null;

    @SneakyThrows
    public static void main(String[] args) {

        Semaphore productNum = new Semaphore(0);
        Semaphore emptyNum = new Semaphore(1);

        Thread t1 = new Thread(() -> {
            for (char c = 'a'; c <= 'z'; c++) {
                try {
                    emptyNum.acquire(); // 空位数减一  // 1 => 0
                    box = c;
                    productNum.release(); // 商品数加一 // 0 => 1
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 26; i++) {
                    TimeUnit.MILLISECONDS.sleep(300);
                    productNum.acquire();   // 商品数减一
                    Character c = box;
                    box = null;
                    emptyNum.release(); // 空位数加一
                    System.out.print(c + " ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

    }

}
