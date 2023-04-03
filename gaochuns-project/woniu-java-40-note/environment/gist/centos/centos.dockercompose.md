---
alias: docker-compose 在 CentOS Stream 上的安装
---

## docker-compose 在 CentOS Stream 上的安装

docker-compose 本质上是一个命令<small>（ 而命令的本质则是一个可执行的二进制文件 ）</small>，当然，docker-compose 不是 Linux 自带的命令，所以需要我们"安装"。

所谓的"安装"，本质上就是将 docker-compose 的二进制可执行程序放在 Linux 特定的目录下，让 Linux 系统"多"出来一个命令可用。

### 第 1 步：确认服务器上是否已安装 docker-compose 命令

执行如下 Shell 命令：

```bash
## Shell 命令
docker-compose -v
```

如果出现类似如下内容，则证明已安装 docker-compose：

```bash
Docker Compose version v2.2.2
```

此时，则无须再次安装 docker-compose 。跳过以下内容，直接进入使用环节。

如果出现类似如下内容，则证明还未安装 docker-compose： 

```bash
-bash: docker-compose: command not found
```

### 第 2 步：下载 docker-compose 二进制程序

网速和人品都不错的时候，你可以从 docekr-compse 的 github 仓库<small>（ 或者国内的 daocloud ）</small>下载<small>（ 优先考虑从 daocloud 下载，速度更快 ）</small>：

```bash
# sudo curl \ 
#   -L "https://github.com/docker/compose/releases/download/v2.6.0/docker-compose-$(uname -s)-$(uname -m)" \
#   -o /usr/local/bin/docker-compose

sudo curl \
	-L https://get.daocloud.io/docker/compose/releases/download/v2.6.0/docker-compose-`uname -s`-`uname -m` \
	-o /usr/local/bin/docker-compose
```

你可以通过修改 URL 中的版本，可以自定义您的需要的版本。

网速和人品不好的时候，你需要向老师索要已经下好的 docker-compose ，并上传到你的 Linux 服务器上。

### 第 3 步：启用 docker-compose 命令

```bash
## Shell 命令
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose
docker-compose -v
```



