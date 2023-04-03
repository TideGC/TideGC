---
alias: "MySQL 在 CentOS Stream 下的卸载"
tag: linux mysql
---

## MySQL Server 在 CentOS Stream 上的卸载

### 第 1 步：停止 MySQL Server 服务

```bash
systemctl disable mysqld --now
```

### 第 2 步：卸载 MySQL Server

```bash
dnf erase -y mysql-server
```

### 第 3 步：删除 MySQL Server 的配置文件

dnf erase 在卸载 MySQL Sever 时，并没有连带删除 mysql-server 的配置文件，<small>猜测可能是 mysql-server 故意如此</small>。

如果想要删除，需要手动执行下面命令：

```bash
rm -rf my.cnf*
```

### 第 4 步：清空 MySQL Server 的数据目录<small>（慎重、酌情考虑）</small>

dnf erase 命令在卸载 MySQL Server 时，并没有连带删除 MySQL Server 的数持久化目录，<small>猜测可能是 mysql-server 故意如此</small>。

如果需要删除，你要亲自手动执行下面命令：

```sh
rm -rf /var/lib/mysql/*
```

