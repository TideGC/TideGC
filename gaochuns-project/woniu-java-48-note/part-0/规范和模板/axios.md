## Axios 发起 AJAX 请求模板

html 页面引入 Axios<small>（使用 CDN）</small>：

``` html
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
```

### 1. 发起 AJAX GET 请求

````ad-cite
title: 原始的通用方式
collapse: close

```js
axios({
    baseURL: '/servlet-example',// 会拼接在 url 的前面
    url: '/hello.do',
    method: 'get',              // 默认值
    params: {                   // 请求参数
        username: 'tom',
        password: '123'
    }
}).then(response => {
    console.log(response);
}).catch(error => {
    console.log(error);
});
```
````

````ad-cite
title: 自己拼 query-string
collapse: close

```js
axios
    .get('/servlet-example/hello.do?username=tom&password=123')
    .then(response => {
        console.log(response);
    })
    .catch(error => {
        console.log(error);
    });
```
````

````ad-cite
title: 利用 params 参数自动转 query-string
collapse: close

```js
const obj = {
  username: 'tom',
  password: '123'
};

axios
    .get('/servlet-example/hello.do', { params: obj })
    .then(response => {
        console.log(response);
    })
    .catch(error => {
        console.log(error);
    });
```
````


### 2. 发起 AJAX POST 请求

> [!danger] 警告
> axios post 请求的 content-type 的默认值就是 application/json ，即，默认传递的参数格式是 json-string ！

````ad-cite
title: 利用 qs 库转 query-string
collapse: close

在页面上引入 qs 库：

``` html
<script src="https://cdn.bootcss.com/qs/6.7.0/qs.min.js"></script>
```

示例代码：

```js
const obj = {
  username: 'tom',
  password: '123456'
};

// 引入 Qs 来帮我们将对象转为 query-string 。
const queryString = Qs.stringify(obj);

axios({
  baseURL: '/servlet-example',    // 会拼接在 url 的前面
  url: '/hello.do',
  method: 'post',
  headers: { 'content-type': 'application/x-www-form-urlencoded' },
  data: queryString   // 注意是 data:... ，不是 get 请求的 params: ...
})
.then(response => {
    console.log(response);
}).catch(error => {
    console.log(error);
});
```
````

````ad-cite
title: 利用 ES6 URLSearchParams 转 query-string
collapse: close

```js
const obj = new URLSearchParams({
  username: 'tom',
  password: '123456'
});
const queryString = obj.toString(); 

axios({
  baseURL: '/servlet-example', 
  url: '/hello.do',
  method: 'post',
  headers: { 'content-type': 'application/x-www-form-urlencoded' },
  data: queryString   // 注意是 data:... ，不是 get 请求的 params: ...
})
.then(response => {
    console.log(response);
}).catch(error => {
    console.log(error);
});
```
````

### 3. 发起 AJAX POST 请求 

````ad-cite
title: 原始的通用方式
collapse: close

```js
const obj = {
  username: 'tom',
  password: '123456'
};

axios({
  baseURL: '/servlet-example',    // 会拼接在 url 的前面
  url: '/hello.do',
  method: 'post',
  headers: {'content-type': 'application/json'}, // 默认值，可省略
  data: obj
}).then(response => {
  console.log(response);
}).catch(error => {
  console.log(error);
});
```
````

````ad-cite
title: 简写
collapse: close

```js
axios
  .post('/servlet-example/hello.do', obj)
  .then(response => {
      console.log(response);
  }).catch(error => {
      console.log(error);
  });
```
````

````ad-cite
title: json-string 的 Java Servlet 后台代码
collapse: close

```java
System.out.println(request.getMethod());
System.out.println(request.getParameter("username"));   // null
System.out.println(request.getParameter("password"));   // null

BufferedReader br = request.getReader();

String str, wholeStr = "";
while ((str = br.readLine()) != null) {
    wholeStr += str;
}
System.out.println(wholeStr);   // json-string 格式的请求参数。

PrintWriter out = response.getWriter();
out.write("hello world");
```
````

