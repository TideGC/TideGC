---
alias: 'RabbitMQ 在 CentOS Stream 上的安装'
tags: [centos-stream-install]
---

## RabbitMQ 在 CentOS Stream 上的安装

### 第 1 步：配置 rabbitmq-server 的仓库

执行以下 2 条 Shell 命令，配置 rabbitmq-server 的 repo 配置文件：

- 第 1 条 Shell 命令：

```sh
# curl -s https://packagecloud.io/install/repositories/rabbitmq/erlang/script.rpm.sh | sudo bash
cat > /etc/yum.repos.d/rabbitmq_erlang.repo << EOF
[rabbitmq_erlang]
name=rabbitmq_erlang
baseurl=https://packagecloud.io/rabbitmq/erlang/el/8/$basearch
repo_gpgcheck=1
gpgcheck=0
enabled=1
gpgkey=https://packagecloud.io/rabbitmq/erlang/gpgkey
sslverify=1
sslcacert=/etc/pki/tls/certs/ca-bundle.crt
metadata_expire=300

[rabbitmq_erlang-source]
name=rabbitmq_erlang-source
baseurl=https://packagecloud.io/rabbitmq/erlang/el/8/SRPMS
repo_gpgcheck=1
gpgcheck=0
enabled=1
gpgkey=https://packagecloud.io/rabbitmq/erlang/gpgkey
sslverify=1
sslcacert=/etc/pki/tls/certs/ca-bundle.crt
metadata_expire=300
EOF
```


- 第 2 条 Shell 命令：

```sh
# curl -s https://packagecloud.io/install/repositories/rabbitmq/rabbitmq-server/script.deb.sh | sudo bash
cat > /etc/yum.repos.d/rabbitmq_rabbitmq-server.repo << EOF
[rabbitmq_rabbitmq-server]
name=rabbitmq_rabbitmq-server
baseurl=https://packagecloud.io/rabbitmq/rabbitmq-server/el/8/$basearch
repo_gpgcheck=1
gpgcheck=0
enabled=1
gpgkey=https://packagecloud.io/rabbitmq/rabbitmq-server/gpgkey
sslverify=1
sslcacert=/etc/pki/tls/certs/ca-bundle.crt
metadata_expire=300

[rabbitmq_rabbitmq-server-source]
name=rabbitmq_rabbitmq-server-source
baseurl=https://packagecloud.io/rabbitmq/rabbitmq-server/el/8/SRPMS
repo_gpgcheck=1
gpgcheck=0
enabled=1
gpgkey=https://packagecloud.io/rabbitmq/rabbitmq-server/gpgkey
sslverify=1
sslcacert=/etc/pki/tls/certs/ca-bundle.crt
metadata_expire=300
EOF
```

### 第 2 步：安装

执行以下 Shell 命令进行安装：

```bash
# 更新 dnf 本地缓存
dnf makecache

# 安装 rabbitmq-server（会连带安装 erlang 环境）
dnf install -y rabbitmq-server

# 启用 rabbitm-server 管理后台
rabbitmq-plugins enable rabbitmq_management

## 在开发环境中，为了避免防火墙的干扰，我们通常会关闭 linux 防火墙
systemctl disable firewalld --now
setenforce 0

# 设置开机启动并立即启动 rabbitmq-server
systemctl enable rabbitmq-server --now
```

### 第 3 步：创建后台账号并登录验证

由于高版本的 rabbitmq-server 提高了安全性，所以，现在使用 guest/guest 只允许本地登录。

因此你想要在远程登录 15672 控制台就需要使用 guest 之外的的账号，且这个账号应该是"管理员"身份。

执行以下命令创建 root 账号，设置密码为 123456，并赋予它 administrator 权限。

```sh
# 创建新用户（ guest 用户只允许本地登录 ）
rabbitmqctl add_user root 123456

# 设置新账号 root 的权限
rabbitmqctl set_user_tags root administrator

# 重启 rabbitmq-server
systemctl restart rabbitmq-server
systemctl status rabbitmq-server
```

