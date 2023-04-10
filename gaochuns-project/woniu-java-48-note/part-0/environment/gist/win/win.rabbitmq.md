---
alias: RabbtiMQ 在 Windows 上的安装
---

## RabbtiMQ 在 Windows 上的安装


> [!danger] 警告
> 解压路径中不要有空格和中文！
> 
> 例如：**D:\\ProgramFiles** 就是很好的选择。
> 
> 另外，对于在 Windows 上解压安装 RabbitMQ 而言，如果计算机名中有中文，也会安装失败。
> 
> 这种情况下，直接在服务器上安装、使用 Docker RabbitMQ ，不要浪费时间了。[[202302230715]]

优先考虑使用服务器上的 RabbitMQ Server ！

### **第 1 步**：下载 Erlang 安装程序

> 由于 RabbitMQ 是用 Erlang 语言编写的，因此需要先安装 Erlang 语言的开发、运行环境。

下载地址：[*http://www.erlang.org/downloads*](http://www.erlang.org/downloads) 

> [!attention] 注意
> 后面我们解压、安装、使用的 RabbitMQ 3.10.2 需要 Erlang 版本 `≥ 23.2` ，支持所有的 24 版本。因此我们这里下载 Erlang 时注意一下版本。

我们这里下载的是 24.3.4 版本。

### 第 2 步：安装 Erlang

双击运行 Erlang 安装程序，一路 next ，不要改动安装程序的默认配置。

> 安装过程中可能会让你安装 Microsoft Visual C++ 运行环境，同样也是点击确认，同意安装。
> 
> ![](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20221104081948.png)

Erlang 会被安装到默认路径 "**C:\\Program Files**" 下。

### 第 3 步：添加 Erlang 环境变量

> 类似于你当初配置 JAVA 环境变量。

增加环境变量 *ERLANG_HOME=C:\\Program Files\\erl10.4* 。

修改环境变量 Path ，在原来的值的基础上追加 `%ERLANG_HOME%\bin` 。

### 第 4 步：下载、解压 RabbitMQ

下载 RabbitMQ ，当前<small>（2022-5-30）</small>最新版本是 [3.10.2](https://github.com/rabbitmq/rabbitmq-server/releases/download/v3.10.2/rabbitmq-server-windows-3.10.2.zip) ，我们使用的是 _3.8.1_ 版。

解压压缩包到 **D:\\ProgramFiles** 目录下。

> [!info] 提示
> 因此，后文的 RABBIT_HOME 指的就是 D:\\ProgramFiles\\rabbitmq_server-3.8.1 。

### 第 5 步：安装 web 控制台插件

在解压目录下的 "sbin" 目录下打开控制台，输入并执行如下命令：

```bash
rabbitmq-plugins.bat enable rabbitmq_management
```

会看到类似如下内容:

```text
Enabling plugins on node xxx:
…

started 3 plugins.
```

### 第 6 步：运行 Rabbitmq Server

在解压目录下的 `sbin` 目录下打开控制台，输入并执行如下命令：

双击解压目录下的 `sbin` 下的 `rabbitmq-server.bat` ，启动 RabbitMQ Server 。

```text
rabbitmq-server.bat
```

成功启动时会看到类似如下信息：

```text
…

###  ##      RabbitMQ 3.10.2
###  ##
###########  Copyright (c) 2007-2022 VMware, Inc. or its affiliates.
#######  ##
###########  Licensed under the MPL 2.0. Website: https://rabbitmq.com

…

Starting broker… completed with 3 plugins.
```

### 第 7 步：访问 web 管理页面

通过浏览器访问 [http://localhost:15672](http://localhost:15672)，并使用默认用户 **guest** 进行登录，密码也是 **guest** ，登录后的页面：

![rabbitmq-install-01)](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627174035.png)

比如 channels / exchanges / queues 等，可以逐个点进去看下详细情况。

> [!attention] 注意
> guest/guest 账号密码只能在本地登录，所以，你要远程登录"别人"的 RabbitMQ Server ，需要新建账号，并授权。
> 
> 如果要添加新用户的话，点击 Admin 选项卡，进行添加。

### 其它：如何关闭 RabbitMQ

ctrl-c 或者直接关闭运行 rabbitmq 的控制台。
