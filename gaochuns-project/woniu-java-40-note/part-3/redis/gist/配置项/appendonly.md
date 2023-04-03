---
alias: appendonly 配置项
---

### appendonly 配置项

语法：

```
appendonly <yes|no>
```

appendonly 配置项用于 AOF 持久化功能，它是 AOF 持久化的开关项。默认值是 *no* 。

> [!danger] 警告
> 启用 AOF 持久化，记得需要将 RDB 持久化关掉。
