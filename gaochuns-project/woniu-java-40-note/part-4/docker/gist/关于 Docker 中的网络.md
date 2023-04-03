---
alias: 关于 Docker 中的网络
tags: docker, docker-network 
---

## 关于 Docker 中的网络

Docker 中的网络模式早先是 3 种，后来又加了 1 种，一共是 4 种。这里，我们只涉及、介绍使用其中的 1 种网络模式：bridge 模式。

_**bridge**_ 是驱动<small>（ Driver ）</small>类型为 bridge 的默认网络；

不同的网络模式的背后，存在一个驱动<small>（Driver）</small>的概念。

你可以通过下述命令查看 Docker 当前的网络情况：

```bash
docker network ls
```

如果愿意，你可以任意创建驱动<small>（ Driver ）</small>类型为 bridge 或其它类型的网络。
