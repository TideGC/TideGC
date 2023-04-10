---
alias: 'Nginx 在 Windows 上的安装'
tags: 
- nginx
- install
- windows 
---

## Nginx 在 Windows 上的安装

> [!danger] 警告
> 解压路径不要有空格和中文！
> 
> 例如：**D:\\ProgramFiles** 就是很好的选择。

Nginx 只有解压版本，没有安装版。

### 第 1 步：下载、解压

从官网 [*http://nginx.org/en/download.html*](http://nginx.org/en/download.html) 下载最新的稳定版。例如：nginx-1.20.2 。

解压压缩包到 _**D:\\ProgramFiles**_ 。

> [!tip] 提示
> 因此，后文的 NGINX_HOME 指的就是 ***D:\\ProgramFiles\\nginx-1.20.2*** 。

解压后，可到看到如下内容：

```text
NGINX_HOME
├── conf        配置文件目录
├── contrib
├── docs
├── html        类似 tomcat 的 webapps
├── logs        日志目录
├── temp
└── nginx.exe   启动程序
```

其中 "conf/nginx.conf" 文件是 Nginx 的配置文件。<small>Nginx 启动后占用哪个端口等配置信息就定义在这个文件中。</small>

### 第 2 步：启动 Nginx 

在 NGINX_HOME 目录下打开命令行窗口，输入如下命令行命令

```sh
start nginx
``` 

或者，直接双击 "nginx.exe" 也行。

这里会有一个黑窗口快速地一闪而过，<small>电脑性能强大的同学可能反而看不见，因外太快了…</small>

### 第 3 步：判断 / 验证 Nginx 是否正在运行

在 cmd 命令窗口输入命令 `tasklist /fi "imagename eq nginx.exe"` 。你会看到类似如下页面：

```text
映像名称    PID     会话名      会话#   内存使用
=========== ======= =========== ======= ============
nginx.exe   17220   Console     8       7,148 K
nginx.exe   17660   Console     8       7,508 K
```

> [!attention] 注意
> 如果显示有 2 条 Nginx 信息则表明有一个 Nginx 正在后台运行中。
> 
> 如果出现了 4 条、6 条甚至更多的 Nginx 信息，则表明你无意之中启动了多次 Nginx ，此时有多个 Nginx 正在后台运行。<small>这会为你正常使用 Nginx 带来麻烦和错误。</small>


### 第 4 步：Nginx 的使用

直接在浏览器地址栏输入网址 [http://localhost:80](http://localhost:80) 。你会看到欢迎页面。即，NGINX_HOME 下的 html 目录中的 index.html 内容。

改变 html 目录下的内容就会影响你在 [http://localhost:80](http://localhost:80) 网址所看到的内容。

### 其他：Nginx 的停止

> [!info] 注意
> 有 2 种方案，其中方案是标准方案，但是有一个小缺陷。推荐使用方案二。

#### 方案一：stop/quit

以管理员权限打开 cmd 命令行终端，进入到 NGINX_HOME 。在 NGINX_HOME 下打开 cmd 命令行，输入如下 2 条命令之一：

```bash
## 快速停止 nginx 
nginx -s stop
  
## 完整有序地停止 nginx 
nginx -s quit
```

这种停止方式有一个小缺陷在于：它只能停止一个 Nginx 程序。

#### 方案二：强行 kill 

如果你像上面所说地，有意或无意中开启了多个 Nginx 进程，现在你想关闭所有的 Nginx 进程，那么你就需要不停地执行上述停止命令，直到看到错误信息为止。

如果想依靠用以地解决这个问题，你可使用如下命令一口气关闭掉所有的 nginx ，无论它有多少个进程：

```bash
taskkill /f /t /im nginx.exe
```


