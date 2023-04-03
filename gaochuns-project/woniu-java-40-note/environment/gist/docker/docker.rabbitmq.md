---
alias: ["Docker Run RabbitMQ", "RabbitMQ Run in Docker"] 
tags: [docker-images, docker-run, rabbitmq]
---

## Docker Run RabbitMQ 


docker hub 上的网址：[*https://hub.docker.com/_/rabbitmq*](https://hub.docker.com/_/rabbitmq)

当前<small>（ 2022-05-23 ）</small>的 latest / 3.10 / 3.10.2 是同一个镜像 。

> [!important] 注意
> 不过，我们选择带有 **management** 的版本的镜像，因为其中包含 web 管理页面<small>（ 15672 ）</small>。例如：**rabbitmq:3.10.2-management** 。

### 第 0 步：关于 RabbitMQ 镜像和容器

#### 数据存储目录：容器中的 "/var/lib/rabbitmq" 

这个目录是 rabbitmq 在做持久化时所使用的目录。

> 其实 "/var/lib/rabbitmq" 这个说法不严谨，并且未来会引起一个 "bug" 。

确切地说，数据存储目录具体是在 /var/lib/rabbitmq 下的某一个目录下。具体位置取决于 _**docker run**_ 命令中的 _**--hostname**_ 属性的属性值的值。

如果你在 docker run 命令中指定了 _**--hostname**_ ，例如

```
--hostname=rabbitmq-5672 
```

那么具体的目录就是 /var/lib/rabbitmq 下的 mnesia/rabbit@rabbitmq-5672  即，

```
/var/lib/rabbitmq/mnesia/rabbit@rabbitmq-5672
```

如果没有指定 --hostname ，那么，这里就是一个随机值，例如

```
/var/lib/rabbitmq/mnesia/rabbit@12e22c2be88d 。
```

> [!attention] 注意
> 在使用 docker run 时，最好是加上 --hostname 选项，因为这对未来的 RabbitMQ 的持久化有影响。

#### 配置文件目录：容器中的 "/etc/rabbitmq"

其中有配置文件 rabbitmq.config 。

> 现在又变了，变成了 /etc/rabbitmq/conf.d/10-defaults.conf ...


### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 docker images 命令查看。

```sh
docker pull rabbitmq:3.10-management
```


### 第 2 步：docker run 运行容器

#### 示例一：简单示例

```bash
## 未挂载「数据存储目录」和「插件目录」
docker run -d --rm \
    --name rabbitmq-5672 \
    --hostname rabbitmq-5672 \
    -v /etc/localtime:/etc/localtime:ro \
    -p 5672:5672 \
    -p 15672:15672 \
    rabbitmq:3.10-management
```

#### 示例二：数据卷示例和外部配置文件

这里利用了一个[[docker.rabbitmq#Docker Run RabbitMQ#小技巧：如何 弄到 默认配置文件|小技巧]]获得 RabbitMQ 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```bash
## 使用数据卷
docker volume rm rabbitmq-5672-data
docker volume create --name rabbitmq-5672-data

docker run -d --rm \
    --name rabbitmq-5672 \
    --hostname rabbitmq-5672 \
    -v /etc/localtime:/etc/localtime:ro \
    -v rabbitmq-5672-data:/var/lib/rabbitmq \
    -v $(pwd)/rabbitmq.config.d:/etc/rabbitmq \
    -p 5672:5672 \
    -p 15672:15672 \
    rabbitmq:3.10-management
```

### 第 3 步：验证

查看容器的运行信息

```bash
docker ps
```

访问 management web 管理系统<small>（ 15672 ）</small>。

由于我们使用的是 management 镜像创建的容器，因此，容器自带了后台管理系统。我们可以通过我们映射的 **15672** 端口访问。

访问网址 `http://宿主机IP:15672` ，使用 `guest / guest` 登录。<small>有一点点延迟，启动容器后稍等片刻。</small>

> [!info] 提示
> 虽然用户名是叫 **guest** ，但是它的角色是管理员 administrator 。

### 其它

#### 小技巧：如何"弄到"默认配置文件

> 如果你不准备"动"默认配置，那么以下内容就用不上。

执行以下 Shell 命令片段，能在"当前目录"下得到一个名为 "rabbitmq.config.d" 的目录，其中有 docker-rabbitmq 的默认配置文件<small>（ 多个 ）</small>

```sh
docker run -d --rm --name rabbitmq-temp rabbitmq:3.10-management
docker cp rabbitmq-temp:/etc/rabbitmq .
docker rm -f rabbitmq-temp
mv rabbitmq rabbitmq.config.d
```

原理概述：

- 通过 docker run 命令启动了一个 ngix 容器：nginx-temp；
- 通过 docker cp 命令将 nginx-temp 容器中的 /etc/nginx 目录整个拷贝了出来，拷贝到了宿主机的当前目录下；
- nginx-temp 容器的"使命"完成，将它删除；
- 将拷贝出来的 nginx 目录重命名为 nginx.config.d 。

> 如果有需要、或不满意的地方，你可以自己动手微调一下这几条命令。
