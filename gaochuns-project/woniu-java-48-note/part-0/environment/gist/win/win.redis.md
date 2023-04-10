---
alias: Redis 在 Windows 上的安装
---

## Redis 在 Windows 上的安装

> [!danger] 警告
> 解压路径不要有空格和中文！
> 
> 例如：**D:\\ProgramFiles** 就是很好的选择。

### 第 1 步：下载、解压

从 [https://github.com/MicrosoftArchive/redis/releases/](https://github.com/MicrosoftArchive/redis/releases/) 上下载 Redis 3.2.100<small>（这是解压免安装版的最高版本）</small>。

解压压缩包到 **D:\\ProgramFiles** 目录。

> [!info] 提示
> 因此，后文的 REDIS_HOME 指的就是 D:\ProgramFiles\Redis-x64-3.2.100 。

解压后会看到类似如下内容

![redis-install-01.png](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627170954.png)


| 文件名  | 简要 |
| :- | :- |
| redis-benchmark.exe     | 基准测试 |
| redis-check-aof.exe     | aof |
| redischeck-dump.exe     | dump |
| **redis-cli.exe**      | 客户端 |
| **redis-server.exe**   | 服务器 |
| **redis.windows.conf** | 配置文件 |

### 第 2 步：启动

在 REDIS_HOME 目录下打开一个命令行终端，执行如下命令行命令：

```bash
redis-server.exe redis.windows.conf

## 简写。有些版本的 Windows 系统无法简写，原因不明。
redis-server.exe
```

![redis-install-02.png](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627170956.png)

当出现此界面的时候，说明启动成功，Redis 正监听端口 **6379**，等待客户端发起连接。

通过上述命令启动 redis-server 之后，执行启动命令的那个命令行窗口就一直是被占用着的。只要它一直是占用状态，Redis Server 就一直在运行。


### 第 3 步：连接到 Redis Server

进入 REDIS_HOME ，打开一个命令行终端，执行如下命令行命令：

```bash
redis-cli.exe -h 127.0.0.1 -p 6379 

# 简写
redis-cli.exe
```

![redis-install-03.png](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627170959.png)

redis-cli 的完整格式是：

```bash
redis-cli.exe -h <指定ip> -p <指定端口> -a <指定密码>
```

> [!info] 提示
> 你也可以使用其它的客户端连接 Redis Server ，例如 RDM 。

### 其它：关闭 Redis Server

强行终止 Redis Server 进程可能会导致 redis 持久化数据丢失。正确停止 Redis 的方式应该是向 Redis 发送 SHUTDOWN 命令。

进入 REDIS_HOME ，打开一个命令行终端，执行如下命令行命令：

```bash
redis-cli.exe -h 127.0.0.1 -p 6379 shutdown 

# 简写
redis-cli.exe shutdown 
```

### 其它：如何设置 Redis 密码 

> 了解

**默认连接 Redis 是不需要密码的**，因为配置文件 "redis.windows.conf" 中并没有指定认证密码。

如有需要，打开 "redis.window.conf" 找到 `# requirepass foobared`（这一行是注释），在其下新增内容：

```conf
requirepass 123456
```

关闭 Redis Server ，再重新启动。现在在客户端窗口第一次输入命令时，redis 会给出 "(error) NOAUTH Authentication required." 提示，要求你提供密码，为此你必须使用 "**auth**" 命令：

```
127.0.0.1:6379> auth 123456
OK
```

或者在 "redis-cli" 连接的时候，就指定密码：*redis-cli.exe -a 123456*

