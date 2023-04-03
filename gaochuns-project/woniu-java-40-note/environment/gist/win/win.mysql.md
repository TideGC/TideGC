---
alias: MySQL 在 Windows 上的安装
tags: mysql install windows 
---

## MySQL 在 Windows 上的安装


> [!danger] 警告
> 解压路径不要有空格和中文！
> 
> 例如：**D:\\ProgramFiles** 就是很好的选择。

### 第 1 步：准备工作

- MySQL 的安装<small>（ 无论是安装版，还是解压版 ）</small>，都依赖于所安装的电脑上要安装 [Visual C++ Redistributable](https://docs.microsoft.com/zh-CN/cpp/windows/latest-supported-vc-redist)

- 在 D 盘下创建 ProgramFiles 目录。

### 第 2 步：下载解压

下载 _**mysql-8.0.26**_ ，略。

解压到 **D:\\ProgramFiles** 。

> [!info] 提示
> 后文的 **MYSQL_HOME** 指的就是 D:\\ProgramFiles\\mysql-8.0.26-winx64 。

### 第 3 步：添加缺省的配置文件

#### MYSQL_HOME/data 目录

在 MYSQL_HOME 下创建 data 目录；

#### MYSQL_HOME/my.ini 文件

在 MYSQL_HOME 下创建 my.ini 文件，并添加如下内容：

```ini
[mysqld]
port=3306
basedir=D:/ProgramFiles/mysql-8.0.26-winx64 
datadir=D:/ProgramFiles/mysql-8.0.26-winx64/data
max-connections=200
max-connect-errors=10
character-set-server=utf8mb4
collation-server=utf8mb4_bin
default-storage-engine=InnoDB
default-authentication-plugin=mysql_native_password

[client]
port=3306
default-character-set=utf8mb4

[mysql]
default-character-set=utf8mb4
```

### 第 4 步：初始化

以管理员身份打开 cmd 命令行，进入到 MYSQL_HOME/bin 目录下，执行以下命令，找到随机产生的密码：

```bash
mysqld --initialize --console
```

记住控制台上显示的初始密码，比如：`?ds:i+)4C/wo` 。另外，上面所说的 data 目录也会被自动创建。

### 第 5 步：启动 MySQL Server

进入到 MYSQL_HOME/bin 目录下，执行以下命令，启动 MySQL Server：

```bash
mysqld
```

> [!danger] 警告
> 执行的是 "**myslqd**" ，而不是 "mysql" 。它们两个是不同的东西，不要执行错了。

> [!tip] 补充
> 后续，我们会把 MySQL Server 注册成 Windows 的开机启动项，这样就避免了每次要使用 MySQL Server 都要启动一次的繁琐操作。

### 第 6 步：修改 root 默认密码

进入到 MYSQL_HOME/bin 目录下，执行以下 Shell 命令，连接 MySQL Server：

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p

<输入密码>
```

[[202208050820 1#8 0 26 版本修改密码|8.0.26 版本修改密码]]

### 第 7 步：注册成 Windows 服务

进入到 MYSQL_HOME/bin 目录下，执行以下 Shell 命令，将 MySQL Server 注册成 Windwos 的服务：

```bash
mysqld --install
```

另外，反向的卸载/删除操作中，使用以下 Shell 命令：

```bash
mysqld --remove
```

卸载前记得要停止 mysqld 。

除了在 Windows 的 "服务" 中手动开关 MySQL Server，你还可以使用以下 Shell 命令启停 MySQL Server：

```bash
## 启动
net start mysql

## 停止
net stop mysql
```

