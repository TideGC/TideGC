---
alias: 
  - plantuml
tags:
  - uml
  - plantuml
---

## plantuml

plantuml 是一款「自动」绘图的工具。它定义了一套语法规则，你按照它的要求编写、提供一个符合语法规则的文本文件，plantuml 就会根据文本中的内容绘制、生成出对应的 UML<small>（或其它）</small>图的图片。

https://plantuml.com/zh/

### 1.Graphviz

plantuml 利用到了 graphviz 。因为，在最底层做画图工作的是 graphviz 。<small>如果你愿意，你也可以直接利用 graphviz 去绘制你所想要的图。当然，要绘制复杂一些的图形，你需要做很多工作。</small>

plantuml 的核心工作是定义出了一套语法规则，然后再利用 graphviz 去绘制这套规则所对应的图。

所以，要使用 plantuml 首先要安装 graphviz ，否则，plantuml 的功能就是空中楼阁。

#### 第 1 步：下载并安装 graphviz

[官网下载地址](http://www.graphviz.org/download/)

下载后执行安装程序，一路 next 。

#### 第 2 步：配置环境变量

定义环境变量 `GRAPHVIZ_HOME` ，并将 %GRAPHVIZ_HOME%\bin 添加到环境变量 PATH 中。<small>其实定义 GRAPHVIZ_HOME 并非必须，关键是要将它的 bin 目录添加到环境变量 PATH 中。</small>

#### 第 3 步：验证

安装、配置完成后，在命令行输入：

```bash
dot -v
```

如果出现类似如下内容，则证明安装成功：

```text
dot - graphviz version 7.1.0 (20230121.1956)
libdir = "C:\Program Files\Graphviz\bin"
Activated plugin library: gvplugin_dot_layout.dll
Using layout: dot:dot_layout
Activated plugin library: gvplugin_core.dll
Using render: dot:core
Using device: dot:dot:core
…
```


### 2.通过 VS Code 使用 plantuml

#### 第 1 步：安装 vs code plantuml 插件

在 vscode 的扩展商城中输入 Plantuml，点击进行安装。

![vscode-plantuml-1.png|1000](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165909.png)


#### 第 2 步：编写 plantuml 文件

在 Plantuml 中我们使用「伪编程语言」来生成图表。plantuml支持多种文件后缀，这里推荐使用 `*.puml` 。

在 .puml 文件中写入如下内容：

```text
@startuml
scale 3
Amber -> Hsing : test
@enduml
```

#### 第 3 步：展现

按 `Alt+D` 或者 `Option+D` 可预览生成的图表。

### 3.通过 IDEA 使用 PlantUML

在 IDEA 中安装并使用 PlantUML 的过程和之前的 VSCode 的情况类似。 

#### 第 1 步：下载插件

在 IDEA 的插件市场<small>（Plugins Marketplace）</small>中下载插件：**PlantUML integration** 。

![idea-plantuml-1](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165913.png)

安装插件后，需要重启 IDEA 。

#### 第 2 步：创建文件

安装插件后，在创建文件时，会多出来一种文件类型： `PlantUML File` 。

![idea-plantuml-2](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165916.png)

在为新创建的文件命名时，PlantUML 插件会很贴心地询问你要创建哪种 UML 图，并在新创建的文件中，帮你填充一些模板代码。

![idea-plantuml-3|200](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165920.png)

#### **第 3 步**：编辑文件并呈现

创建 PlantUML File 之后，IDEA 会自动打开绘图展示窗口。你也可以手动控制这个窗口的显示与隐藏。

![idea-plantuml-4|200](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627165922.png)

在文件中写入如下内容：

```text
@startuml
scale 3
Amber -> Hsing : test
@enduml
```

你就会看到对应的内容。

