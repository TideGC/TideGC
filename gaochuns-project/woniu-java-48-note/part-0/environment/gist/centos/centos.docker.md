---
alias: 
- "Docker 在 CentOS Stream 上的安装"
tags: 
- docker 
- install
- centos
---

## Docker 在 CentOS Stream 上的安装

CentOS 的默认的红帽公司软件源中实际上有 docker 的安装包，如果直接进行 `yum install docker` 也是可行的。

但是这里的 Docker 的版本并不太新。因此，Docker 官方自己又提供了软件源供用户下载最新<small>（及历史）</small>版本。

### 第 1 步：卸载旧版本

较旧的 Docker 版本称为 *docker* 或 *docker-engine* 。如果已安装这些程序，请卸载它们以及相关的依赖项。

```bash
dnf remove -y docker \
  docker-client \
  docker-client-latest \
  docker-common \
  docker-latest \
  docker-latest-logrotate \
  docker-logrotate \
  docker-engine
```

### 第 2 步：添加 Docker 官方软件源

```bash
yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo

## 更新 yum 软件源的清单
dnf makecache 
```

### 第 3 步：验证软件源配置

查看所有仓库中所有 docker 版本：

```bash
dnf list docker-ce --showduplicates | sort -r
```

如果出现一大堆的 docker-ce 信息则表示 Docker 官方软件源配置正确。

### 第 4 步：下载 Docker 并安装

```bash
## 安装默认版本（也就是最新版）
dnf install -y docker-ce
```

### 第 5 步：配置 Docker Server 开机启动

```bash
## 加入开机启动项，且立刻生效（即，现在就启动，而不用等下次系统重启）
systemctl enable docker --now

## 在开发环境中，为了避免防火墙的干扰，我们通常会关闭 linux 防火墙
systemctl disable firewalld --now
setenforce 0

## 查看状态
systemctl status docker
```

会出现类似如下结果：

```bash
Created symlink from /etc/systemd/system/multi-user.target.wants/docker.service to /usr/lib/systemd/system/docker.service.
```

### 第 6 步：验证安装是否成功

输入 `docker version` 命令，会出现类似如下内容：

```bash
Client: Docker Engine - Community
  Version:          20.10.12
  …

Server: Docker Engine - Community
Engine:
  Version:          20.10.12
  …
```

有 Client 和 Server 两部分表示 docker server 安装并启动都成功了。

### 第 7 步：配置国内 docker hub 镜像服务器

由于 docker hub 的中央镜像仓库在国外，因此有时我们连接 docker hub 从中下载镜像速度会很感人，因此，我们需要配置国内的镜像网址，从国内现在镜像文件。

国内的镜像本质上就是 docker hub 中央仓库在国内的一份缓存/备份。

#### 第 7.1 步：创建并编辑 docker 配置文件

```bash
mkdir -p /etc/docker
cat > /etc/docker/daemon.json << EOF
{
  "registry-mirrors": [
     "https://registry.docker-cn.com",
     "https://docker.mirrors.ustc.edu.cn"
  ]
}
EOF
```

该配置文件配置了两个镜像，一个是 docker 官方在中国境内的官方镜像，一个是中科大维护的一个镜像。

#### 第 7.2 步：重新启动 docker 服务

```bash
systemctl restart docker
```

#### 第 7.3 步：查看修改结果

```bash
docker info
```

会有如下内容<small>（则表示配置成功并生效）</small>：

```
…
Registry Mirrors:
  https://registry.docker-cn.com/
  https://docker.mirrors.ustc.edu.cn/
…
```

### 第 8 步：验证连接 docker hub 网络镜像仓库

输入 ***docker search -f is-official=true mysql*** 命令，会出现类似如下结果：

```bash
NAME     DESCRIPTION                                     STARS  OFFICIAL …
mysql    MySQL is a widely used, open-source relation…   8819   [OK]     …
mariadb  MariaDB is a community-developed fork of MyS…   3102   [OK]     …
percona  Percona Server is a fork of the MySQL relati…   459    [OK]     …
```
