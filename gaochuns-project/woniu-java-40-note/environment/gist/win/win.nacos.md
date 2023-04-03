---
alias: Nacos 解压版 
tag: [nacos]
---

## Nacos 解压版 

Nacos 只有解压版本，没有安装版

> [!danger] 警告
> 解压路径中不要有空格和中文。例如：**D:\\ProgramFiles** 就是很好的选择。

### 第 1 步：下载和解压

首先去 nacos 的 github 地址下载 release 安装包。[下载地址](https://github.com/alibaba/nacos/releases)

解压 nacos 压缩包到 **D:\\ProgramFiles** 目录下。因此，后文的 NCAOS_HOME 指的就是 D:\\ProgramFiles\\nacos-server-1.4.2 。

### 第 2 步：启动

进入到 nacos/bin 目录下面，**startup** 命令用于启动 nacos ，**shutdown** 命令用于停掉 nacos 。

在 windows 的命令行黑窗口中执行如下命令，启动 Nacos ：

```bash
startup.cmd -m standalone
```


### 第 3 步：访问验证

nacos 的默认服务端口是 **8848** ，启动完成之后通过浏览器访问 nacos：[http://127.0.0.1:8848/nacos](http://127.0.0.1:8848/nacos) 。

看到如下界面，需要登陆，默认的用户名密码是 nacos/nacos ，登陆之后看到如下界面：

![nacos-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627174231.png)

nacos 的单机 **standalone** 模式是开发环境中使用的启动方式，它对用户而言非常友好，几乎不需要的更多的操作就可以搭建 nacos 单节点。另外，standalone 模式安装默认是使用了 nacos 本身的嵌入式数据库 apache derby 。


### 其它：如何关闭 Nacos Server

ctrl-c 或者关闭控制台窗口

