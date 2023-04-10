---
alias: 
  - 方法命名常用单词
---

# 方法命名常用单词

## 1. 常用的成对出现的动词

### 1.1 反义词 

| 含义 | 单词      | 反义词 | 反义词单词 |
| :--- | :-------- | :----- | :------ |
| 获取 | get       | 设置   | set     | 
| 增加 | add       | 删除   | remove  | 
| 创建 | create    | 移除   | destroy | 
| 启动 | start     | 停止   | stop    |
| 打开 | open      | 关闭   | close   | 
| 读取 | read      | 写入   | write   |
| 载入 | load      | 保存   | save    | 
| 开始 | begin     | 结束   | end     | 
| 备份 | backup    | 恢复   | restore | 
| 导入 | import    | 导出   | export  | 
| 分割 | split     | 合并   | merge   |
| 注入 | inject    | 提取   | extract |
| 下载 | download  | 上传   | upload  | 
| 更新 | update    | 复原   | revert  | 
| 锁定 | lock      | 解锁   | unlock  | 
| 签出 | check out | 签入   | check in| 
| 推   | push      | 拉     | pull    |
| 展开 | expand    | 折叠   | collapse| 
| 开始 | begin     | 结束   | end     |
| 进入 | enter     | 退出   | exit    |
| 放弃 | abort     | 离开   | quit    |
| 输入 | input     | 输出   | output  |
| 压缩 | compress  | 解压   | decompress  |
| 打包 | pack      | 解包   | unpack  |
| 编码 | encode    | 解码   | decode  |
| 加密 | encrypt   | 解密   | decrypt |
| 连接 | connect   | 断开   | disconnect  |

### 1.2 近义词/相关词

| 含义 | 单词     | 近义词、相关词 | 近义词、相关词单词 |
| :--- | :------- | :------------- | :----------------  |
| 启动 | launch   | 运行           | run                |
| 编译 | compile  | 执行           | execute            | 
| 调试 | debug    | 跟踪           | trace              | 
| 观察 | observe  | 监听           | listen             | 
| 构建 | build    | 发布           | publish            | 
| 解析 | parse    | 生成           | emit               | 
| 发送 | send     | 接收           | receive            | 
| 刷新 | refresh  | 同步           | synchronize        |
| 提交 | submit   | 交付           | commit             | 
| 废弃 | obsolete | 废旧           | depreciate         | 


## 2. 返回真伪值的方法

注：

- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix | is | 对象是否符合、满足 xxx 状态。<br>返回 true 表示符合 xxx 状态。 | isClosed |
| Prefix | can | 对象是否能执行 xxx 动作。<br>返回 true 表示能执行 xxx  操作。 | canRemove |
| Prefix | should | <small>（接下来）</small>是否应该执行 xxx 方法。<br> 返回 true 表示<small>（接下来）</small>应该执行。| shouldMigrate |
| Prefix | has | 对象是否有所期待的数据和属性。<br> 返回 true 表示拥有 xxx 数据、属性。 | hasObservers|
| Prefix | needs | 调用方是否需要执行某个命令（或方法）| needsMigrate |


## 3. 用来检查的方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix | ensure | 检查是否为期待的 xxx 状态，不是则抛出异常或返回错误码 | ensureCapacity |
| Prefix | validate | 检查是否为正确的 xxx 状态，不是则抛出异常或返回错误码 | validateInputs |


## 4. 按需求才执行的方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Suffix | IfNeeded | 需要的时候执行，不需要的时候什么都不做 | drawIfNeeded |
| Prefix | might | 同上 | mightCreate |
| Prefix | try | 尝试执行，失败的时候抛出异常或返回 error code | tryCreate |
| Suffix | OrDefault | 尝试执行，失败时返回默认值 | getOrDefault |
| Suffix | OrElse | 尝试执行，失败时返回参数中指定的值 | getOrElse |
| Prefix | force | 强制尝试执行，失败时抛出异常或返回 error code | forceCreate，forceStop |

## 5. 与集合操作相关的方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix or Alone | contains | 是否持有相关对象 | contains | 
| Prefix or Alone | add | 添加 | addJob|
| Prefix or Alone | append | 添加 | appendJob |
| Prefix or Alone | insert | 插入到指定下标处 | insertJob|
| Prefix or Alone | put | 添加与 key 对应元素 | putJob|
| Prefix or Alone | remove | 移除元素 | removeJob |
| Prefix or Alone | enqueue | 添加到队列的最末尾 | enqueueJob |
| Prefix or Alone | dequeue | 从队列头部移除 | dequeueJob |
| Prefix or Alone | push | 添加到栈头部 | pushJob |
| Prefix or Alone | pop | 从栈头部移除 | popJob |
| Prefix or Alone | peek | 获取栈头部数据（不移除）| peekJob |
| Prefix or Alone | find | 寻找符合条件的某物（单个） | findJob |
| Prefix or Alone | list | 寻找符合条件的某物（多个） | listJob |


## 6. 与数据相关的方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix or Alone | create | 新创建 | createAccount | 
| Prefix or Alone | new | 新创建 | newAccount | 
| Prefix or Alone | from | 从既有的某物新建，或依据其它的数据新建 | fromConfig | 
| Prefix or Alone | to | 转换 | toString | 
| Prefix or Alone | update | 更新既有某物 | updateAccount | 
| Prefix or Alone | load | 读取 | loadAccount | 
| Prefix or Alone | fetch | 远程读取 | fetchAccount | 
| Prefix or Alone | delete | 删除 | deleteAccount | 
| Prefix or Alone | remove | 删除 | removeAccount | 
| Prefix or Alone | save | 保存 | saveAccount | 
| Prefix or Alone | store | 保存 | storeAccount | 
| Prefix or Alone | commit | 保存 | commitAccount | 
| Prefix or Alone | apply | 保存或应用 | applyChange | 
| Prefix or Alone | clear | 清除，或恢复到初始状态 | clearAll | 



## 7. 异步相关方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix | blocking | 线程阻塞方法 | blockingGetUser |
| Suffix | InBackground | 执行在后台的线程 | doInBackground |
| Suffix | Async | 异步方法 | sendAsync |
| Suffix | Sync | 同步方法 | sendSync |
| Prefix or Alone | schedule | Job 和 Task 放入队列 | schedule，scheduleJob |
| Prefix or Alone | post | 同上 | postJob |
| Prefix or Alone | execute | 执行同步方法 | execute，executeTask |
| Prefix or Alone | cancel | 停止同步方法 | cancel，cancelTask |
| Prefix or Alone | start | 执行异步方法 | start，startJob |
| Prefix or Alone | stop | 停止异步方法 | stop，stopTask |

## 8. 回调方法

注：
- Prefix: 前缀
- Suffix: 后缀
- Alone: 单独使用

| 位置 | 单词 | 意义 | 示例 |
| :- | :- | :- | :- |
| Prefix | on | 放发生事件时执行 | onCompleted |
| Prefix | before / pre / will | 事情发生前执行 | beforeUpdate |
| Prefix | after / post / did | 事件发生后执行 | afterUpdate |
| Prefix | should | 确认事件是否可以发生时 | shoudupdate |

## 9. 操作对象生命周期方法

| 单词 | 意义 | 示例 |
| :- | :- | :- |
| initialize | 初始化 | linitialize |
| pause | 暂停 | onPause| 
| stop | 停止 | onStop |
| destroy | 销毁 | destroy |
| abandon | 同上 | abandon |
| dispose | 同上 | dispose |

## 10. 测试方法 

测试方法有 2 种风格：

- 以 `test` 开头，后接测试方法名，如 **testClearAll** 。
- 以 `test` 结尾，后接测试方法名，如 **clearAllTest** 。


