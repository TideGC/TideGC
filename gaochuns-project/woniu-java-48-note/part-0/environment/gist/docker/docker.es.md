---
alias: Docker Run ElasticSearch
---

## Docker Run ElasticSearch

docker hub 网址 [*https://hub.docker.com/_/elasticsearch*](https://hub.docker.com/_/elasticsearch) 。

当前<small>（2021-04-02）</small>的 7 版本最新的是 **7.12.0** ；但是我们这里要使用的是 **7.10.1** 。

> [!info] 注意
> 支持 Window 7 操作系统的最后一个 es 版本是 **7.10.2**，<small>再高你就要换成 win10 了。</small> 但是 docker 镜像里只有 **7.10.1** 版本。

### 第 0 步：关于 ElasticSearch 镜像和容器

#### 必要的目录

ElasticSearch 容器的数据存储目录、插件目录和配置文件目录都在一起：

- _数据存储目录_ — 容器中的 `/usr/share/elasticsearch/data`
- _插件存储目录_ — 容器中的 `/usr/share/elasticsearch/plugins`
- _配置文件目录_ — 容器中的 `/usr/share/elasticsearch/config`

- _必要环境变量_ — `discovery.type`

> [!cite]- 关于必要环境变量
> Docker ES 的必要环境变量 **discovery.type** 表示 ES 的运行模式。
> 
> 开发中我们通常使用 "discovery.type=single-node" ，表示单节点运行。

### 第 1 步：下载镜像

如果本地有镜像，则无需下载。是否有镜像，可通过 docker images 命令查看。

```bash
docker pull elasticsearch:7.10.1
```

### 第 2 步：docker run 运行容器

> [!cite]- 示例一：简单示例
> ```bash
> ## 未挂载「数据存储目录」和「插件目录」
> docker run -d --rm \
>   --name es-9200  \
>   -e "discovery.type=single-node"  \
>   -v /etc/localtime:/etc/localtime:ro \
>   -p 9200:9200  \
>   -p 9300:9300  \
>   elasticsearch:7.10.1
> ```

> [!cite]- 示例二：挂载宿主机目录（以保存数据）
> ```bash
> docker run -d --rm \
>   --name es-9200 \
>   -e "discovery.type=single-node" \
>   -v /etc/localtime:/etc/localtime:ro \
>   -v $(pwd)/es-9200-data:/usr/share/elasticsearch/data \
>   -v $(pwd)/es-9200-plugins:/usr/share/elasticsearch/plugins \
>   -p 9200:9200  \
>   -p 9300:9300  \
>   elasticsearch:7.10.1
> ```

> [!cite]- 示例三：使用数据卷
> > 了解
> 
> 这里利用了一个[[202302071558#如何“弄到”默认配置文件#ElasticSearch 默认配置文件|小技巧]]获得 Nginx 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。
> 
> ```bash
> # 挂载「数据存储目录」和「插件目录」
> 
> # docker volume rm es-9200-data
> # docker volume create --name es-9200-data
> 
> # docker volume rm es-9200-plugins
> # docker volume create --name es-9200-plugins
> 
> docker run -d --rm \
>   --name es-9200 \
>   -e "discovery.type=single-node" \
>   -v /etc/localtime:/etc/localtime:ro \
>   -v es-9200-data:/usr/share/elasticsearch/data \
>   -v es-9200-plugins:/usr/share/elasticsearch/plugins \
>   -p 9200:9200  \
>   -p 9300:9300  \
>   elasticsearch:7.10.1
> ```

> [!warning]- 可能会报错
> docker 运行 elasticsearch 时，有可能会报如下错误：
> 
> ```sh
> max virtual memory areas vm.max_map_count [65530] is too low, increase to at least [262144]
> ```
>  
> 解决办法是：在宿主机 "/etc/sysctl.conf" 文件最后添加一行：vm.max_map_count=262144 ，然后执行命令 "sysctl -p" 。
>  
> ```bash
> echo "vm.max_map_count=262144" >> /etc/sysctl.conf
> ```


### 第 3 步：验证

通过 _docker ps_ 查看容器的运行信息。

访问网址 `http://主机IP:9200/`，<small>会有一点点延迟，</small>会看到类似如下内容:

```json
{
  "name" : "…",
  "cluster_name" : "…",
  "cluster_uuid" : "…",
  "version" : {
    "number" : "7.10.1",
    "build_flavor" : "…",
    "build_type" : "…",
    "build_hash" : "…",
    "build_date" : "…",
    "build_snapshot" : …,
    "lucene_version" : "…",
    "minimum_wire_compatibility_version" : "…",
    "minimum_index_compatibility_version" : "…"
  },
  "tagline" : "You Know, for Search"
}
```


