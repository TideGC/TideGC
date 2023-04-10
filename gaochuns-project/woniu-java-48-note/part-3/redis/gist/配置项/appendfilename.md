---
alias: appendfilename 配置项
---

### appendfilename 配置项

AOF 持久化文件文件名，默认值是 *appendonly.aof* 。

语法

```
appendfilename <文件名>
```

AOF 文件的保存位置和 RDB 文件的位置相同，都是通过 *dir* 参数设置的。

一般开发环境中不会去动它。

