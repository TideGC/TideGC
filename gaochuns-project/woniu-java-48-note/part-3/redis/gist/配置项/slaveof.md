---
alias: 
- slaveof 配置项
tags: 
- redis
- 配置
---

## slaveof 配置项

语法：

```ini
slaveof <IP> <端口>
```

slaveof 配置用来指定当前 Redis<small>（从机身份）</small>的主机的 IP 和端口。例如：

```conf
slaveof 150.158.196.179 6379
```

