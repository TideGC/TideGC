### cluster-config-file 配置项

#redis #redis-cluster #cluster-config-file #配置项 

集群中的每一个节点都需要有一个文件去记录群集中其他节点的状态，持久变量等等相关信息，并且随着集群节点的状态的变化，会持续更新这个文件，以便能够在启动时重新读取它。

cluster-config-file 就是用来指定这个文件的文件路径名的。例如：

```conf
cluster-config-file /data/nodes.6379.conf
```

cluster-config-file 的默认值是 `nodes.conf` 。

