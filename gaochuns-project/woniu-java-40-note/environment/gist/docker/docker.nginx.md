---
alias: ["Docker Run Nginx", "Nginx Run in Docker"]
tags: [docker-images,  docker-run, nginx]
---

## Docker Run Nginx

docker hub 网址：[*https://hub.docker.com/\_/nginx*](https://hub.docker.com/_/nginx) 。

当前<small>（ 2022-09-01 ）</small> ：

- latest / 1.23.1 / 1.23 是同一个版本；
- stable / 1.22.0 / 1.22 是同一个版本。

### 第 0 步：关于 Nginx 镜像和容器

#### 数据存储目录：容器中的 "/usr/share/nginx" 

Nginx 所展示的静态资源就是放在容器中的这个目录下的。

#### 配置文件目录：容器中的 "/etc/nginx" 

- 其下的 nginx.conf 是入口配置文件；

- 在 nginx.conf 的最后它又 include 了同级目录下的 conf.d 目录；

- /etc/nginx/conf.d 下可以有更多的 .conf 配置文件。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有，可通过 docker images 命令查看。

```bash
docker pull nginx:stable
```


### 第 2 步：docker run 运行容器

#### 示例一：简单示例

```bash
docker run -d --rm \
	--name nginx-80 \
	-v /etc/localtime:/etc/localtime:ro \
	-p 80:80 \
	nginx:stable
```

#### 方式二：数据卷示例

这里利用了一个[[docker.nginx#小技巧：如何 弄到 默认配置文件|小技巧]]获得 Nginx 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```bash
# docker volume rm nginx-80-html nginx-80-log
# docker volume create --name nginx-80-html
# docker volume create --name nginx-80-log
docker run -d --rm \
	--name nginx-80 \
	-v /etc/localtime:/etc/localtime:ro \
	-v nginx-80-html:/usr/share/nginx/html \
	-v nginx-80-log:/var/log/nginx \
	-v $(pwd)/nginx.config.d:/etc/nginx/conf.d \
	-p 80:80 \
	nginx:stable
```

### 第 3 步：验证

```bash
# 通过 docker ps 命令查看容器的运行信息：
docker ps
```

启动容器后，访问 `http://宿主机IP:80`

能看到 Nginx 的欢迎界面： Welcome to nginx!

### 其它

#### 小技巧：如何"弄到"默认配置文件

> 如果你不准备"动"默认配置，那么以下内容就用不上。

执行以下 Shell 命令片段，能在"当前目录"下得到一个名为 "nginx.config.d" 的目录，其中有 docker-nginx 的默认配置文件<small>（ 多个 ）</small>

```sh
docker run -d --rm --name nginx-temp nginx:stable
docker cp nginx-temp:/etc/nginx .
docker rm -f nginx-temp
mv nginx nginx.config.d
```

原理概述：

- 通过 docker run 命令启动了一个 ngix 容器：nginx-temp；
- 通过 docker cp 命令将 nginx-temp 容器中的 /etc/nginx 目录整个拷贝了出来，拷贝到了宿主机的当前目录下；
- nginx-temp 容器的"使命"完成，将它删除；
- 将拷贝出来的 nginx 目录重命名为 nginx.config.d 。

> 如果有需要、或不满意的地方，你可以自己动手微调一下这几条命令。