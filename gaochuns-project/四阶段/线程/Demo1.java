package com.example.threadpro.model;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * 生产者消费者模型的重难点：
 * 1. 仓库中没空位时，生产者进行生产应该阻塞等待。直到仓库有了空位（原有的商品被消费者取走）后才能解除阻塞状态，进行生产；
 * 2. 仓库中没商品时，消费者进行消费应该阻塞等待。直到仓库有了商品（原有的空位被生产者放入了新商品）后才能解除阻塞状态，进行消费。
 *
 * 在下述实现中，我们利用的是容量为 1 的阻塞队列：
 *  1. 当队列中有数据的时候，生产者进行 put 操作时，会阻塞等待；
 *  2. 当队列中没有数据的时候，消费者进行 take 操作时，会阻塞等待。
 */
public class Demo1 {

    public static BlockingQueue<Character> box = new ArrayBlockingQueue<Character>(1);

    @SneakyThrows
    public static void main(String[] args) {

        Thread t1 = new Thread(() -> {
            for (char c = 'a'; c <= 'z'; c++) {
                try {
                    box.put(c);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            try {
                for (int i = 0; i < 26; i++) {
                    TimeUnit.MILLISECONDS.sleep(300);
                    Character c = null;
                    c = box.take();
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
