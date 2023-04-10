---
alias: 
- Redis 在 CentOS Stream 下的卸载
tags: 
- linux
- redis 
---

## Redis 在 CentOS Stream 上的卸载

### 第 1 步：停止 Redis 服务

```sh
systemctl disable redis --now
```

### 第 2 步：卸载 Redis

```bash
dnf erase -y redis
```

### 第 3 步：删除 Redis 配置文件

dnf erase 命令在卸载 redis 时，会连带删除 /etc 下的 redis 的配置文件。

如果你想留下配置文件，那么当初就不要使用 dnf erase 命令，而是使用 dnf remove 命令来卸载 redis 。

### 第 4 步：删除 Redis 数据目录<small>（慎重、酌情考虑）</small>

dnf erase 命令在卸载 redis 时，并没有连带删除 redis 的数持久化目录。如果需要删除，你要亲自手动执行。

```sh
rm -rf /var/lib/redis
```