---
alias: 
- "Docker Run RabbitMQ"
- "RabbitMQ Run in Docker"
tags: 
- docker-images
- docker-run
- rabbitmq
---

## Docker Run RabbitMQ 

docker hub 上的网址：[*https://hub.docker.com/_/rabbitmq*](https://hub.docker.com/_/rabbitmq)

当前<small>（2022-05-23）</small>的 latest / 3.10 / 3.10.2 是同一个镜像 。

> [!important] 注意
> 不过，我们选择带有 **management** 的版本的镜像，因为其中包含 web 管理页面<small>（15672）</small>。例如：**rabbitmq:3.10.2-management** 。

### 第 0 步：关于 RabbitMQ 镜像和容器

- _数据存储目录_ — 容器中的 `/var/lib/rabbitmq`

> [!cite]- 关于数据存储目录
> 于数据存储目录 _/var/lib/rabbitmq_ 是 rabbitmq 在做持久化时所使用的目录。
> 
> > 其实 _/var/lib/rabbitmq_ 这个说法不严谨，并且未来会引起一个 bug 。
> 
> 确切地说，数据存储目录具体是在 _/var/lib/rabbitmq 下的某一个目录下_。具体位置取决于 _docker run_ 命令中的 `--hostname` 属性的属性值的值。
> 
> 如果你在 docker run 命令中指定了 `--hostname` ，例如
> 
> ```
> --hostname=rabbitmq-5672 
> ```
> 
> 那么具体的目录就是 /var/lib/rabbitmq 下的 _mnesia/rabbit@rabbitmq-5672_  即，
> 
> ```
> /var/lib/rabbitmq/mnesia/rabbit@rabbitmq-5672
> ```
> 
> 如果没有指定 `--hostname` ，那么，这里就是一个随机值，例如
> 
> ```
> /var/lib/rabbitmq/mnesia/rabbit@12e22c2be88d 。
> ```
> 
> > [!attention] 注意
> > 在使用 _docker run_ 时，最好是加上 _--hostname_ 选项，因为这对未来的 RabbitMQ 的持久化有影响。

- _配置文件目录_ — 容器中的 `/etc/rabbitmq`

> [!cite]- 关于配置文件目录
> 配置文件目录 _/etc/rabbitmq_ 中有配置文件 _rabbitmq.config_ 。
> 
> 以前是这样的…
> 
> 现在变了，变成了 _/etc/rabbitmq/conf.d/10-defaults.conf_ 了 …

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 _docker images_ 命令查看。

```sh
docker pull rabbitmq:3.10-management
```

### 第 2 步：docker run 运行容器

> [!cite]- 示例一：极简默认
> ```bash
> docker run -d --rm \
>     --name rabbitmq-5672 \
>     --hostname rabbitmq-5672 \
>     -p 5672:5672 \
>     -p 15672:15672 \
>     rabbitmq:3.10-management
> ```

> [!cite]- 示例二：挂载宿主机目录（以保留数据）
> ```bash 
> ## mkdir rabbitmq-5672-data
> 
> docker run -d --rm \
>     --name rabbitmq-5672 \
>     --hostname rabbitmq-5672 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v $(pwd)/rabbitmq-5672-data:/var/lib/rabbitmq \
>     -p 5672:5672 \
>     -p 15672:15672 \
>     rabbitmq:3.10-management
> ```

> [!cite]- 示例三：挂载宿主机目录以及配置文件目录
> 这里利用了一个[[202302071558#如何“弄到”默认配置文件#RabbitMQ 默认配置文件|小技巧]]获得 RabbitMQ 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。
> ```bash 
> docker run -d --rm \
>     --name rabbitmq-5672 \
>     --hostname rabbitmq-5672 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v $(pwd)/rabbitmq-5672-data:/var/lib/rabbitmq \
>     -v $(pwd)/rabbitmq.conf.d:/etc/rabbitmq \
>     -p 5672:5672 \
>     -p 15672:15672 \
>     rabbitmq:3.10-management
> ```

> [!cite]- 示例四：挂载自建数据卷以及配置文件目录
> 
> > 了解
>
> 在上个示例的基础上，创建并使用了数据卷代替了宿主机目录。当然，数据卷本质上也是在数据集上的一个目录<small>（只不过在一个特定的位置）</small>。
> 
> ```bash
> docker volume rm rabbitmq-5672-data
> docker volume create --name rabbitmq-5672-data
> 
> docker run -d --rm \
>     --name rabbitmq-5672 \
>     --hostname rabbitmq-5672 \
>     -v /etc/localtime:/etc/localtime:ro \
>     -v rabbitmq-5672-data:/var/lib/rabbitmq \
>     -v $(pwd)/rabbitmq.config.d:/etc/rabbitmq \
>     -p 5672:5672 \
>     -p 15672:15672 \
>     rabbitmq:3.10-management
> ```

### 第 3 步：验证

查看容器的运行信息

```bash
docker ps
```

访问 management web 管理系统<small>（15672）</small>。

由于我们使用的是 management 镜像创建的容器，因此，容器自带了后台管理系统。我们可以通过我们映射的 **15672** 端口访问。

访问网址 `http://宿主机IP:15672` ，使用 `guest / guest` 登录。<small>有一点点延迟，启动容器后稍等片刻。</small>

> [!info] 提示
> 虽然用户名是叫 **guest** ，但是它的角色是管理员 administrator 。

### 其它

- [[202302071558#如何“弄到”默认配置文件#RabbitMQ 默认配置文件|《如何弄到 RabbitMQ 默认配置文件》]]

