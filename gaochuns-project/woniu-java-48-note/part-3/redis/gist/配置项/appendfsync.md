### appendfsync 配置项

#redis #appendfsync #配置项 

语法：

```ini
appendfsync <always|everysec|no>
```

appendfsync 配置项用于控制 AOF 持久化刷盘的频次。

| 值 | 说明 |
| :- | :- |
| everysec | 默认值。写命令执行完先放入 AOF 缓冲区，<br>然后表示每隔 1 秒将缓冲区数据写到 AOF 文件。|
| always | 表示每执行一次写命令，立即记录到 AOF 文件。 |
| no | 写命令执行完先放入 AOF 缓冲区，<br>由操作系统决定何时将缓冲区内容写回磁盘。|
刷盘频率越低，性能越好，可靠性越差。