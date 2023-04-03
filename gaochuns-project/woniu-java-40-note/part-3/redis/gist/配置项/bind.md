---
alias: bind 配置项
tags: redis, 配置
---

## bind 配置项

语法：

```
bind <ip>
```

Redis Server 可以控制只接受从某个 IP 上发起的连接，靠的就是 **bind** 配置。例如：

```conf
bind 127.0.0.1
```

这样的话你就只能从 Redis Server 所在的那台服务器，向 Redis 发起连接，并使用它。

bind 属性有个特殊值：

```conf
bind 0.0.0.0
```

这就表示不限客户端所在 IP，任何人都能向它发起连接，并使用它。

