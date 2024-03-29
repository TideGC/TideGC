# CompletableFuture

    CompletableFuture 是 Java 8 提出的新的异步编程方式。

JDK 5 引入了 Future 模式。Future 接口是 Java 多线程 Future 模式的实现，在 java.util.concurrent 包中，可以来进行异步计算。

Future 模式是多线程设计常用的一种设计模式。Future 模式可以理解成：

> 我有一个任务，提交给了 Future，Future 替我完成这个任务。期间我自己可以去做任何想做的事情。一段时间之后，我就便可以从 Future 那儿取出结果。

Future 的接口很简单，只有五个方法。

```java
public interface Future<V> {

    boolean cancel(boolean mayInterruptIfRunning);

    boolean isCancelled();

    boolean isDone();

    V get() throws InterruptedException, ExecutionException;

    V get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException;
}
```









