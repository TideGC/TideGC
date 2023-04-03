# 接口管理软件 Apifox

简单来说，Apifox 它是集：接口文档管理、接口调试、Mock、接口自动化测试于一体的 all in one 式的全流程工具，其功能等同于 Postman + Swagger + Mock + JMeter 几款工具功能累加。

官方地址：[https://www.apifox.cn](https://www.apifox.cn)

概括来讲，Apifox 常用功能分为 4 类：

- 接口文档定义功能：Apifox 遵循 OpenApi 3.0 <small>( 原 Swagger )</small>、JSON Schema 规范的同时，提供了非常好用的可视化文档管理功能，零学习成本，非常高效。
- 接口调试功能：Postman 有的功能，比如环境变量、预执行脚本、后执行脚本、Cookie / Session 全局共享 等功能，Apifox 都有，并且和 Postman 一样高效好用。
- 数据 Mock 功能：内置 Mock.js 规则引擎，非常方便 mock 出各种数据，并且可以在定义数据结构的同时写好 mock 规则。支持添加“期望”，根据请求参数返回不同 mock 数据。最重要的是 Apifox 零配置即可 Mock 出非常人性化的数据，具体在本文后面介绍。
- 接口自动化测试：提供接口集合测试，可以通过选择接口<small>（ 或接口用例 ）</small>快速创建测试集。目前接口自动化测试更多功能还在开发中！目标是：JMeter 有的功能基本都会有，并且要更好用。

> 对于 Java 后端开发工程师，我们主要使用的是它的接口调试和接口文档定义功能；对于前端开发工程师和测试工程师，他们会分别用到 Mock 功能和自动化测试功能。

## hello world

在 Apifox 中，「项目」隶属于「团队」，所以，你需要加入或创建一个团队，然后才能再在「团队之下」创建或发现项目，进而使用 Apifox 。

![apifox-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133454.png)

在上图中演示了：

- 如何调出团队窗口；
- 如何选择团队，如何选择团队下的项目；
- 如何退出、解散团队；
- 如何创建自己的团队。


对于初次使用，Apifox 官方会为你准备一个宠物店的小 demo 供你把玩。

在 Apifox<small>（ 及其同类的产品中 ）</small> 可以为项目配置多个环境，即，为同一个 URI 拼上不同的前缀，自动生成不同的 URL ，从而可以向不同的服务发起请求。

- 点这里可以触发、进入设置页面。可以选择修改已有的设置，也可以再创建新设置：

  ![apifox-02](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133456.png)

- 在这里进行设置：

  ![apifox-03](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133458.png)

- 你所选择的环境（的相关配置）会拼接到你的 URI 之前，从而决定了请求的去往目的地：

  ![apifox-04](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133500.png)

对于 Apifox<small>（ 及其同类产品 ）</small>的接口调试功能，即，如何发出 HTTP 请求，和 postman 基本一摸一样，<small>毕竟，Postman 是这个领域内的翘楚和标杆，后浪们都是再抄它的这个功能。</small>这里就不再赘述和一一演示了。

另外，你可以在项目中新建一个接口，需要注意的是，接口隶属于某个分组，所以，你需要创建新的分组，或选中某个已有分组，作为你新建接口的落脚点。

![apifox-05](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133502.png)

## 文档功能

我们之所以使用 Apifox<small>（ 及其同类产品 ）</small>来代替 postman 主要目的是因为他们在「发起 HTTP 请求」这个功能之外还整合了其它功能，其中一个重要功能就是「文档」功能。

> 用 swagger 作在线文档属实太难看了一点，不友好，而且它的发送 HTTP 请求功能偏弱。

如果你对 Swagger 不陌生的话，其实 Apifox<small>（ 及其同类产品 ）</small> 的文档功能本质上就等同于 swagger 。所有的、需要你在 apifox 上去填写、编写的内容在曾经的 swagger 页面上你一定都见到过。


![apifox-06](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627133505.png)