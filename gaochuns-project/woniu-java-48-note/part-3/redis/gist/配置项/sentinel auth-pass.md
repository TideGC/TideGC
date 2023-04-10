---
alias: sentinel auth-pass 配置项
tags: 
- edis
- redis-sentinel
- auth-pass
- 配置项
---

## sentinel auth-pass 配置项

如果 master 设置了登录密码，那么就需要在哨兵配置中指明用于连接的密码，否则，哨兵是无法连上 master 的。

语法如下：

```ini
sentinel auth-pass <master名> <密码>
```

这里记得要加上密码设置：

```conf
sentinel auth-pass mymaster 123456
```
