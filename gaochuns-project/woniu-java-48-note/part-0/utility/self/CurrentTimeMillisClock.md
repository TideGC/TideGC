```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class CurrentTimeMillisClock {
    private final AtomicLong now;

    private CurrentTimeMillisClock() {
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleTick();
    }

    private void scheduleTick() {
        new ScheduledThreadPoolExecutor(1, runnable -> {
            Thread thread = new Thread(runnable, "current-time-millis");
            thread.setDaemon(true);
            return thread;
        }).scheduleAtFixedRate(() -> {
            now.set(System.currentTimeMillis());
        }, 1, 1, TimeUnit.MILLISECONDS);
    }

    public long now() {
        return now.get();
    }

    public static CurrentTimeMillisClock getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static final CurrentTimeMillisClock INSTANCE = new CurrentTimeMillisClock();
    }

    // ==================================== test =====================================

    /**
     * 理论上来说，都是调用 100 次 ***`System.currentTimeMillis();`***，但是并行情况下比串行情况下要慢上很多（几百上千倍的差距），原因在于：
     *
     * 1. 调用 gettimeofday() 需要从用户态切换到内核态；
     * 2. gettimeofday() 的表现受 Linux 系统的计时器（时钟源）影响，在 HPET 计时器下性能尤其差；
     * 3. 系统只有一个全局时钟源，高并发或频繁访问会造成严重的争用。
     *
     * javaTimeMillis() 方法，这就是 System.currentTimeMillis() 的 native 实现。gettimeofday() 是其方法中的核心代码。
     *
     * 最常见的办法是用单个调度线程来按毫秒更新时间戳，相当于维护一个全局缓存。其他线程取时间戳时相当于从内存取，不会再造成时钟资源的争用，代价就是牺牲了一些精确度。
     * 使用的时候，直接 CurrentTimeMillisClock.getInstance().now()就可以了。
     * 不过，在 System.currentTimeMillis() 的效率没有影响程序整体的效率时，就完全没有必要做这种优化，这只是为极端情况准备的。
     */
    private static final int COUNT = 10000000;

    public static void main(String[] args) throws Exception {

        long beginTime = System.nanoTime();
        for (int i = 0; i < COUNT; i++) {
            System.currentTimeMillis();
        }
        long elapsedTime = System.nanoTime() - beginTime;
        // 单线程调用 100 次
        System.out.println("串行 : " + elapsedTime + " ns");

        // ------------------------------------------------------------------

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(COUNT);
        for (int i = 0; i < COUNT; i++) {
            new Thread(() -> {
                try {
                    startLatch.await();
//                    System.currentTimeMillis();
                    CurrentTimeMillisClock.getInstance().now();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    endLatch.countDown();
                }
            }).start();
        }
        beginTime = System.nanoTime();
        startLatch.countDown();
        endLatch.await();
        elapsedTime = System.nanoTime() - beginTime;
        // 100 个线程各自调用过一次
        System.out.println("并行 ： " + elapsedTime + " ns");
    }
}
```