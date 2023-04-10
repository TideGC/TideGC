---
alias: 'nodejs 在 CentOS Stream 上的安装'
---

## nodejs 在 CentOS Stream 上的安装

CentOS 8 上默认的源中的 node.js 的版本比较低<small>（好像是 14.x）</small>，很显然确实是有点太老旧了一点。对此，我们不能从默认的源中下载，而需要配置另外的<small>（官方的）</small>源。

### 第 1 步：卸载旧版本

稳妥起见，先确认你的电脑上没有旧的 nodejs 。

执行如下命令行命令命令进行卸载删除：

```bash
dnf remove nodejs npm -y
```

### 第 2 步：添加 nodejs 新源

> 在一步操作只用执行一次。如果你或者别人曾经在这台服务器上执行过这部操作，那么就不用再重复执行了。

下载并执行 nodejs 官方提供的 shell 脚本：

```bash
curl --silent --location https://rpm.nodesource.com/setup_16.x | bash -
```

> [!info] 上面命令的效果
> 上述命令会在你的 "/etc/yum.repo.d/" 目录下面创建了一个 "nodesource-el8.repo" 文件，其中内容如下：
> 
>  ```ini
>  [nodesource]
>  name=Node.js Packages for Enterprise Linux 8 - $basearch
>  baseurl=https://rpm.nodesource.com/pub_16.x/el/8/$basearch
>  failovermethod=priority
>  enabled=1
>  gpgcheck=1
>  gpgkey=file:///etc/pki/rpm-gpg/NODESOURCE-GPG-SIGNING-KEY-EL
>  
> 
> [nodesource-source]
>  name=Node.js for Enterprise Linux 8 - $basearch - Source
>  baseurl=https://rpm.nodesource.com/pub_16.x/el/8/SRPMS
>  failovermethod=priority
>  enabled=0
>  gpgkey=file:///etc/pki/rpm-gpg/NODESOURCE-GPG-SIGNING-KEY-EL
>  gpgcheck=1
>  ```

执行更新网络源的数据信息，这个过程要持续好一会<small>（看着屏幕刷刷刷）</small>：

```bash
dnf update -y
```

### 第 4 步：安装 nodejs

```bash
dnf install -y nodejs
```

验证 nodejs 和 npm 的安装：

```bash
node -v
npm -v
```

对于初学者，你可以[[centos.nodejs.uninstall|卸载]]掉 nodejs 再重新安装学习、熟悉安装过程和步骤。

### 第 6 步：改变 npm 的中央仓库网址

既然 npm 相当于 maven ，那么，显而易见，npm 未来会从「网上」下载包。你可通过下述 Shell 命令查看到这个默认的「下载网址」：

```bash
# Shell 命令
npm config get registry

# 不出意外显示的应该是："https://registry.npmjs.org/"
```

而和 maven 一样，默认的、官方的中央仓库在国外，从国内连接、下载网速感人，而且还容易中断。因此，在安装 npm 之后，我们都会设置 npm ，让它从另一个、国内的仓库下载：淘宝。

```bash
# 新域名。旧域名 2022 年 05 月 31 日起停用
npm config set registry https://registry.npmmirror.com
```




