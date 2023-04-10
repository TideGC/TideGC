---
alias: 
- cluster-node-timeout 配置项
tags: 
- redis
- redis-cluster
- cluster-node-timeout
---

## cluster-node-timeout 配置项

集群超时时间<small>（毫秒）</small>，节点超时多久则认为它宕机了。默认值是 `15000` 。

如果主节点超过指定的时间不可达，进行故障切换，将其对应的从节点提升为主。

注意：每个"无法在指定时间内到达大多数主节点"的节点将停止接受查询。