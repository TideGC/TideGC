---
alias: 'Nginx 在 CentOS Stream 上的安装'
---

## Nginx 在 CentOS Stream 上的安装

CentOS Stream 的官方自带软件源中就有 Nginx 。

### 第 1 步：查询搜索

```bash
## 简单查询搜索
dnf search nginx

## 查看详情
dnf info nginx
```

### 第 2 步：安装

```bash
## 安装
dnf install -y nginx
```

对于初学者，你可以[[centos.nginx.uninstall|卸载]]掉 nginx 再重新安装来学习、熟悉安装过程和步骤。

### 第 3 步：启动 nginx

```bash
## 查看 nginx 运行状态
systemctl status nginx

## 在开发环境中，为了避免防火墙的干扰，我们通常会关闭 linux 防火墙
systemctl disable firewalld --now
setenforce 0

## 将 nginx 设置为开机启动，并立即启动
systemctl enable nginx --now
```

### 第 4 步：验证

访问网址 http://<nginx所在服务器ip>:80/index.html 可看到一个默认的 nginx 页面，即可。

### 其它

#### Nginx 在 CentOS Stream 上的配置文件概述


nginx 的配置文件都在 _**/etc/nginx**_ 目录下。


其中 nginx.conf 是入口配置文件。在 nginx.conf 配置文件的末尾，它引入了同级目录下的 conf.d 目录下的所有的 .conf 配置文件。

在 /etc/nginx/conf.d 目录下，你会发现有一个名为 default.conf 的配置文件。

```text
/etc/
└── nginx/
    ├── nginx.conf
    └── conf.d/
        └── default.conf
```

你可以仿造官方的这种<small>（高逼格）</small>做法，如果你有一段 server 的配置，你也可以写在 conf.d 下，写成一个独立的 .conf 配置文件。

当你修改 nginx 的配置文件后，使用如下命令重启 neginx：

```bash
## 重启 nginx
systemctl restart nginx
```

> [!tip] 提示
> 详情参见笔记《[[202208050030|Nginx 在 CentOS Stream 上的配置文件]]》 

