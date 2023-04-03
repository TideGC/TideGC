---
alias: ElasticSearch 
---

## Elasticsearch 在 Windows 上的安装

> [!danger] 警告
> 解压路径中不要有空格和中文。例如：**D:\ProgramFiles** 就是很好的选择。

> [!tip] 提示
> Elasticsearch 只有解压版本，没有安装版

Elastic 官网：[https://www.elastic.co/cn/](https://www.elastic.co/cn/)

![es-install-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165136.png)

Elastic 有一条完整的产品线及解决方案：Elasticsearch、Kibana、Logstash 等，前面说的三个就是大家常说的 ELK 技术栈。

![es-install-02](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165134.png)

Elasticsearch 具备以下特点：

- 分布式，无需人工搭建集群<small>（solr 就需要人为配置，使用 Zookeeper 作为注册中心）</small>；
- Restful 风格，一切 API 都遵循 Restful 原则，容易上手；
- 近实时搜索，数据更新在 Elasticsearch 中几乎是完全同步的。


> [!warning] 警告
> kibana 从 7.11 开始升级了 node.js 的版本，因此，从这个版本开始不再支持 win7，也就是说，win7 能使用的 kibana 的最后的版本是 **7.10.2** 。我们这里使用的是 **7.10.1** 版本。

### 1. 安装 ES

#### 第 1 步：下载和解压

从官网下载 Elasticsearch 和 Kibana。注意两者版本必须要一致。这里都是 `7.10.1` 。

> [!tip] 说明
> 如果是在 Linux 安装运行，需要注意的是，出于安全考虑，elasticsearch 默认不允许以 root 账号运行。

将压缩包解压到 **D:\\ProgramFiles** 目录，因此，后文的 

- ES_HOME 指的就是 `D:\ProgramFiles\elasticsearch-7.10.1`

- KIBANA_HOME 指的就是 `D:\ProgramFiles\kibana-7.10.1-windows-x86_64`


#### 第 2 步：配置

我们进入 `ELASTICSEARCH_HOME/config` 目录：

需要修改的配置文件有 2 个：elasticsearch.yml 和 **jvm.options** ，在这里我们要改动的是后者。

Elasticsearch 基于 Lucene 的，而 Lucene 底层是 java 实现，因此我们需要配置 jvm 参数。

编辑 jvm.options 文件，将默认的 1g 调小一点<small>（ 当然你内存够大就不用动它了 ）</small>

```text
-Xms512m
-Xmx512m
```


#### 第 3 步：运行

进入 `ELASTICSEARCH_HOME\bin` 目录

双击 `elasticsearch.bat`，启动成功时，会显示 `started` 字样，并且可我们在浏览器中访问：[http://127.0.0.1:9200](http://127.0.0.1:9200)，可见类似如下内容：

```json
{
  "name": "DESKTOP-U2113T0",
  "cluster_name": "elasticsearch",
  "cluster_uuid": "WWYAej-aRdCw6d8dNsEGTw",
  "version": {
    "number": "7.10.1",
    "build_flavor": "default",
    "build_type": "zip",
    "build_hash": "1c34507e66d7db1211f66f3513706fdf548736aa",
    "build_date": "2020-12-05T01:00:33.671820Z",
    "build_snapshot": false,
    "lucene_version": "8.7.0",
    "minimum_wire_compatibility_version": "6.8.0",
    "minimum_index_compatibility_version": "6.0.0-beta1"
  },
  "tagline": "You Know, for Search"
}
```

#### 其它：如何关闭 ES

ctrl-c 或者关闭控制台。


### 2. 安装 Kibana

Kibana 扮演的角色类似于 MySQL 世界中的 Navicat 。它是 ES 的官方客户端，只不过它是基于 node.js 的 web 版。

Kibana 版本与 elasticsearch 保持一致，在这里必须是 `7.10.1` ，因为 ES 的版本是 `7.10.1` 。

> [!danger] 警告
> 解压路径中不要有中文和空格！

#### 第 1 步：下载和解压

略。

如果因为之前的 Kibana 未能"退出干净"从而导致再次启动 Kibana 无法连上 ES<small>（因为 ES 那里还有上次的连接信息，从而判定你是重复连接，进而导致拒绝）</small>，这种情况下，执行如下命令<small>（向 ES 发送一个 HTTP 请求，让它废除之前的 Kibaba 连接信息）</small>：

```bash
curl -X DELETE http://127.0.0.1:9200/.kibana*
```

#### 第 2 步：配置

进入 **KIBANA_HOME/config** 目录，查看其配置文件 **kibana.yml** 配置文件。

该配置文件中，最重要的一项配置是 **elasticsearch.hosts** ，该配置项要指向 Elasticsearch 服务器的访问地址和端口。其默认值是 localhost:9200 ，表示 kibana 连接的 es 服务器在本机运行。

如果，你的 es 是运行在本机，那么就不用动它；如果你的 es 服务器在别处，例如 192.172.0.201 ，那么就把它改成 192.172.0.201:9200 。


#### 第 3 步：运行

进入 `KIBANA_HOME/bin` 目录，双击 **kibana.bat** 启动。<small>（启动过程比较耗时，耐心等待）</small>。

在 kibana 的启动信息中，你会发现 kibana 的监听端口是 5601 ，等着你从 5601 端口连接它）。


#### 第 4 步：访问、验证

我们可访问：[http://127.0.0.1:5601](http://127.0.0.1:5601)


#### 其它：如何关闭 Kibana

ctrl-c 或者关闭控制台。


### 3. 安装 IK 分词器

IK 分词器版本与 elasticsearch 保持一致，在这里必须是 `7.10.1` ，因为 ES 的版本是 `7.10.1` 。

#### 第 1 步：下载和解压

IK 分词器的下载地址在 [*https://github.com/medcl/elasticsearch-analysis-ik/releases*](https://github.com/medcl/elasticsearch-analysis-ik/releases)


将 elasticsearch-analysis-ik 解压到 `ELASTICSEARCH_HOME/plugins` 目录下。

<Badge text="注意" type="error" /> 解压后，plugins 目录有且仅有 analysis-ik 这一个儿子，没有别的儿子了！而 analysis-ik 目录下有散着的、一堆儿子！

![es-ik-install-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165128.png)


![es-ik-install-02](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165123.png)


#### 第 2 步：测试和验证

**重启 elasticsearch**

- 测试分词器

  ```json
  POST _analyze
  {
    "analyzer" : "ik_smart",
    "text" : "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
  }
  ```


IK 分词器有两种类型：

| IK 分词器类型 | 说明 |
| :- | :- |
| ik_smart | 会做最粗粒度的拆分。 |
| ik_max_word | 会将文本做最细粒度的拆分，会穷尽各种可能。 |

以上述例子为例，`ik_max_word` 拆出了 15 项，而 `ik_smart` 拆出 11 项<small>（不同版本的 ik 分词器可能会略有不同）</small>。

