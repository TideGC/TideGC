---
alias: Redis Cluster 集群
---

## 搭建 Redis Cluster 集群环境

[[202212061648|《关于 Redis Cluster 集群方案》]]

现在我们使用 Docker / Docker-Compose 搭建最简单的 Redis Cluster 集群：3 主节点，无从节点。

Redis 规定 Cluster 集群中最少要有 3 个节点，如果主节点数不足，在搭建集群过程中你会看到类似如下信息：

```
*** ERROR: Invalid configuration for cluster creation.
*** Redis Cluster requires at least 3 master nodes.
*** This is not possible with 2 nodes and 0 replicas per node.
*** At least 3 nodes are required.
```


### 第 1 步：为三个节点准备 redis.conf 配置文件

``` conf
port 7001
#port 7002
#port 7003

bind 0.0.0.0
replica-announce-ip 150.158.196.179
replica-announce-port 7001
#replica-announce-port 7002
#replica-announce-port 7003
databases 1

cluster-enabled yes

cluster-node-timeout 5000
```

- cluster-enabled 配置项说明参见[[cluster-enabled|《笔记》]]
- cluster-node-timeout 配置项说明参见[[cluster-node-timeout|《笔记》]]

### 第 2 步：编写 docker-compose.yml 文件

```yaml
version: '3'

# docker container inspect --format='{{json .Mounts}}' <容器名 > | jq

services:
  redis-7001:
    image: redis:6.2.1
    container_name: redis-7001
    volumes:
      - ./7001.conf.d:/usr/local/etc/redis
    network_mode: host
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]

  redis-7002:
    image: redis:6.2.1
    container_name: redis-7002
    volumes:
      - ./7002.conf.d:/usr/local/etc/redis
    network_mode: host
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]

  redis-7003:
    image: redis:6.2.1
    container_name: redis-7003
    volumes:
      - ./7003.conf.d:/usr/local/etc/redis
    network_mode: host
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]
```

执行 `docker-compose up -d` 启动 3 个 Redis 服务，通过 `docker-compose logs -f` 可观察其运行日志。

注意，此时仅仅是运行了 3 个 Redis 服务而已，还未将其配置成 Redis Cluster 集群。

### 第 3 步：创建 Cluster 集群

redis-cli 中有一套 `--cluster` 命令专门用于操作 Redis Cluster 集群，你可以通过如下命令查看到这套命令的简介：

```sh
redis-cli --cluster help
```

在这套命令中，`redis-cli --cluster create` 是用于将以运行的<small>（ 至少 3 个 ）</small>Redis 服务配置成 Redis Cluster 集群的命令。其语法如下：

``` sh
redis-cli --cluster create --cluster-replicas <每个主节点的从节点数> \
  节点IP:port \
  节点IP:port \
  ...
```

结合我们的 Docker Redis 的情况我们执行如下命令：

```sh
redis-cli --cluster create --cluster-replicas 0 \
  150.158.196.179:7001 \
  150.158.196.179:7002 \
  150.158.196.179:7003
```

执行过后，你会看到如下的信息，redis 会询问你是否要将这 3 个节点配置成 Redis Cluster 集群：

```
>>> Performing hash slots allocation on 3 nodes...
Master[0] -> Slots 0 - 5460
Master[1] -> Slots 5461 - 10922
Master[2] -> Slots 10923 - 16383
M: 1343ec7ebaf455575a8992353cefff4d8d4089bd 150.158.196.179:7001
   slots:[0-5460] (5461 slots) master
M: d1c21ab875be15e79b4e7ea4ca05073f8897b092 150.158.196.179:7002
   slots:[5461-10922] (5462 slots) master
M: 9e351f5426c30e018b40201e0b6c5ca599f1b4c1 150.158.196.179:7003
   slots:[10923-16383] (5461 slots) master
```

输入 `yes` 后，等待一小会，你会看见配置成功信息。

```
>>> Nodes configuration updated
>>> Assign a different config epoch to each node
>>> Sending CLUSTER MEET messages to join the cluster
Waiting for the cluster to join
.....
>>> Performing Cluster Check (using node 150.158.196.179:7001)
M: 1343ec7ebaf455575a8992353cefff4d8d4089bd 150.158.196.179:7001
   slots:[0-5460] (5461 slots) master
M: 9e351f5426c30e018b40201e0b6c5ca599f1b4c1 150.158.196.179:7003
   slots:[10923-16383] (5461 slots) master
M: d1c21ab875be15e79b4e7ea4ca05073f8897b092 150.158.196.179:7002
   slots:[5461-10922] (5462 slots) master
[OK] All nodes agree about slots configuration.
>>> Check for open slots...
>>> Check slots coverage...
[OK] All 16384 slots covered.
```

### 第 4 步：验证

使用类似如下命令<small>（ 连上 3 节点中的任意节点 ）</small>可查看 cluster 集群信息：

```sh
redis-cli -p 7001 cluster nodes
```

你会看到类似如下信息：

```
137cc57c56a98d23d881241f57282f403566f6eb 10.0.4.2:7001@17001 myself,master - 0 1656631635000 1 connected 0-5460
d4d284356789fa3af8be2454450c2770f5914c2b 150.158.196.179:7002@17002 master - 0 1656631637013 2 connected 5461-10922
e025f214b21458201d7a52720520931904bc98f3 150.158.196.179:7003@17003 master - 0 1656631636010 3 connected 10923-16383
```

使用类似如下命令<small>（ 连上 3 节点中的任意节点 ）</small>，执行 Redis 读写操作：

```sh
redis-cli -c -p 7001
```

在 redis-cli 中先后执行 `get hello` 和 `get a` 观察其不同。

