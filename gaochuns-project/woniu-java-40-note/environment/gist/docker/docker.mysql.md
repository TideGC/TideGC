---
alias: ["Docker Run MySQL", "MySQL Run in Docker"]
tags: [docker-run, docker-images,  mysql]
---

## Docker Run MySQL

docker hub 地址：[https://hub.docker.com/\_/mysql](https://hub.docker.com/_/mysql)

当前<small>（ 2022-09-01 ）</small>：

- latest / 8.0.30 / 8.0 / 8 版本是同一个镜像；
- 5.7.39 / 5.7 / 5 是同一个镜像。

### 第 0 步：关于 MySQL 镜像和容器

以下目录路径都是容器中的目录

| 目录 | 位置 |
| -: | :- |
| **数据存储目录** | /var/lib/mysql |
| **配置文件目录** | /etc/mysql |
| **必要环境变量** | MYSQL_ROOT_PASSWORD | 

关于以上目录的说明：

- MySQL 的 Dockerfile 通过 [[202208252105|VOLUME 指令]] 将容器内的 "/var/lib/mysql" 目录默认挂载到了宿主机磁盘空间，你可以通过 [[202209141442#查看容器的挂载卷|docker container inspect]] 命令查看默认挂载路径。

- "/etc/mysql" 下的 my.cnf 是 MySQL 的入口配置文件，在 my.cnf 的最后它又 include 了同级目录下的 conf.d 目录。/etc/mysql/conf.d 下可以有更多的 .cnf 配置文件。

- 环境变量 ***MYSQL_ROOT_PASSWORD*** 用来指定 root 账号的登录密码。未指定时，docker run 命令会启动失败。

- 另外，有一个非必要的、常见的环境变量 ***MYSQL_ROOT_HOST*** 用来指定 root 账号的登录 host ，它的默认值就是 **%** ，所以一般可省略。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 docker images 命令查看。

```bash
docker pull mysql:8.0.26
```

### 第 2 步：docker run 命令运行

环境变量 **MYSQL_ROOT_PASSWORD** 是必须的，用于指定 root 账户的登录密码。

#### 示例一：默认数据卷和默认配置

```bash
## 默认挂载数据目录：docker inspect --format='{{json .Mounts}}' mysql-3306 | jq
docker run -d --rm \
	--name mysql-3306 \
    -e MYSQL_ROOT_PASSWORD=123456 \
    -v /etc/localtime:/etc/localtime:ro \
    -p 3306:3306 \
    mysql:8.0.26
```

#### 示例二：数据卷和外部配置文件

这里利用了一个[[docker.mysql#小技巧：如何 弄到 默认配置文件|小技巧]]获得 MySQL 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```bash
## 使用数据卷和外部配置文件

## docker volume rm mysql-3306-data
## docker volume create --name mysql-3306-data

docker run -d --rm \
	--name mysql-3306 \
    -e MYSQL_ROOT_PASSWORD=123456 \
    -v /etc/localtime:/etc/localtime:ro \
    -v mysql-3306-data:/var/lib/mysql \
    -v $(pwd)/mysql.config.d:/etc/mysql \
    -p 3306:3306 \
    mysql:8.0.26
```

### 第 3 步：验证

```bash
## 查看容器的运行信息
docker ps

## 进入 mysql-3306 容器
docker exec -it mysql-3306 /bin/bash

## 执行 mysql-cli 的连接命令
mysql -h 127.0.0.1 -P 3306 -u root -p
```

### 其它

#### 小技巧：如何"弄到"默认配置文件

> 如果你不准备"动"默认配置，那么以下内容就用不上。

执行以下 Shell 命令片段，能在"当前目录"下得到一个名为 "mysql.config.d" 的目录，其中有 docker-mysql 的默认配置文件<small>（ 多个 ）</small>

```sh
docker run -d --rm --name mysql-temp -e MYSQL_ROOT_PASSWORD=123456 mysql:8.0.26
    
docker cp mysql-temp:/etc/mysql .

docker rm -f mysql-temp

mv mysql mysql.conf.d
```

原理概述：

- 通过 docker run 命令启动了一个 mysql 容器：mysql-temp；
- 通过 docker cp 命令将 mysql-temp 容器中的 /etc/mysql 目录整个拷贝了出来，拷贝到了宿主机的当前目录下；
- mysql-temp 容器的"使命"完成，将它删除；
- 将拷贝出来的 mysql 目录重命名为 mysql.conf.d 。

> 如果有需要、或不满意的地方，你可以自己动手微调一下这几条命令。

#### 附加功能：初始化数据库

mysql 的 docker 镜像有一个功能：它在第一次创建容器<small>（ 重启不算 ）</small>时，会到容器内的 ***/docker-entrypoint-initdb.d*** 目录下查看有没有 sql 脚本，如果有，就执行 sql 脚本。因此，你可以通过这个功能来完成数据库的创建等初始化工作。

你可以在某个目录下创建 .sql 脚本，并在其中写上建库、建表等 SQL 语句，然后将这个目录映射成 ***/docker-entrypoint-initdb.d*** 。

> [!danger] 警告
> 使用 Navicat 的同学注意：通过 Navicat 导出的数据库脚本中没有建库（ create database ）部分！所以，直接扔到上述目录下，mysql 启动会失败，因为无法执行 SQL 脚本。


