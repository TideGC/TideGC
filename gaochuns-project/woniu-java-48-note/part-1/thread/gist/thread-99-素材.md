线程池

线程池其实是对于线程与任务两者合并的消费生产者模式。

自 JDK 5 开始，JDK 引入了 concurrent 包，也就是大名鼎鼎的 JUC ，其中最常见 ThreadPoolExecutor

常规知识点，线程池的几个重要参数

-tx-
|参数|变量|
|:-|:-|
|核心线程数|corePoolSize|
|最大线程数|maxPoolSize|
|线程空间时长|keepAliveTime|
|核心线程超时|allowCoreThreadTimeout，默认值 false 。|\
|            |若为true,空闲时，线程池中会没有线程 |
|任务队列|workQueue|
|拒绝处理器|rejectedExecutionHandler|
| 自带拒绝策略 |  AbortPolicy:默认策略，丢弃任务，抛RejectedExecutionException异常|\
| | DiscardPolicy:丢弃任务，但不抛异常|\
| | DiscardOldestPolicy:丢弃队列最前面的任务，然后重新尝试执行任务|\
| | CallerRunsPolicy:由调用线程处理任务|

线程池的工作原理，网上有很多资料，提交任务给线程池，大致流程：

1. 当线程池中线程数量小于 corePoolSize 则创建线程，并处理请求；
2. 当线程池中线程数量大于等于 corePoolSize 时，则把请求放入 workQueue 中，随着线程池中的核心线程们不断执行任务，只要线程池中有空闲的核心线程，线程池就从 workQueue 中取任务并处理
3. 当 taskQueue 已存满，放不下新任务时则新建非核心线程入池，并处理请求直到线程数目达到 maximumPoolSize（最大线程数量设置值）
4. 如果线程池中线程数大于 maximumPoolSize 则使用 RejectedExecutionHandler 来进行任务拒绝处理

参数设定

在日常使用过程中，需要关注的是各参数数值设定，那么如何设定呢？

一、线程数越多越好吗？

这个明显不是，比如单核CPU，设置上万个线程没有意义；而且线程过多，上下文切换，反而降低性能

这儿引出一个问题，单核能多线程吗？

进程是资源的基本单位，线程是 CPU 调度的基本单位

如果一个线程一直占用 CPU，那肯定多线程无用，但总有等待时候，如IO，此时，多线程就有了用武之地

线程数小了，显示又达不到最大化CPU性能

二、队列容量越大越好吗？

显然也不是，常人都关注线程数的设定，但队列大小鲜有人问，如果队列过大，积压相当多的任务，必然导致响应时间过长

如果队列过小，甚至没有，那任务没有缓冲，可能造成线程快速扩张

线程数与队列容量得配合使用，怎么才是合理的参数呢？

最直接的方式，模拟线上请求进行压测，随着请求量增加，QPS上升，当到了一定的阀值之后，请求数量增加QPS并不会增加，或者增加不明显，同时请求的响应时间却大幅增加。这个阀值我们认为是最佳线程数

对于线程数量多少，有很多的设定理论依据

### 任务类型

以任务类型设定，这是常用来粗略估值的

- IO 密集型

如果是 IO 密集型，一般是 Ncpu x 2 ；

IO 密集型 CPU 使用率不高，可以让 CPU 等待 IO 的时候处理别的任务，充分利用 CPU

- CPU 密集型

如果是 CPU 密集型 ，一般是 Ncpu + 1 ；

CPU使用率高，若开过多线程，增加线程上下文切换次数，带来额外开销

公式

前人早就有了计算公式

一、《Java Concurrency in Practice》

这本书中作者给出了定义

![862b29adf3308e7b9d7315a21bf1ce41](https://s4.51cto.com/images/blog/202102/26/862b29adf3308e7b9d7315a21bf1ce41.jpeg)

比如平均每个线程 CPU 运行时间为 0.5s，而线程等待时间（非 CPU 运行时间，比如 IO）为 1.5s ，CPU核心数为 8 ，那么根据上面这个公式估算得到：8 * (1+(1.5/0.5)=32

二、《Programming Concurrency on the JVM Mastering》

```
Nthread = Ncpu / (1-阻塞系数)
```

其中计算密集型阻塞系数为0，IO密集型阻塞系数接近1：

阻塞系数指的是任务的"生命周期"中，处于阻塞状态的时间的占比。比如，任务 90% 的时间处于阻塞状态而只有 10% 的时间在干活，那么阻塞系数就是 0.9 。

如果以这个示例使用第一个公式计算：2 * (1+(0.9/0.1)) = 20，结果也是 20 。

可以这么思考：两个公式表达不同，但实质一致，即

```
Ncpu/（1-阻塞系数）= Ncpu * (1+w/c)
```

则，阻塞系数=w/(w+c)，阻塞系数=阻塞时间/（阻塞时间+计算时间）

以第二个公式计算第一公式中的示例：8 / (1-(1.5/(1.5+0.5))) = 32

这样两个公式计算出的结果就是一样的

以公式计算法推算任务类型分类方式：

IO 型：`w/c≈1`，公式算出 2 x cpu

CPU型：`w/c≈0`，公式算出 1 x cpu

公式的方法有了，如何确定公式中的变量值呢？

最原始的办法打印日志，等待CPU时间无非就是 db、io、网络调用，在发包前，发包后打上日志，算得时间差，求出平均值。

三、其它公式

设置的线程数 = 目标QPS/(1/任务实际处理时间) = QPS x 每个任务处理时间

(核心线程就以平均 qps 定；最大线程就以最高 qps 定)

举例说明，假设目标 QPS=100 ，任务实际处理时间 0.2s，100 * 0.2 = 20个线程，这里的 20 个线程必须对应物理的 20 个CPU核心，否则将不能达到预估的 QPS 指标

```
队列大小 = 线程数 * (最大响应时间/任务实际处理时间)
```

假设目标最大响应时间为 0.4s ，计算阻塞队列的长度为 20 * (0.4 / 0.2) = 40

这个公式有点难以理解，最初的公式应该是

```
线程数/任务实际处理时间 * 响应时间 = 队列大小
```

类似 速度*时间=长度

这么多的公式，死记硬背是不行的，简单理解一下，qps 是每秒处理任务数，如果一个线程处理是 1s ，那么多少 QPS ，就需要多少线程了，若是处理 0.2s，那 1s 可以处理 5 个任务了，也就是 1/0.2 ，那线程数只需要 QPS/5 了，也就是公式 QPS/(1/任务处理时间) 。

