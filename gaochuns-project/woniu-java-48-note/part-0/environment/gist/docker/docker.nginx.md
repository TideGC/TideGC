---
alias: 
- "Docker Run Nginx", 
- "Nginx Run in Docker"
tags: 
- docker-images
- docker-run
- nginx
---

## Docker Run Nginx

docker hub 网址：[*https://hub.docker.com/\_/nginx*](https://hub.docker.com/_/nginx) 。

当前<small>（2022-09-01）</small>：

- latest / 1.23.1 / 1.23 是同一个版本；
- stable / 1.22.0 / 1.22 是同一个版本。

### 第 0 步：关于 Nginx 镜像和容器

- _数据存储目录_ — 容器中的 `/usr/share/nginx`

> [!cite]- 关于数据存储目录
> Nginx 所展示的静态资源就是放在容器中的 _/usr/share/nginx_ 目录下的。即，Nginx 回复给浏览器、所展示的内容都是在 _/usr/share/nginx_ 下面找到的内容。 

- _配置文件目录_ — 容器中的 `/etc/nginx`

> [!cite]- 关于配置文件目录
> 容器中的 _/etc/nginx/nginx.conf_ 文件是 Nginx 的入口配置文件；在它的最后又 include 了同级目录下的 _conf.d_ 目录（即，_/etc/nginx/conf.d_ 目录）下的更多的 .conf 配置文件。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有，可通过 _docker images_ 命令查看。

```bash
docker pull nginx:stable
```

### 第 2 步：docker run 运行容器

> [!cite]- 示例一：极简默认
> 
> ```bash
> docker run -d --rm  \
>     --name nginx-80  \
>     -p 80:80  \
>     nginx:stable
> ```

> [!cite]- 示例二：挂载宿主机目录<small>（以提供静态资源）</small>
> ```bash
> # mkdir nginx-80-html
> 
> docker run -d --rm  \
>     --name nginx-80  \
>     -v $(pwd)/nginx-80-html:/usr/share/nginx/html \
>     -p 80:80  \
>     nginx:stable
> ```

> [!cite]- 示例三：挂载宿主机目录以及配置文件目录
> 在上个示例的基础上，这里利用了一个[[202302071558#Nginx 默认配置文件|小技巧]]获得 Nginx 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。
> ```bash
> # mkdir nginx-80-html
> 
> docker run -d --rm  \
>     --name nginx-80  \
>     -v $(pwd)/nginx-80-html:/usr/share/nginx/html \
>     -v $(pwd)/nginx.conf.d:/etc/nginx/conf.d \
>     -p 80:80  \
>     nginx:stable
> ```
> 
> 
> 

> [!cite]- 示例四：挂载自建数据卷以及配置文件目录
> 
> > 了解 
> 
> 在上个示例的基础上，创建并使用了数据卷代替了宿主机目录。当然，数据卷本质上也是在宿主机上的一个目录<small>（只不过在一个特定的位置）</small>。
> 
> ```bash
> ## docker volume rm nginx-80-html nginx-80-log
> ## docker volume create --name nginx-80-html
> ## docker volume create --name nginx-80-log
> 
> docker run -d --rm \
>     --name nginx-80 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v nginx-80-html:/usr/share/nginx/html \
>     -v nginx-80-log:/var/log/nginx \
>     -v $(pwd)/nginx.config.d:/etc/nginx/conf.d \
>     -p 80:80 \
>     nginx:stable
> ```

### 第 3 步：验证

```bash
# 通过 docker ps 命令查看容器的运行信息：
docker ps
```

启动容器后，访问 `http://宿主机IP:80`

能看到 Nginx 的欢迎界面： Welcome to nginx!

### 其它

- [[202302071558#小技巧：如何“弄到” Nginx 默认配置文件|《小技巧：如何“弄到” Nginx 默认配置文件》]]



