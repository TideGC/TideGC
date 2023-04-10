---
alias: 
- "Docker Run Nacos"
- "Nacos Run in Docker"
tags: 
- docker-images
- docker-run
- nacos
---

## Docker Run Nacos 

docker hub 网址：[*https://hub.docker.com/r/nacos/nacos-server*](https://hub.docker.com/r/nacos/nacos-server)

当前<small>（2022-03-10）</small> latest / 2.0.4 是同一个版本。但是中央仓库还没有 2.0.4 这个 tag ，所以，你要么就下载 latest 版本<small>（即，2.0.4）</small>，要么就下载 2.0.3 版本。

### 第 0 步：关于 Nacos 镜像和容器

- _数据存储目录_ — 容器中的 `/home/nacos/data`

> [!cite]- 关于数据存储目录
> nacos 中的内置 derby 数据库的数据文件就存在这个目录下。

- _配置文件目录_ — 无

> [!cite]- 关于配置文件目录
> Nacos Server 没有配置文件，对于 Nacos Server 的配置都是通过其环境变量<small>（Environment）</small> 来配置的。
> 
> 具体有哪些环境变量见 [Nacos 的 Dockerhub 官方主页](https://hub.docker.com/r/nacos/nacos-server) 。

- _必要环境变量_ — `MODE`

> [!cite]- 关于环境变量
> docker-nacos 要求容器运行时必须要有环境变量 **MODE** 。
> 
> 运行 nacos 需要指定它的运行模式。例如，开发环境中我们使用 _standalone_ 模式运行。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有，可通过 _docker images_ 命令查看。

```bash
docker pull nacos/nacos-server:1.4.2
```

### 第 2 步：docker run 运行容器

> [!cite]- 示例一：简单示例
> ```bash
> docker run -d --rm \
>     --name nacos-8848 \
>     -e MODE=standalone \
>     -p 8848:8848 \
>     nacos/nacos-server:1.4.2
> ```

> [!cite]- 示例二：挂载宿主机目录（以保留数据）
> 
> ```bash
> ## mkdir nacos-8848-data
> 
> docker run -d --rm  \
>   --name nacos-8848  \
>   -e MODE=standalone  \
>   -v /etc/localtime:/etc/localtime:ro  \
>   -v $(pwd)/nacos-8848-data:/home/nacos/data  \
>   -p 8848:8848  \
>   nacos/nacos-server:1.4.2
> ```

> [!cite]- 示例三：挂载自建数据卷
> > 了解
> 
> 在上个示例的基础上，创建并使用了数据卷代替了宿主机目录。当然，数据卷本质上也是在数据集上的一个目录（只不过在一个特定的位置）。
> ```bash
> ## 数据卷
> docker volume rm nacos-8848-data
> docker volume create --name nacos-8848
> 
> docker run -d --rm  \
>   --name nacos-8848  \
>   -e MODE=standalone  \
>   -v /etc/localtime:/etc/localtime:ro  \
>   -v nacos-8848-data:/home/nacos/data  \
>   -p 8848:8848  \
>   nacos/nacos-server:1.4.2
> ```

### 第 3 步：验证

```bash
## 查看容器的运行信息
docker ps
```

访问 `http://<宿主机IP>:8848/nacos` 能看到 nacos 的管理界面，使用 nacos/nacos 可登录。

