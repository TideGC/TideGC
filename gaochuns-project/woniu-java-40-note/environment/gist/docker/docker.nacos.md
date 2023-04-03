---
alias: ["Docker Run Nacos", "Nacos Run in Docker"]
tags: [docker-images, docker-run, nacos]
---

# Docker Run Nacos 

docker hub 网址：[*https://hub.docker.com/r/nacos/nacos-server*](https://hub.docker.com/r/nacos/nacos-server)

当前<small>（ 2022-03-10 ）</small> latest / 2.0.4 是同一个版本。但是中央仓库还没有 2.0.4 这个 tag ，所以，你要么就下载 latest 版本<small>（ 即，2.0.4 ）</small>，要么就下载 2.0.3 版本。

## 第 0 步：关于 Nacos 镜像和容器

### 数据存储目录：容器中的 "/home/nacos/data" 

nacos 中的内置 derby 数据库的数据文件就存在这个目录下。

### 配置文件目录：无

Nacos Server 没有配置文件，对于 Nacos Server 的配置都是通过其环境变量<small>（ Environment ）</small> 来配置的。

具体有哪些环境变量见 [Nacos 的 Dockerhub 官方主页](https://hub.docker.com/r/nacos/nacos-server) 。

### 必要环境变量：MODE

docker-nacos 要求容器运行时必须要有环境变量 **MODE** 。

运行 nacos 需要指定它的运行模式。例如，开发环境中我们使用 standalone 模式运行。

## 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有，可通过 docker images 命令查看。

```bash
docker pull nacos/nacos-server:2.0.3
```

## 第 2 步：docker run 运行容器

### 示例一：简单示例

```bash
# 未挂载「数据存储目录」
docker run -d --rm \
	--name nacos-8848 \
	-e MODE=standalone \
	-v /etc/localtime:/etc/localtime:ro \
	-p 8848:8848 \
	nacos/nacos-server:2.0.3
```

### 示例二：使用数据卷

```bash
# 数据卷
docker volume rm nacos-8848-data
docker volume create --name nacos-8848

docker run -d --rm \
	--name nacos-8848  \
	-e MODE=standalone  \
	-v /etc/localtime:/etc/localtime:ro  \
	-v nacos-8848-data:/home/nacos  \
	-p 8848:8848 \
	nacos/nacos-server:2.0.3
```


## 第 3 步：验证

```bash
# 查看容器的运行信息
docker ps
```

访问 `http://<宿主机IP>:8848/nacos` 能看到 nacos 的管理界面，使用 nacos / nacos 可登录。

