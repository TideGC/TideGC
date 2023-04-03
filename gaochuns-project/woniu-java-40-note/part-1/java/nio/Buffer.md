NIO 的缓冲区本质上是一个内存块，既可以写入数据，也可以从中读取数据。

Java NIO 中代表缓冲区的 Buffer 类是一个抽象类，位于 java.nio 包中。

> Buffer 类是一个非线程安全类，所以不要在多个线程之间共享它。

Buffer 是一个抽象类，对应于 Java 的主要数据类型，在 NIO 中，有 8 种缓冲区类：ByteBuffer / CharBuffer / DoubleBuffer / FloatBuffer / IntBuffer / LongBuffer / ShortBuffer / MappedByBuffer 。

前 7 种类型涵盖了能在 IO 中传输的所有 Java 基本数据类型，第 8 种类型是一种专门用于内存映射的 ByteBuffer 类型。不同的 Buffer 子类可以操作的数据类型能够通过名称进行判断，比如 IntBuffer 只能操作 Integer 类型的对象。

实际上，使用最多的是 ByteBuffer（二进制字节缓冲区）类型。

### Buffer 类的重要属性

Buffer 的子类会拥有一块"与自己类型对应的数组"作为内存区，来进行数据的读写缓冲区。例如，ByteBuffer 子类就拥有一个 byte[] 类型的数组成员。 

为了记录读写的状态和位置，Buffer 类额外提供了一些重要的属性，其中有三个重要的成员属性：capacity（容量）、position（读写位置）和 limit（读写的限制）。

#### capacity 属性

Buffer 类的 capacity 属性表示内部容量的大小。

Buffer 类的对象在初始化时会按照 capacity 分配内部数组的内存，在数组内存分配好之后，它的大小就不能改变了。

因此，一旦写入的数据量超过了 capacity ，缓冲区就满了，不能再写入了。

> [!cite] 啰嗦一个小细节
> capacity 并不是指内部的内存块 byte[] 数组的字节数量，而是指能写入的数据对象的最大限制数量。
> 
> 例如， 子类 DoubleBuffer 能写入的数据类型是 double ，如果在创建实例时其 capacity 是 100 ，那么我们最多可以写入 100 个double类型的数据。

#### position 属性

Buffer 类的 position 属性表示当前的位置。position 属性的值与缓冲区的读写模式有关。

在不同的模式下，position 属性值的含义是不同的，在缓冲区进行读写的模式改变时，position值会进行相应的调整。

- 在写模式下，position 值的变化规则如下：

  1. 在刚进入写模式时，position 值为 0 ，表示当前的写入位置为从头开始。
  2. 每当一个数据写到缓冲区之后，position 会向后移动到下一个可写的位置。
  3. 初始的 position 值为 0 ，最大可写值为 limit-1 。当position 值达到 limit 时，缓冲区就已经无空间可写了。

- 在读模式下，position 值的变化规则如下：

   1. 当缓冲区刚开始进入读模式时，position 会被重置为 0 。
   2. 当从缓冲区读取时，也是从 position 位置开始读。读取数据后，position 向前移动到下一个可读的位置。
   3. 在读模式下，limit 表示可读数据的上限。position 的最大值为最大可读上限 limit ，当 position 达到 limit 时表明缓冲区已经无数据可读。

Buffer 的读写模式具体如何切换呢？

当新建了一个缓冲区实例时，缓冲区处于写模式，这时是可以写数据的。在数据写入完成后，如果要从缓冲区读取数据，就要进行模式的切换，可以调用 **flip()** 方法将缓冲区变成读模式，flip 为翻转的意思。

在从写模式到读模式的翻转过程中，position 和 limit 属性值会进行调整，具体的规则是：

1. limit 属性被设置成写模式时的 position 值，表示可以读取的最大数据位置。

2. position 由原来的写入位置变成新的可读位置，也就是 0 ，表示可以从头开始读。


#### limit 属性

Buffer 类的 limit 属性表示可以写入或者读取的数据最大上限，其属性值的具体含义也与缓冲区的读写模式有关。

在不同的模式下，limit 值的含义是不同的，具体分为以下两种情况：

- 在写模式下，limit 属性值的含义为可以写入的数据最大上限。在刚进入写模式时，limit 的值会被设置成缓冲区的 capacity 值，表示可以一直将缓冲区的容量写满。
- 在读模式下，limit 值的含义为最多能从缓冲区读取多少数据。

一般来说，在进行缓冲区操作时是先写入再读取的。当缓冲区写入完成后，就可以开始从Buffer读取数据，调用flip()方法（翻转），这时limit的值也会进行调整。具体如何调整呢？将写模式下的 position值设置成读模式下的limit值，也就是说，将之前写入的最大数量作为可以读取数据的上限值。

Buffer 在翻转时的属性值调整主要涉及 position、limit 两个属性，但是这种调整比较微妙，不是太好理解，下面举一个简单的例子：

首先，创建缓冲区。新创建的缓冲区处于写模式，其 position 值为 0 ，limit 值为最大容量 capacity 。

然后，向缓冲区写数据。每写入一个数据，position 向后面移动一个位置，也就是 position 的值加 1 。这里假定写入了 5 个数，当写入完成后，position 的值为 5 。

最后，使用 flip 方法将缓冲区切换到读模式。limit 的值会先被设置成写模式时的 position 值，所以新的 limit 值是 5 ，表示可以读取数据的最大上限是 5 。之后调整 position 值，新的 position 会被重置为 0，表示可以从 0 开始读。

缓冲区切换到读模式后就可以从缓冲区读取数据了，一直到缓冲区的数据读取完毕。 

#### mark 属性

除了以上 capacity、position、limit 三个重要的成员属性之外，

Buffer 还有一个比较重要的标记属性：mark（标记）属性。该属性的大致作用为：在缓冲区操作过程当中，可以将当前的 position 值临时存入 mark 属性中；需要的时候，再从 mark 中取出暂存的标记值，恢复到 position 属性中，重新从 position 位置开始处理。


|     属性 | 说明 |
| :------- | :- |
| capacity | 容量，即可以容纳的最大数据量，在缓冲区创建时设置并且不能改变|
|    limit | 读写的限制，缓冲区中当前的数据量 |
| position | 读写位置，缓冲区中下一个要被读或写的元素的索引 |
|     mark | 调用 mark() 方法来设置 mark=position，再调用 reset() 让 position 恢复到 mark 标记的位置。|












### Buffer 类的重要方法

Buffer 类的常用方法包括创建、写入、读取、重复读、标记和重置等。

#### allocate 方法

在使用 Buffer 实例之前，我们首先需要获取 Buffer 子类的实例对象，并且分配内存空间。需要获取一个 Buffer 实例对象时，并不是使用子类的构造器来创建，而是调用子类的 allocate() 方法。

下面的程序片段演示如何获取一个整型的 Buffer 实例对象：

```java
import com.crazymakercircle.util.Logger;
import java.nio.IntBuffer;

public class UseBuffer
{
   // 一个整型的Buffer静态变量
   static IntBuffer intBuffer = null;

   public static void allocateTest() {
     // 创建一个intBuffer实例对象
     intBuffer = IntBuffer.allocate(20);
     Logger.debug("------------after allocate-------
-----------");
     Logger.debug("position=" + intBuffer.position());
     Logger.debug("   limit=" + intBuffer.limit());
     Logger.debug("capacity=" + intBuffer.capacity());
   }
                  
   // 省略其他代码
}
```

本例中，IntBuffer 是具体的 Buffer 子类，通过调用 IntBuffer.allocate(20) 创建了一个 intBuffer 实例对象，并且分配了 20×4 字节的内存空间。

运行程序之后，通过程序的输出结果，我们可以查看一个新建缓冲区实例对象的主要属性值，如下所示：

```
allocatTest |> ------------after allocate------------------
allocatTest |> position=0
allocatTest |> limit=20
allocatTest |> capacity=20
```

从上面的运行结果可以看出：一个缓冲区在新建后处于写模式，position 属性（代表写入位置）的值为 0 ，缓冲区的 capacity 值是初始化时 allocate 方法的参数值（这里是20），而limit最大可写上限值也为 allocate 方法的初始化参数值。

