---
alias: 'MySQL 在 CentOS Stream 上的安装'
---

## MySQL 在 CentOS Stream 上的安装

CentOS Stream 的默认软件源<small>（AppStream）</small>中有 mysql-server 。

### 第 1 步：查询

你可以通过如下 Shell 命令查看到它们：

```bash
## 查询
dnf search mysql-server

## 查看详情
dnf info mysql-server
```

对于初学者，你可以[[centos.mysql.uninstall|卸载]]掉 mysql 再重新安装来学习、熟悉安装过程的步骤。

### 第 2 步：安装

执行如下 Shell 命令进行安装：

```bash
## 安装
dnf install -y mysql-server
```

### 第 3 步：设置开机启动

执行如下 Shell 命令将 MySQL Server 设置为开机启动：

```sh
# Shell 命令

## 查看 mysqld 运行状态
systemctl status mysqld 

## 在开发环境中，为了避免防火墙的干扰，我们通常会关闭 linux 防火墙
systemctl disable firewalld --now
setenforce 0

## 将 mysqld 设置为开机启动，并立即启动
systemctl enable mysqld --now
```

### 第 4 步：查看 root 初始密码

- [[202208050820 1#8 0 26 版本初始密码|8.0.26 版本查看初始密码]]

- [[202208050820 1#8 0 27 版本初始密码|8.0.27 版本查看修改密码]]

### 第 5 步：修改默认 root 密码

使用 root 初始密码连接到 mysql-server，例如：

```bash
mysql -u root -p
Enter password: <此处直接回车>
```

- [[202208050820 1#8 0 26 版本修改密码|8.0.26 版本修改密码]]

- [[202208050820 1#8 0 27 版本修改密码|8.0.27 版本修改密码]]

验证：退出后使用新密码登录。

### 附加操作

- 8.0.26 无安全性要求

- [[202208050843 1|8.0.27 降低密码安全性要求]]

