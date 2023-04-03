## MinIO 在 Windows 上的安装

### 1. 简介

Minio 是个基于 Golang 编写的开源对象存储套件，虽然轻量，却拥有着不错的性能。


| 说明 | 网址 |
| -: | :- |
| 官网地址| [MinIO \| High Performance, Kubernetes Native Object Storage](https://min.io/) |
| 官网文档地址|[MinIO \ The MinIO Quickstart Guide](https://docs.min.io/)   |
| 官网文档<small>（ 中文 ）</small>地址|[官网中文网址](http://docs.minio.org.cn/docs/) <small>中文文档对应的是上个版本，新版本中有些内容已发生了变化。</small> |
| JAVA SDK API|[minio java sdk api 文档](https://docs.min.io/docs/java-client-api-reference.html)|


> [!cite] 何为对象存储？
> 对象存储服务<small>（ Object Storage Service，OSS ）</small>是一种海量、安全、低成本、高可靠的云存储服务，适合存放任意类型的文件。容量和处理能力弹性扩展，多种存储类型供选择，全面优化存储成本。

对于中小型企业，如果不选择存储上云，那么 Minio 是个不错的选择，麻雀虽小，五脏俱全。

### 第 1 步：下载安装

Windows 版下载地址：[windows-amd64 版](https://dl.min.io/server/minio/release/windows-amd64/minio.exe)

按惯例，我们将下载的 minio.exe 放在 D:\\ProgramFiles\\MinIO 下，因此，MINIO_HOME 就是 D:\\ProgramFiles\\MinIO 。


### 第 2 步：启动运行

1. 进入 MINIO_HOME 。

2. 打开 cmd 执行命令：`minio.exe server ./data`

启动后会打印出 AccessKey 和 SecretKey 等信息，类似如下：

```text
API: http://192.172.5.42:9000  http://192.168.154.1:9000  http://192.168.181.1:9000  http://127.0.0.1:9000              
RootUser: minioadmin
RootPass: minioadmin

Console: http://192.172.5.42:60880 http://192.168.154.1:60880 http://192.168.181.1:60880 http://127.0.0.1:60880
RootUser: minioadmin
RootPass: minioadmin

...
```

### 第 3 步：使用

MinIO Server 成功启动后访问 [http://127.0.0.1:9000](http://127.0.0.1:9000) ，你会看到类似如下界面：

![minio-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171956.png)

输入用户名/密码 `minioadmin/minioadmin` 可以进入 web 管理系统：

![minio-02](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171954.png)

### 其它：关停 MinIO

通过命令行启动 MioIO 后，命令行窗口就被占用了。关闭命令行窗口即关闭 MioIO ，也可以使用 `ctrl + c` 关闭。