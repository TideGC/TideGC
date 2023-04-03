### sentinel monitor 配置项

#redis #redis-sentinel #monitor #配置项 

用于配置初始 master 的 IP 和端口。

通过它，当前哨兵就能知道主从节点中的主节点是哪个。哨兵一旦连接到主节点之后，可以从主节点处查询获悉其从节点的节点信息。

语法如下：

```ini
sentinel monitor <自定义master名> <主机IP> <主机端口> <投票数>
```

例如：

``` conf
sentinel monitor mymaster 150.158.196.179 6379 1
```

