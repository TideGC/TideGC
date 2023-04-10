---
alias: 
- "Docker Run MySQL", 
- "MySQL Run in Docker"
tags: 
- docker-run
- docker-images
- mysql
---

## Docker Run MySQL

docker hub 地址：[https://hub.docker.com/\_/mysql](https://hub.docker.com/_/mysql)

当前<small>（2022-09-01）</small>：

- latest / 8.0.30 / 8.0 / 8 版本是同一个镜像；
- 5.7.39 / 5.7 / 5 是同一个镜像。

### 第 0 步：关于 MySQL 镜像和容器

以下目录路径都是容器中的目录

- _数据存储目录_ — 容器中的 `/var/lib/mysql`

> [!cite]- 关于数据存储目录
> MySQL 的 Dockerfile 通过[[202208252105|《VOLUME 指令》]]将容器内的 */var/lib/mysql/* 目录默认挂载到了宿主机磁盘空间，你可以通过 [[202209141442#查看容器的挂载卷|docker container inspect]] 命令查看默认挂载路径。

- _配置文件目录_ — 容器中的 `/etc/mysql`

> [!cite]- 关于配置文件目录
> 容器中的 _/etc/mysql/my.cnf_ 是 MySQL 的入口配置文件，在它的的最后又 include 了同级目录下的 conf.d 目录（即，_/etc/mysql/conf.d/_）下的更多的的 .cnf 配置文件。

- _必要环境变量_ — `MYSQL_ROOT_PASSWORD`

> [!cite]- 关于必要环境变量
> 环境变量 _MYSQL_ROOT_PASSWORD_ 用来指定 root 账号的登录密码。未指定时，docker run 命令会启动失败。
>  
> 另外，有一个非必要的、常见的环境变量 _MYSQL_ROOT_HOST_ 用来指定 root 账号的登录 host ，它的默认值就是“**%**”，也正是我们所需要的，所以一般就省略对它的设置。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 _docker images_ 命令查看。

```bash
docker pull mysql:8.0.26
```

### 第 2 步：docker run 命令运行

环境变量 **MYSQL_ROOT_PASSWORD** 是必须的，用于指定 root 账户的登录密码。

> [!cite]- 示例一：极简默认
> 
> ```bash
> docker run -d --rm \
>     --name mysql-3306 \
>     -e MYSQL_ROOT_PASSWORD=123456 \
>     -p 3306:3306 \
>     mysql:8.0.26
> ```

> [!cite]- 示例二：挂载宿主机目录<small>（用以保存数据文件）</small>
> ```bash
> mkdir mysql-3306-data
> 
> docker run -d --rm \
>     --name mysql-3306 \
>     -e MYSQL_ROOT_PASSWORD=123456 \
>     -p 3306:3306 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v $(pwd)/mysql-3306-data:/var/lib/mysql \
>     mysql:8.0.26
> ```

> [!cite]- 示例三：挂载宿主机数据目录以及配置文件目录
> 
> 在上个示例的基础上，这里利用了一个[[202302071558#MySQL 默认配置文件|小技巧]]获得 MySQL 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。
> 
> ```bash
> mkdir mysql-3306-data
> 
> docker run -d --rm \
>     --name mysql-3306 \
>     -e MYSQL_ROOT_PASSWORD=123456 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v $(pwd)/mysql-3306-data:/var/lib/mysql \
>     -v $(pwd)/mysql.conf.d:/etc/mysql \
>     -p 3306:3306 \
>     mysql:8.0.26
> ```

> [!cite]- 示例四：挂载自建数据卷以及配置文件目录
> 
> > 了解 
> 
> 在上个示例的基础上，创建并使用了数据卷代替了宿主机目录。当然，数据卷本质上也是在宿主机上的一个目录<small>（只不过在一个特定的位置）</small>。
> 
> ```bash
> ## 默认挂载数据目录：docker inspect --format='{{json .Mounts}}' mysql-3306 | jq
> 
> ## docker volume rm mysql-3306-data
> ## docker volume create --name mysql-3306-data
> 
> docker run -d --rm \
>     --name mysql-3306 \
>     -e MYSQL_ROOT_PASSWORD=123456 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v mysql-3306-data:/var/lib/mysql \
>     -v $(pwd)/mysql.conf.d:/etc/mysql \
>     -p 3306:3306 \
>     mysql:8.0.26
> ```

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

- [[202302071558#MySQL 默认配置文件|《小技巧：如何弄到 MySQL 默认配置文件》]]
- [[202302071728|《MySQL 容器的一个附加功能：初始化数据库》]]
