---
alias: 
- 'Nginx 在 CentOS Stream 下的卸载'
tags: 
- linux
- nginx 
---

## Nginx 在 CentOS Stream 上的卸载

### 第 1 步：停止 Nginx 服务

```sh
systemctl disable nginx --now
```

### 第 3 步：卸载 Nginx 

使用 erase 卸载可删除配置文件和数据。<small>如果想留下它们，那就是用 remove 卸载。</small>

```bash
dnf erase -y nginx
```

### 第 4 步：删除 Nginx 配置文件

dnf erase 命令在卸载 nginx 时，会连带删除 nginx 在 /etc 目录下的配置文件。

如果你想留下配置文件，那么当初就不要使用 dnf erase ，而使用 dnf remove 命令卸载 nginx 。

### 第 5 步：删除 Nginx 数据目录<small>（慎重、酌情考虑）</small>

dnf erase 命令在卸载 nginx 时，会连带删除 nginx 在 /usr/share 下的数据目录。

如果你想留下数据目录，那么当初就不要使用 dnf erase ，而使用 dnf remove 命令卸载 nginx 。


