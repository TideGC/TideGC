---
alias:
  - 我司的 RESTful API 规范
tags:
  - 规范
---

# 我司的 RESTful API 规范

## 1. URI 设计

### 1. 请求方法

- 对于不会造成服务器状态<small>（数据库数据）</small>变动的请求<small>（例如，查）</small>，使用 GET 请求；

- 对于会造成服务器状态<small>（数据库数据）</small>变动的请求<small>（例如，增删改）</small>，使用 POST 请求。

### 2. URI 串行命名法

- URL 采用全小写的串行命名法；

- 请求参数采用驼峰命名<small>（无论是 query-string 还是 json-string），方便与后端 Java 代码对接</small>。

### 3. URI 只允许在末端出现动词

由于我们使用 POST 请求提交多种途径的操作<small>（增删改）</small>，而不是使用 PUT、PATCH、DELETE 等请求方法，所以，为了便于 URI 表达出应有的含义，我们允许 URI 中出现动词，但只能出现在 URI 的末端<small>（即，最后一个 / 部分）</small>。

例如：

- 发布一篇文章：`POST /articles/<id>/publish`

- 重新发送邮件：`POST /mails/<id>/resend` 

### 4. URI 中资源名词为复数

URI 代表资源，那么我们对资源的操作可能是单个的操作，也可能是批量的操作，所以统一使用复数更不容易产生歧义。

例如：

- 获取某一篇文章：`GET /articles?authorId=2&articleName=book`
- 获取该作者所有文章： `GET /articles?authorId=2`

### 5. URI 中只能嵌 ID

URI 中的名词代表的是资源，因此作为资源的属性的名称不能出现在 URI 中，特别是不能作为查询条件，例如下例就是错误的：

```
GET /articles/author/tommy
```

上述错误示例，应该改写为：`/articles?author=tommy`

另外，嵌入的 ID 也必须是当前资源自己的 ID，而不能是它的属性的 ID ，例如下例就是错误的：

```
GET /articles/authors/1
```

上述错误示例想表达的含义是想查询 ID 编号是 1 的作者的所有文章，但是 URL 设计错误。可以改成如下 2 种：

```
GET /authors/1/articles
GET /articles?authorId=1
```


### 6. URI 最后不要有 /

URI 的最后可以有 / ，也可以没有，例如：

```
GET /articles/
GET /authors/2/

GET /articles
GET /authors/2
```

但是有还是没有，前端和后端要统一。这里，我们统一不加 / 。


## 2. 响应设计

### 1. 响应状态码和响应信息

响应基本格式：

```js
{
  code: 10000,
  data: {
    message: "success"
  }
}
```

其中

- **code** 代表请求处理状态编码。

  10000 表示处理成功，非 10000 表示处理失败。

  为了避免重用 http 响应状态码导致的逻辑混乱，这里我们避免使用 http 协议的状态编码数字。用 10000 表示成功，各个模块各自约定非 10000 编码所代表的错误逻辑。


- **data.message** 代表与请求处理状态编码配套的文字信息。

  - code=10000 && data.message="success": 请求处理成功

  - code=10000 && data.message!="success": 请求处理成功, 普通消息提示：message 的实际内容

  - code!=10000: 请求处理失败，警告消息提示：message 的实际内容


### 2. 返回实体

响应实体格式：

```js
{
  code: 10000,
  data: {
    message: "success",
    entity: {
      id: 1,
      name: "XXX",
      code: "XXX"
    }
  }
}
```

**data.entity** 的内容就是响应返回的实体数据（单个）

### 3. 返回实体列表

响应列表格式：

```js
{
  code: 10000,
  data: {
    message: "success",
    list: [{
      id: 1,
      name: "XXX",
      code: "XXX"
    }, {
      id: 2,
      name: "XXX",
      code: "XXX"
    }]
  }
}
```

**data.list** 的内容就是响应返回的实体数据的列表。

### 4. 响应带分页信息

```js
{
  code: 10000,
  data: {
    message: "success",
    recordCount: 2,
    totalCount: 2,
    pageNo: 1,
    pageSize: 10,
    totalPage: 1
    list: [{
      id: 1,
      name: "XXX",
      code: "H001"
    }, {
      id: 2,
      name: "XXX",
      code: "H001"
    }],
  }
}
```

- data.recordCount: 当前页记录数 
- data.totalCount: 总记录数 
- data.pageNo: 当前页码 
- data.pageSize: 每页大小 
- data.totalPage: 总页数

> [!tip]
> 具体有些什么属性、需要什么属性，可以借鉴 Myabtis PageHelper 的 PageInfo 。

## 3. 特殊内容规范

### 1. 关于特殊的条件查询

条件支持 =、!=、>、>=、<、<=、like、not like、in、not in 。

对应的操作符为 eq 、neq 、gt 、gte 、lt 、lte 、like 、nlike 、in 、nin 。


参数设置文式为

```
name[eq]=xiaomei&money[gt]=100&nick[like]=%25liu&id[in]=12,13,14,15,16
```

上例中的，`%25`表示的是编码后的百分号。

如果查询参数和控制参数不冲突且为等于，则可以删除操作符，如：name=tommy


### 2. 关于下拉框、复选框、单选框的选中

如果是由后端接口统一逻辑判定是否选中，那么通过 **isSelected** 标示是否选中，示例如下：

```js
{
  code: 10000,
  data: {
    message: "success",
    list: [
      {
        id: 1,
        name: "XXX",
        code: "XXX",
        isSelected: 1
      },
      {
        id: 1,
        name: "XXX",
        code: "XXX",
        isSelected: 0
      }
    ]
  }
}
```

下拉框、复选框、单选框的 **禁止** 判定则是由前端来处理。

### 3. 关于 Boolean 类型

关于 Boolean 类型，JSON 数据传输中一律使用 1 / 0 来标示：

- 1 表示 true ；
- 0 表示 false 。

### 4. 关于日期类型

关于日期类型，JSON 数据传输中一律使用字符串，具体日期格式因业务而定。


