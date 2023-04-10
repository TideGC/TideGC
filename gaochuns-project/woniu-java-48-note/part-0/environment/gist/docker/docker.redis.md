---
alias: 
- "Docker Run Redis"
- "Redis Run in Docker"
tags: 
- docker-images
- docker-run
- redis
---

## Docker Run Redis 

docker hub 网址：[*https://hub.docker.com/_/redis*](https://hub.docker.com/_/redis)

当前<small>（2022-09-01）</small> latest / 7.0.4 / 7.0 / 7 是同一个版本。

### 第 0 步：关于 Redis 镜像和容器

- _数据存储目录_ — 容器内的 `/data`

> [!cite]- 关于数据存储目录
> Redis 默认开启的是 RDB 持久化，所以生成的 .rdb 文件就放在容器中的 _/data_ 目录下。
> 
> 另外，Redis 的 Dockerfile 通过 [[202208252105|VOLUME 指令]] 将 _/data_ 目录默认挂载到了宿主机的磁盘空间，你可以通过 [[202211021334|docker container inspect]] 命令查看默认挂载路径。

- _配置文件目录_ — `无`

> [!cite]- 关于配置文件目录
> Redis 的配置文件是启动时指定的，所以原则上配置文件可以在容器内任意。
> 
> 基于这一点，我们可以这样“规划”：将配置文件放在 _/etc/redis_ 目录下，且命名为 _redis.conf_ 文件。<small>当然，你有别的"规划"也可以，反正你怎么"规划"的，未来你就怎么用。</small>

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 _docker images_ 命令查看。

```bash
docker pull redis:6.2.1
```

### 第 2 步：docker run 运行容器

> [!cite]- 示例一：极简默认
> ```bash
> docker run -d --rm \
>     --name redis-6379 \
>     -p 6379:6379 \
>     redis:6.2.1
> ```

> [!cite]- 示例二：挂载宿主机目录<small>（以保留数据）</small>
> ```bash
> ## mkdir redis-6379-data
> 
> docker run -d --rm \
>     --name redis-6379  \
>     -v /etc/localtime:/etc/localtime:ro  \
>     -v $(pwd)/redis-6379-data:/data  \
>     -p 6379:6379 \
>     redis:6.2.1
> ```

> [!cite]- 示例三：挂载宿主机目录以及配置文件目录
> 这里利用了一个[[202302071558#Redis 默认配置文件|小技巧]]获得 Redis 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。
> ```bash
> docker run -d --rm \
>     --name redis-6379  \
>     -v /etc/localtime:/etc/localtime:ro  \
>     -v $(pwd)/redis-6379-data:/data  \
>     -v $(pwd)/redis.conf:/etc/redis.conf \
>     -p 6379:6379 \
>     redis:6.2.1 \
>     redis-server /etc/redis.conf
> ```
> 
> 这里和上面的示例有点特殊的是：在指定配置文件的情况下，要额外的多加一段命令，告知 redis-server 它要加载的配置文件。

> [!cite]- 示例四：挂载自建数据卷以及配置文件目录
> 
> > 了解
>
> 在上个示例的基础上，创建并使用了数据卷代替了宿主机目录。当然，数据卷本质上也是在数据集上的一个目录<small>（只不过在一个特定的位置）</small>。
> 
> ```bash
> ## 默认挂载数据目录：docker inspect --format='{{json .Mounts}}' redis-6379 | jq
> 
> ## docker volume rm redis-6379-ata
> ## docker volume create --name redis-6379-data
> 
> docker run -d --rm \ 
>     --name redis-6379 \ 
>     -v /etc/localtime:/etc/localtime:ro \ 
>     -v redis-6379-data:/data \ 
>     -v $(pwd)/redis.conf:/etc/redis.conf \ 
>     -p 6379:6379 \ 
>     redis:6.2.1 \ 
>     redis-server /etc/redis.conf
> ```

### 第 3 步：验证

```bash
## 查看容器的运行信息
docker ps

## 进入 redis-6379 容器
docker exec -it redis-6379 /bin/bash

## 执行 redis-cli 连接 redis server
redis-cli
```

### 其它

- [[202302071558#如何“弄到”默认配置文件#Redis 默认配置文件|《如何弄到 Redis 默认配置文件》]]

- 配置文件中需要改动的地方

  由于 Redis 默认只接受从 127.0.0.1 即，本机发起的连接请求<small>（即，bind 配置项）</small>。

  所以，我们需要放开这个设置，因此，我们要将 Redis 配置文件中的 bind 配置项的内容改成如下：

  ```bash
  bind 0.0.0.0
  ```

