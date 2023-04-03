---
alias: 'Redis 在 CentOS Stream 上的安装'
---

## Redis 在 CentOS Stream 上的安装

CentOS Stream 的官方软件源中就有 Redis 。

### 第 1 步：查询、搜索

```bash
## 简单查询搜索
dnf search redis

## 查看详情
dnf info redis
```

对于初学者，你可以[[centos.redis.uninstall|卸载]]掉 redis 再重新安装来重新学习、熟悉安装过程的步骤。

### 第 2 步：安装

```bash
## 安装
dnf install -y redis
```

### 第 3 步：配置

默认情况下，redis 只允许从本地发起连接。所以，如果我们要从"别处"连接 redis server ，需要修改配置放开这个限制。

redis 的配置文件在 "/etc" 目录下，名为 "redis.conf" 。

```text
/etc
└── redis.conf
```

执行以下命令，间接修改 redis.conf 配置文件：

```bash
# Shell 命令
## 取消本地绑定。69 行附近
sed -i -e "s|bind 127.0.0.1|#bind 127.0.0.1|" /etc/redis.conf

## 关闭保护模式。88 行附近
sed -i -e "s|protected-mode yes|protected-mode no|" /etc/redis.conf
```


### 第 4 步：启动 redis

```bash
# Shell 命令

## 查看 redis 运行状态
systemctl status redis

## 在开发环境中，为了避免防火墙的干扰，我们通常会关闭 linux 防火墙
systemctl disable firewalld --now

## 将 redis 设置为开机启动，并立即启动
systemctl enable redis --now
```

### 第 5 步：从"别处"连接 redis server

在别处使用 redis-cli 命令向 redis server 发起连接并使用：

```bash
# Shell 命令
redis-cli -h <IP>
```

