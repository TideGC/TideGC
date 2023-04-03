---
alias: ["Docker Run Redis", "Redis Run in Docker"] 
tags: [docker-images, docker-run, redis]
---

## Docker Run Redis 

docker hub 网址：[*https://hub.docker.com/_/redis*](https://hub.docker.com/_/redis)

当前<small>（ 2022-09-01 ）</small> latest / 7.0.4 / 7.0 / 7 是同一个版本。

### 第 0 步：关于 Redis 镜像和容器

#### 数据存储目录：容器内的 "/data" 
 
Redis 默认开启的是 RDB 持久化，所以生成的 .rdb 文件就放在这个目录下。

Redis 的 Dockerfile 通过 [[202208252105|VOLUME 指令]] 将 /data 目录默认挂载到了宿主机的磁盘空间，你可以通过 [[202211021334#查看容器的挂载卷|docker container inspect]] 命令查看默认挂载路径。

#### 配置文件目录：无

因为配置文件是启动时指定的，所以原则上配置文件可以在容器内任意。

我们可以这样"规划"：将配置文件放在 /etc/redis 目录下，且命名为 redis.conf 文件。<small>当然，你有别的"规划"也可以，反正你怎么"规划"的，你就怎么用。</small>

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 docker images 命令查看。

```bash
docker pull redis:6.2.1
```

### 第 2 步：docker run 运行容器

#### 示例一：默认数据卷和默认配置

```bash
## 默认挂载数据目录：docker inspect --format='{{json .Mounts}}' mysql-3306 | jq
docker run -d --rm \
	--name redis-6379 \
	-v /etc/localtime:/etc/localtime:ro \
	-p 6379:6379 \
	redis:6.2.1
```

#### 方式二：数据卷和外部配置文件

这里利用了一个[[docker.redis#小技巧：如何 弄到 默认配置文件|小技巧]]获得 Redis 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```bash
## 使用数据卷

## docker volume rm redis-6379-conf
## docker volume create --name redis-6379-data
docker run -d --rm \
	--name redis-6379  \
    -v /etc/localtime:/etc/localtime:ro  \
    -v redis-6379-data:/data  \
    -v $(pwd)/redis.conf:/etc/redis.conf \
    -p 6379:6379 \
    redis:6.2.1 \
    redis-server /etc/redis.conf
```

#### 方式三：docker-compose

```yml
  redis-6379:
    container_name: redis-6379
    image: redis:6.2.1
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - redis-6379-data:/data
      - ./redis/redis.conf:/etc/redis.conf
    ports:
      - 6379:6379
    command: ["redis-server", "/etc/redis.conf"]
```

### 第 3 步：验证

```bash
## 查看容器的运行信息
docker ps

## 进入 redis-6379 容器
docker exec -it redis-6379 /bin/bash

## 执行 redis-cli 连接 redis server
redis-cli
```

### 其它

#### 小技巧：如何"弄到"默认配置文件

> 如果你不准备"动"默认配置，那么以下内容就用不上。

由于 redis 的默认配置的提供思路是硬编码在代码中的，而不是默认配置，所以，我们这里不能使用之前的 Redis、MySQL 那样的技巧从容器中找到默认的配置文件。

我们这里需要去找到解压版的 redis 6.2.1 ，或者是 redis 6.2.1 的源码，从中复制它的默认配置：[Github 源码中的默认配置文件](https://github.com/redis/redis/blob/6.2.1/redis.conf)

配置文件中大段的内容都是注释，以及默认没有开启的功能、特性。去掉这部分内容，有效内容如下：

redis.conf

```conf
bind 127.0.0.1 -::1
protected-mode yes
port 6379
tcp-backlog 511
timeout 0
tcp-keepalive 300
daemonize no
pidfile /var/run/redis_6379.pid
loglevel notice
logfile ""
databases 16
always-show-logo no
set-proc-title yes
proc-title-template "{title} {listen-addr} {server-mode}"
stop-writes-on-bgsave-error yes
rdbcompression yes
rdbchecksum yes
dbfilename dump.rdb
rdb-del-sync-files no
dir ./
replica-serve-stale-data yes
replica-read-only yes
repl-diskless-sync no
repl-diskless-sync-delay 5
repl-diskless-load disabled
repl-disable-tcp-nodelay no
replica-priority 100
acllog-max-len 128
lazyfree-lazy-eviction no
lazyfree-lazy-expire no
lazyfree-lazy-server-del no
replica-lazy-flush no
lazyfree-lazy-user-del no
lazyfree-lazy-user-flush no
oom-score-adj no
oom-score-adj-values 0 200 800
disable-thp yes
appendonly no
appendfilename "appendonly.aof"
appendfsync everysec
no-appendfsync-on-rewrite no
auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb
aof-load-truncated yes
aof-use-rdb-preamble yes
lua-time-limit 5000
slowlog-log-slower-than 10000
slowlog-max-len 128
latency-monitor-threshold 0
notify-keyspace-events ""
hash-max-ziplist-entries 512
hash-max-ziplist-value 64
list-max-ziplist-size -2
list-compress-depth 0
set-max-intset-entries 512
zset-max-ziplist-entries 128
zset-max-ziplist-value 64
hll-sparse-max-bytes 3000
stream-node-max-bytes 4096
stream-node-max-entries 100
activerehashing yes
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit replica 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60
hz 10
dynamic-hz yes
aof-rewrite-incremental-fsync yes
rdb-save-incremental-fsync yes
jemalloc-bg-thread yes
```

#### 配置文件中需要改动的地方

由于 Redis 默认只接受从 127.0.0.1 即，本机发起的连接请求<small>（ 即，bind 配置项 ）</small>。

所以，我们需要放开这个设置，因此，我们要将 Redis 配置文件中的 bind 配置项的内容改成如下：

```
bind 0.0.0.0
```

