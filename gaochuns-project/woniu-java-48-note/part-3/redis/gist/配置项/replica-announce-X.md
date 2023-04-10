---
alias: replica-announce-X 配置项
tags: 
- redis
- 配置
- replica-announce-ip
- replica-announce-port
---

## replica-announce-X 配置项

语法：

```
replica-announce-ip <IP>
replica-announce-port <PORT>
```

在 Redis 主从关系中，从机要向主机上报自己的 IP 和端口。这就是为什么我们在主机中执行 `info replication` 命令能看到其从机信息的原因。

默认情况下，Redis 可以自己查询到自己所在服务器的 IP 和端口，但是在某些环境中，服务器不止有一个物理 IP<small>（和虚拟IP）</small>，这种情况下，为了避免 Redis 上报错了 IP 和端口，通常我们干脆会在配置信息中将当前 Redis 所使用的 IP 和端口明确指出来。

这就是 replica-announce-ip 和  replica-announce-port 的作用。例如：

```conf
replica-announce-ip 150.158.196.179
replica-announce-port 6379
```
