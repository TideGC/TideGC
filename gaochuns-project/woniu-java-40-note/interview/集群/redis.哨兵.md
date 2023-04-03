---
alias: Redis 哨兵模式集群
tags: redis, 哨兵, 集群
---

## 搭建 Redis 哨兵集群环境

### 一主两从配置

[[redis.主从|Redis 主从配置]]

### sentinel 配置

-   **sentinel/sentinel.conf**

    ```conf
    port 16379
    sentinel monitor mymaster 150.158.196.179 6379 1
    ```


[[sentinel monitor|sentinel monitor 配置项]]

如果主机设置了登录密码，那么这里你还要使用 [[sentinel auth-pass|sentinel auth-pass 配置项]]

### docker-compose.yml

```yaml
version: '3'

volumes:
  redis-master-data:
  redis-slave1-data:
  redis-slave2-data:

services:
  redis-master: 						# 主节点的容器
    image: redis:6.2.1
    container_name: redis-master
    volumes:
      - ./master.conf.d:/usr/local/etc/redis
    network_mode: host
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]

  redis-slave-1: 						# 从节点1的容器
    image: redis:6.2.1
    container_name: redis-slave-1
    volumes:
      - ./slave1.conf.d:/usr/local/etc/redis
    network_mode: host
    depends_on:
      - redis-master
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]

  redis-slave-2: 						# 从节点2的容器
    image: redis:6.2.1
    container_name: redis-slave-2
    volumes:
      - ./redis.conf.d:/usr/local/etc/redis
    network_mode: host
    depends_on:
      - redis-master
    command: ["redis-server", "/usr/local/etc/redis/redis.conf"]

  # sentinel 节点
  sentinel:
    image: redis:6.2.1
    container_name: sentinel
    volumes:
      - ./sentinel.conf.d:/usr/local/etc/redis
    network_mode: host
    depends_on:
      - redis-master
      - redis-slave-1
      - redis-slave-2
    command: ["redis-sentinel", "/usr/local/etc/redis/sentinel.conf", "--sentinel"]
```

### spring-boot 连接 Redis 哨兵集群

```yaml
spring:
  redis:
    sentinel:
      master: mymaster
      nodes:
        - 150.158.196.179:16379
```

```java
@Configuration
public class RedisConfig {

    @Bean
    public LettuceClientConfigurationBuilderCustomizer configurationBuilderCustomizer() {
//      return builder -> builder.readFrom(ReadFrom.REPLICA_PREFERRED);
        return new LettuceClientConfigurationBuilderCustomizer() {
            @Override
            public void customize(LettuceClientConfiguration.LettuceClientConfigurationBuilder builder) {
                builder.readFrom(ReadFrom.REPLICA_PREFERRED);
            }
        };
    }

}
```


这里的 ReadFrom 是配置 Redis 的读取策略，是一个枚举，包括下面选择：
| 枚举值 | 说明 |
| :- | :- |
| MASTER|从主节点读取|
|MASTER_PREFERRED|优先从 master 节点读取，master不可用才读取 replica|
|REPLICA|从 slave<small>（ replica ）</small>节点读取|
| REPLICA_PREFERRED|优先从 slave<small>（ replica ）</small>节点读取，所有的 slave 都不可用才读取 master 。|
