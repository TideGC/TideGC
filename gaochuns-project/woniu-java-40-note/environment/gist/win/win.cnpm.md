---
alias: cnpm 是什么鬼？
---

## cnpm 是什么鬼？

有没有办法在不改变"中央仓库"网址的配置的情况下，仍然使用国内的淘宝的仓库呢？

有！就是有点麻烦：你的未来的每一个 npm install 命令都要带上淘宝的仓库网址。例如：

```bash
npm install <某个包的包名> --registry=https://registry.npm.taobao.org
```

这样太麻烦了，有没有简单一点的办法<small>（ 前提仍然是不改变中央仓库网址的设置 ）</small>？

有！安装，并使用 cnpm 命令。

先安装<small>（ 只用装一次 ）</small>：

```bash
npm install -g cnpm --registry=https://registry.npmmirror.com
```

装完以后使用：

```bash
cnpm install <某个包的包名>
```

cnpm 命令本质上就是之前那个命令的简写版：它「帮」你在你的每一个 npm install 命令之后加上了 --registry 。

> [!danger] 警告
> 有人反映这种方式下载的包的依赖上有时可能会有问题。所以有些人不推荐使用 cnpm 命令。

