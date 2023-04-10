---
alias: 
- cluster-enabled 配置项
tags: 
- redis
- redis-cluster
- cluster-enabled
- 配置项
---

## cluster-enabled 配置项

```conf
cluster-enabled no
```

Redis 服务器在启动时会根据 **cluster-enabled** 配置项的值来决定是否开启服务器的集群模式。默认值是 `no` ，表示不开启。

当 cluster-enabled 的值是 false 时，就是最长见的单机模式运行，此时 Redis 服务器就是一个普通 Redis 服务器；开启 cluster-enabled 之后，Redis 服务器的身份就是 Redis Cluster 中的一员。

```conf
cluster-enabled true
```

