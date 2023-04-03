---
alias: Git 的使用工作流
---

# Git 的使用工作流


Git 工作流作为人为的规定，不止一种。我们这里介绍和使用的是 Atlassian 公司所推荐的一种基于 "git rebase" 命令的 Git 工作流。

这种工作流与其它常见的工作流的却别在于："处理代码冲突"的工作从项目管理者转交到了代码提交者手里。

> [!info] 整个流程的总而言之一句话
> feature rebase on master, master merge feature.

## 1. 核心规则

这种基于 "git rebase" 的工作流核心准则只有 2 条：

> [!important] 准则一
> 让你的本地的 master 分支和远程仓库的 master 分支指向同一个提交记录。即，master 的两个图标要"在一起"。
> 
> ![git-flow-1|300](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163910.png)

> [!important] 准则二
> 让 master 分支所指向的提交记录是你的工作分支的基。即，工作分支回溯时，一定要"路过" master 所指向的节点。
> 
> ![git-flow-2|300](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163912.png)

> [!danger] 总而言之，上述 2 个准则总结起来就是一句话
>  **时刻保证你当前的工作分支是『基于』最新的 master 远程分支。**

至于如何做到这 2 点，我们后面会详细介绍。

## 2. 工作流程

当你要去准备「搞」GitKraken 的时候，你所看到的「应该、应该、应该」是如下情况。<small>如果不是，大概你之前的操作有问题。此时，你已经不具备把 Git「搞」对的前提了，赶紧喊人吧！</small>

![git-flow-10|400](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163917.png)


上图有 3 个值得关注的点，此时，请「依次、依次、依次」思考如下 3 个问题：

```text
1. 有没有虚圈？ --------------------- 答：有，需要解决。
│ 
└──> 2. 是否满足核心准则一？ -------- 答：不满足，需要解决。
     │ 
     └──> 3. 是否满足核心准则二？ --- 答：不满足，需要解决。
```

> [!warning] 注意
> 上述 3 个问题及其解决，一定是要按顺序依次进行的！


### 有虚圈，怎么处理？

- low 逼一点的做法是提交<small>（ Commit ）</small>：虚圈==变实圈==；
- 装逼一点的做法是暂存<small>（ Stash ）</small>：虚圈==变实框==。

> [!dice-1]  处理方式一：提交（ Commit ）

  ![git-flow-commit|500](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163920.gif)

> [!dice-2] 处理方式二：暂存（ Stash ）

  ![git-flow-stash|500](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163925.gif)


### 两个 master 不在一起，怎么处理？

两个 master 不在一起只有一种形式：远程仓库的 master 在本地仓库的 master 的上面、上面、上面！

> [!danger] 警告
> 一旦出现本地 master 在远程 master 的上面，那你就抓紧时间喊人来吧 ... 

- 第 1 步： 切换到 master 分支   

- 第 2 步： 执行 git pull ，更新本地 master 。

![git-flow-master-pull](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163929.gif)

现在，它俩在一起了。


### 工作分支不是基于最新 master ，怎么处理？

- 第 1 步： 切换回你的工作分支   

- 第 2 步： 执行 git rebase ，「变」你的工作分支的「基」。

注意，编辑过程中遇到冲突问题，需要细心解决。因为是为了展示操作过程，下图中我对冲突的解决很简单、粗暴，你们不要这样。

![git-flow-master-rebase](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163936.gif)

现在，你的工作分支指针一定是在 master 上面，至少至少是和 master 在一起。

### 后续操作：pop

如果在第一个问题中，你使用 stash 解决的虚圈，那么在这里，你要做一个 stash 的反向操作 pop 。

![git-flow-master-pop.gif](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163953.gif)


## 其它

### 多个工作分支

理论上，我们肯定是接受到项目经理的工作安排才开始着手工作的，因此，为你接收到的每一个工作任务创建一个独立的工作分支，这个分支上只提交和这个工作任务有关的代码。

> [!note] 如何创建分支
> 1. 确保 2 个 master 在一起；
> 2. 在 master 上鼠标右键 `Create branch here` 。

当项目经理将你的某个工作分支合并到 master 主分支之后，可以就可以删掉它了。

### 强推和交互式变基

在你的工作分支变基之后，你完成了你的工作，并且需要将工作分支的代码上传<small>（ git push ）</small>到中央仓库<small>（ 的对应的远程分支 ）</small>上，这时的推送需要「强推」git push -force 。

在推送前，你可以使用交互式变基或软撤销来优化、美化你的提交记录，将多个提交记录压缩成一个。

### 严禁出现的 2 种情况

如果出现了这 2 种情况，抓紧时间喊人来 ...

- 本地 master 要领先于中央仓库 master 

  ![git-flow-7.png|300](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163956.png)

- 开发分支不是基于最新的 master 就要提交

  ![git-flow-8.gif|400](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627163958.gif)

