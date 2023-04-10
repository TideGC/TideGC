---
alias:
  - Intellij IDEA 调试魔法
tags:
  - debug
  - idea
---

# Intellij IDEA 调试魔法

## 1. 断点处添加 log

==重要==

很多程序员在调试代码时都喜欢 print 一些内容，这样看起来更直观，print 完之后又很容易忘记删除掉这些没用的内容，最终将代码提交到 remote ，code review 时又不得不删减这些内容重新提交，不但增加不必要的工作量，还让 log tree 的一些节点没有任何价值。

IntelliJ IDEA 提供 <mark style="background: #CACFD9A6;">Evaluate and Log at Breakpoints</mark> 功能恰巧可以帮助我们解决这个问题, 来看下面代码：

```java
public static void main(String[] args) {

    ThreadLocalRandom random = ThreadLocalRandom.current();
    int count = 0;
    for (int i = 0; i < 5; i++) {
        if ( isInterested(random.nextInt(10)) ) {
            count++;
        }
    }
    System.out.printf("Found %d interested values%n", count);
}

private static boolean isInterested(int i) {
    return i % 2 == 0;
}
```

假如我们想在第 15 行查看每次调用，随即出来的 i 的值到底是多少，我们没必要在这个地方添加任何 log，在正常加断点的地方使用快捷键 <mark style="background: #FFB8EBA6;">Shift + 鼠标左键</mark> ，就会弹出下面的内容：

![idea-debug-01|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528143534.jpg)

勾选上 ==Evaluate and log== , 并自定义你想查看的 log/变量 ，比如这里的 "interested" + i, 这样以 Debug 模式运行程序（正常模式运行，不会打印这些 log）：

```txt copybtn:false
interested 7
interested 5
interested 1
interested 2
interested 0
Found 2 interested values
```

如果你在多处添加了这种断点，简单的看 log 可能偶尔还是不够直观，可以勾选上面图片绿色框线的 =="Breakpoint hit" message== :

```txt copybtn:false
Breakpoint reached at top.dayarch.TestDebug.isInterested(TestDebug.java:49)
interested 6
Breakpoint reached at top.dayarch.TestDebug.isInterested(TestDebug.java:49)
interested 0
Breakpoint reached at top.dayarch.TestDebug.isInterested(TestDebug.java:49)
interested 9
Breakpoint reached at top.dayarch.TestDebug.isInterested(TestDebug.java:49)
interested 8
Breakpoint reached at top.dayarch.TestDebug.isInterested(TestDebug.java:49)
interested 1
Found 3 interested values
Disconnected from the target VM, address: '127.0.0.1:0', transport: 'socket'

Process finished with exit code 
```

如果你想要更详细的信息，那就勾选上 Stack trace ( 大家自己查看运行结果吧 )，有了这个功能，上面说的一些问题都不复存在了。

## 2. 字段断点

如果你阅读源码，你一定会有个困扰，类中的某个字段的值到底是在哪里改变的，你要一点点追踪调用栈，逐步排查，稍不留神，就可能有遗漏。

> 我们可以在 IntelliJ IDEA 中为某个字段添加断点，当字段值有修改时，自动跳到相应方法位置

使用起来很简单：

- step 1: 在字段定义处鼠标左键添加断点<small>（会出现“眼睛”的图标）</small>
- step 2: 在“眼睛”图标上鼠标右键
- step 3: 在弹框中勾选上 <mark style="background: #CACFD9A6;">Field access</mark> 和 <mark style="background: #CACFD9A6;">Field modification</mark> 两个选项

![idea-debug-02|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528143941.jpg)

如果修改字段值的方法比较多，也可以在 `Condition` 的地方定义断点进入条件, 有了这个功能的加成，相信你阅读源码会顺畅许多。

## 3. 异常断点

<small>#h/lime ==重要==</small>

除了阅读源码，一定是遇到了异常我们才开始调试代码，代码在抛出异常之后会自动停止，但是我们希望：

> 代码停在抛出异常之前，方便我们查看当时的变量信息

这时我们就用到了 Exception Breakpoints, 当抛出异常时，在 catch 的地方打上断点，可以通过下图的几个位置获取栈顶异常类型，比如这里的 NumberFormatException

![idea-debug-03|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144022.jpg)

知道异常类型后，就可以按照如下步骤添加异常断点了：

![idea-debug-04|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144025.jpg)

然后在弹框中选择 NumberFormatException

![idea-debug-05|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144028.jpg)

重新以 Debug 模式运行程序：

![idea-debug-06|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144030.jpg)

程序「一路绿灯式」定位到抛出异常的位置，同时指出当时的变量信息，三个字：稳，准，狠，还有谁？

## 4. 方法断点

当阅读源码时，比如 Spring，一个接口的方法可能被多个子类实现，当运行时，需要查看调用栈逐步定位实现类，IDEA 同样支持在接口方法上添加断点（快捷键 cmd+F8 / ctrl+F8）：

1. 鼠标左键在方法处点击断点（♦ ️形状）
2. 断点上鼠标右键

勾选上绿色框线上的内容，同样可以自定义跳转条件 Condition

![idea-debug-07|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144316.jpg)

当以 Debug 模式运行程序的时候，会自动进入实现类的方法（注意断点形状，是个菱形）：

![idea-debug-08|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220528144319.jpg)

看到这你应该想到常见的 Runnable 接口中的 run 方法了，同样是有作用的，大家可以自行去尝试了

## 5. 总结

相信有以上四种调试技巧的加成，无论是工作debug 还是私下阅读源码，都可以轻松驾驭了。最后，来看看 IDEA 支持的各种断点调试类型，如果你只知道红色小圆点，那咱在留言区好好说说吧

![|700](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627132704.jpg)










