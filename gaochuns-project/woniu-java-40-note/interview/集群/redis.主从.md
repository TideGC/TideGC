---
alias: Redis 主从环境搭建
tags: 主从, 集群
---

[[202212061629|《关于 Redis 主从集群》]]

## Redis 主从环境搭建


> [!info] 提示
> 为了简便起见，下面的案例是基于 docker/docker-compose 实现的，以减少大家找/创建多台服务器的麻烦。

### 第 1 步：准备主库配置文件

master.redis.conf

``` conf
# 监听端口。默认 6379
# port 6379
  
bind 0.0.0.0

# 指定自己占用的 IP 和端口
replica-announce-ip 192.172.0.16
replica-announce-port 6379
```

- bind 配置项说明见[[bind|《笔记》]]；
- replica-announce-ip 和 replica-announce-port 配置项说明见[[replica-announce-X|《笔记》]]；


### 第 2 步：准备主库

```yml
# docker container inspect --format='{{json .Mounts}}' redis-6379 | jq

# 主节点的容器
redis-master:
  image: redis:6.2.1
  container_name: redis-6379
  volumes:
    - ./master.redis.conf:/usr/local/etc/redis/redis.conf
  network_mode: host
  command: ["redis-server", "/usr/local/etc/redis/redis.conf"]
```

### 第 3 步：从机配置文件

slaver.redis.conf

``` conf
# 监听端口
port 6380
  
bind 0.0.0.0

# 指定自己占用的 IP 和端口
replica-announce-ip 192.172.0.16
replica-announce-port 6380

# 指定 master 占用的 IP 和端口
slaveof 192.172.0.16 6379
```
  
- bind 配置项说明见[[bind|《笔记》]]；
- replica-announce-ip 和 replica-announce-port 配置项说明见[[replica-announce-X|《笔记》]]；
- slaveof 配置项说明见[[slaveof|《笔记》]]。

### 第 4 步：准备从库

```yaml
# docker container inspect --format='{{json .Mounts}}' redis-6380 | jq

# 从节点的容器
redis-slave:
  image: redis:6.2.1
  container_name: redis-6380
  volumes:
    - ./slaver.redis.conf:/usr/local/etc/redis/redis.conf
  network_mode: host
  command: ["redis-server", "/usr/local/etc/redis/redis.conf"]
  depends_on:
    - redis-master
```


### 第 6 步：验证

#### 主机操作

执行命令行命令：

```sh
# 登录 master
shell> redis-cli -h 192.172.0.16 -p 6379
```

执行 Redis 命令：

```sh
# 查看主机的从机信息。
redis> info replication

# 会看到类似如下内容：
# Replication
# role:master
# connected_slaves:2
# slave0:ip=150.158.196.179,port=6380,state=online,offset=2718,lag=1
# slave1:ip=150.158.196.179,port=6381,state=online,offset=2718,lag=1
# ...

# 主机中存一个键值对（未来在从机中查询它）
redis> set hello world
```

#### 从机操作

执行命令行命令：

```sh
# 登录 slave
shell> redis-cli -h 192.172.0.16 -p 6380
```

执行 Redis 命令：

```sh
# 查看 hello 键值对
redis> get hello
```




