---
alias: Git Stash 的使用
---

## Git Stash 的使用

> 其实 Git Stash 有替代方案，并非一个必须的知识点。

### 1. 有虚圈，不切换

在使用 Git 时，如果有未提交<small>（ 包括 Unstage 和 Staged ）</small>的文件时，不要切换分支<small>（ 也不要去「翻旧账」）</small>。

你切换时，有可能会见到如下的错误提示信息：

![|300](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220723230334.png)

> [!danger] 简而言之：有虚圈，不切换。切记，切记！

![git-stash.gif](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171436.gif)

之所以有这样的惯例原因有二：

1. 有可能你会切换失败，如上图，你想切都没得切。这是因为你的未提交文件和你要切换的那个分支的文件有内容冲突。

2. 如果没冲突，你能切换成功，但是会导致逻辑混乱。初学者一不小心，后续操作就会出状况，到时候又是一堆麻烦要解决。

> [!faq]- 什么情况下会出现 "有虚圈，需要你切换" 的场景？
> 常见两种场景：
> 
> 1. 你在你的开发分支上开发到一半，需要同步远端 master 分支代码。<small>此时，你需要从你的开发分支切换到 master 分支，但是你的开发分支上有「虚圈」。</small>
> 
> 2. 手头的工作被打断，需要紧急修复 master 分支上的 bug 。<small>此时，你需要从 master 分支上新建分支，再从你的开发分支切换到这个新分支，但是你的开发分支上有「虚圈」。</small>

### 2. Git Stash 的作用

现在，我们面对着一个矛盾：按理说应该是 "有虚圈，不切换" 但是我们当前的工作任务又需要我们切换分支<small>（ 或去「翻旧账」）</small>。怎么办？

靠 "git stash" 命令。

"git stash" 用于想要保存当前的未提交<small>（ Unstage 和 Stage ）</small>的修改。

![git-stash-01.gif](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171441.gif)

"git stash" 将本地的修改保存起来，并且将当前代码切换到 HEAD 提交上，即，你磁盘上的文件会恢复成未修改之前的样子。

> [!info] 简单来说
> 使用 git stash 还你一个干净的工作目录！

### 3. Git Stash 的使用

| # | 思考和操作 |
| :-: | :------------------------------------ |
| 0 | 有虚圈，不切换。                          |
| 1 | "git stash" 存档，暂存未提交修改。           |
| 2 | 切换，搞事情；搞完事情，切回来。            |
| 3 | "git pop" 读档，继续完成「当初」未完成的工作。|

