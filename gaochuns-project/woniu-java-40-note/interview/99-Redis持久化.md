# Redis 持久化

Redis 的读写都是在内存中，所以它的性能较高，但在内存中的数据会随着服务器的重启而丢失，为了保证数据不丢失，我们需要将内存中的数据存储到磁盘，以便 Redis 重启时能够从磁盘中恢复原有的数据，而整个过程就叫做 Redis 持久化。

> [!cite] 扩展
> Redis 持久化也是 Redis 和 Memcached 的主要区别之一，因为 Memcached 不具备持久化功能。

Redis 持久化有 3 种方式：

1. 快照方式<small>（RDB, Redis DataBase）</small>将某一个时刻的内存数据，以二进制的方式写入磁盘；
2. 文件追加方式<small>（AOF, Append Only File）</small>，记录所有的操作命令，并以文本的形式追加到文件中；
3. Redis 4.0 开始出现的混合持久化方式。混合持久化是结合了 RDB 和 AOF 的优点，在写入的时候，先把当前的数据以 RDB 的形式写入文件的开头，再将后续的操作命令以 AOF 的格式存入文件，这样既能保证 Redis 重启时的速度，又能减低数据丢失的风险。

## 1. RDB

RDB<small>（Redis DataBase）</small>是将某一个时刻的内存快照<small>（Snapshot）</small>，以二进制的方式写入磁盘的过程。

RDB 的持久化触发方式有 2 类：

1. 手动触发；
2. 自动触发。

### 手动触发

手动触发持久化的操作有两个： `save` 和 `bgsave` ，它们主要区别体现在：**是否阻塞 Redis 主线程的执行** 。

- save 命令

  在客户端中执行 `save` 命令，就会触发 Redis 的持久化，但同时也是使 Redis 处于阻塞状态，直到 RDB 持久化完成，才会响应其他客户端发来的命令，所以在生产环境一定要慎用。

  ```bash
  127.0.0.1:6379> save
  OK
  ```

  save 命令会在当前目录下生成一个名为 dump.rdb 的文件。

  ::: danger 再强调一遍 
  save 命令会阻塞 Redis 的工作线程，直到 save 工作结束，在此期间，Redis 不会接收、处理其他命令。
  :::

- bgsave 命令

  bgsave<small>（background save）</small>既后台保存的意思， 它和 save 命令最大的区别就是 bgsave 会 fork() 一个子进程来执行持久化，整个过程中只有在 fork() 子进程时有短暂的阻塞，当子进程被创建之后，Redis 的主进程就可以响应其他客户端的请求了，相对于整个流程都阻塞的 save 命令来说，显然 bgsave 命令更适合我们使用。 

  ```bash
  127.0.0.1:6379> bgsave
  Background saving started
  ```


















### 自动触发

RDB 自动持久化主要来源于以下几种情况：
