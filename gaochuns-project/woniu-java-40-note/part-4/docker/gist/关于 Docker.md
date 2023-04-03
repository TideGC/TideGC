---
alias: 关于 Docker
tags: docker
---

## 关于 Docker


### 没有 Docker 的世界

> [!faq] 问题
> 没有 Docker 或类似的技术，我们会面对什么问题？

项目发布时，有大量的软件/组件需要在目标服务器上安装运行，而微服务项目因为有多实例运行的需要和要求，所以越发是会涉及到更多的服务器和更多的软件/组件的安装和运行。

#### 目标服务器的环境可能并不统一

- 目标服务器可能是红帽系的发行版，也可能是 Debian 系的发行版；
- 即便是同一个体系的发行版，可能是 RedHat/CentOS ，也可能是 Fedora；
- 即便是同一类系统，可能是 ReadHat/CentOS 7 ，也可能是 8 。

#### 目标服务器上的软件/组件可能并不统一

例如：目标服务器上可能已经有了 MySQL 5.7<small>（ 被其它项目所使用 ）</small>，但你需要的是 8.0 ，而你可能又不能卸载 5.7 安装 8.0 。

#### 软件/组件所依赖的函数库可能并不同

例如，高版本的 ES 和 Jenkins 依赖于高版本的  JDK ，但是你的 Tomcat 所使用的 JDK 又不需要那么高，你直接卸载低版本 JDK 安装 高版本 JDK ，虽然 ES 和 Jenkins 是满意了，但又怕 Tomcat 这些不兼容。

#### 等等...

> [!important] 答案
> Docker<small>（ 它所代表的理念和思路 ）</small>能解决上述及更多项目部署、运行环境相关问题。

### 关于 Docker

Docker 利用"虚拟化"的方案解决了上述问题：每一个软件/组件都放在一个独立的"虚拟机"中运行。

- "虚拟机"能解决系统不统一的问题；
- "虚拟机"能解决软件/组件版本、依赖等不一致问题。

### Docker 和 Vmware/VirtualBox

Docker 不是第一个，也不是唯一一个有"虚拟化"能力的工具，Vmware/VirtualBox 都是更早就出现的可行、可靠的虚拟化方案，为什么不用它俩？

> [!important] 答案
> Vmware/VirtualBox 是"真"虚拟化，而 Docker 是"假"虚拟化。

![](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220826202210.png)


Docker 是依赖于 Linux 内核的 LXC、Cgroup 等技术所实现的轻量级虚拟化方案。

这里"轻量级虚拟化"的意思是：Docker 既实现了虚拟化的资源隔离效果，又没有付出太大的性能损耗。完美~


| 特性     | Docker    | 虚拟机    |
| :------: | :--------:| :-------: |
| 性能     | 接近原生  | 性能较差  |
| 硬盘占用 | 一般为 MB | 一般为 GB |
| 启动     | 秒级      | 分钟级    |

当然代价是：Docker 依赖于 Linux ，所以，你只能在 Linux 上使用 Docker 。



### Docker 的安装

关于 Docker 的安装常见笔记[[centos.docker\|《Docker 的安装》]]。

