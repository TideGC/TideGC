## 1. 反向代理服务器

### 1.1 概念

由于请求的方向是从客户端发往服务端，因此 `客户端 -> 服务端` 这个方向是『正向』。

所谓『反向代理服务器』指的就是：Nginx『站』在服务端的角度，分担了服务端的负担，增强了服务端的能力。

> 在这种情况下，在客户端看来，`Nginx` + `服务端` 整体扮演了一个更大意义上的服务端的概念。

![nginx-01](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171813.png)


### 1.2 基于 Nginx 的动静分离方案

对 Nginx 的最简单的使用是将它用作静态资源服务器。

![nginx-02](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171811.png)

在这种方案种，将 `.html`、`.css`、`.js`、`.png` 等静态资源放置在 Nginx 服务器上。

将对静态资源的访问流量就分流到了 Nginx 服务器上，从而减轻 Servlet 容器的访问压力。

### 1.3 基于 Nginx 的前后端分离

随着前端单页应用技术的发展，『前端』从简单的『前端页面』演进成了『前端项目』。

这种情况下，在动静分离方案的基础上进一步延伸出了『更激进』的方案：前后端分离。

![nginx-03](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627171809.png)

### 1.4 实现原理 

要实现前后端分离<small>（涵盖动静分离）</small>，这里需要 Nginx 能提供一种能力：请求转发。

在整个过程中，所有的请求首先都是『交到了 Nginx 手里』，有一部分请求是 Nginx 自己能响应的，它就响应了；而另一部分请求则是被 Nginx 转给了 SpringBoot，而等到 Nginx 获得到 SpringBoot 的 JSON 的返回之后，Nginx 再将响应数据回复给客户端。

## 2. Nginx 代理（转发）配置 

> [!note] 提示
> 所谓『代理』指的就是 Nginx『帮』真正的服务端所接收的请求，那么也就意味着这样的请求，Nginx 最终需要再交给真正的服务端去处理。

### 2.1 两个需要提前交代的问题

1. 「减法」问题

   在处理转发请求时，Nginx 常常对 URL 做一个「减法」操作，即，减去 URL 中的协议、IP 和端口部分，然后再使用剩下的部分。例如：

    - URL `http://127.0.0.1:8080` 做减法后啥，都不剩；
    - URL `http://127.0.0.1:8080/` 做减法后，还剩 `/` ；
    - URL `http://127.0.0.1:8080/api` 做减法后，还剩 `/api` ；
    - URL `http://127.0.0.1:8080/api/` 做减法后，还剩 `/api/` 。

1. 「规则 2 选 1」问题：

    用户的原始 URL 会被 Nginx「加工」成什么样子？请求会被转发到谁那里？有 2 套规则，具体是哪套规则起作用取决于你的 location 中的 **proxy_pass 做「减法」后还剩不剩东西**？例如

    - URL 1：`http://127.0.0.1:8080` 
    - URL 2：`http://127.0.0.1:8080/` 
    - URL 3：`http://127.0.0.1:8080/api`
    - URL 4：`http://127.0.0.1:8080/api/`

    上面 4 个 URL ，后 3 个 URL 使用同一个规则，而第 1 个 URL 则使用的是另一个规则。

### 结论先行

在 Nginx 的请求转发的配置块中有一个 proxy_pass 配置项，它的值就是转发的目标服务器的地址。形如，http://192.172.0.200 。

Nginx 的请求转发规则有 2 种，当 Nginx 收到一个待转发的请求后，是使用哪一个规则将它转给目标服务取决于 proxy_pass 配置项『做减法』的结果！

- 如果 proxy_pass『做减法』后，啥都不剩，则使用规则一：直接拼出目标路径。

![](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20221018182207.png)


- 如果 proxy_pass『做减法』后，还剩了点啥，则使用规则二：先做一个减法加工，再拼出目标路径。

![](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20221018182602.png)


> [!important] 对于第二个规则的一个配置技巧
> 如果你的 proxy_pass 是以 `/` 结尾的<small>（无论它是长是短）</small>，那么你的 location 最好也以 `/` 结尾，以避免不必要的麻烦。

## 3. 验证转发规则
### 3.1 验证规则的准备工作

编写一个 Controller 其中暴露 2 个 URI ，一个故意有 /api ，一个则没有。

```java
@RequestMapping("/login")  
public String login1() {  
    System.out.println("login without /api");  
    return "login without /api";  
}  
  
@RequestMapping("/api/login")  
public String login2() {  
    System.out.println("login with /api");  
    return "login with /api";  
}
```


### 3.2 验证规则一

> 直接拼，不"砍" /api 。

#### 第 1 步：加 Nginx 配置

为 Nginx 的配置文件添加如下配置段：

```ini
location /api {
  proxy_pass http://192.172.0.200:8080;
}
```

#### 第 2 步：分析

由于配置中的 proxy_pass 配置项的值『做减法』后，啥都不剩，所以，使用"直接拼"的规则。**就是 URI 拼 proxy_pass** 。

URI 是 /api/xxx，proxy_pass 是 [http://192.172.0.200:8080](http://192.172.0.200:8080)，两者一拼就是：[http://192.172.0.200:8080/api/xxx](http://192.172.0.200:8080/api/xxx) 。

所以，Nginx 就把<small>（收到的以 /api 开头的）</small>请求转到了 [http://192.172.0.200:8080/api/xxx](http://192.172.0.200:8080/api/xxx) 。

#### 第 3 步：验证

重启 Nginx<small>（触发重新加载配置文件）</small>：

```sh
# kill
taskkill /f /t /im nginx.exe

# 再次启动
start nginx

# 观察启动
tasklist /fi "imagename eq nginx.exe"
```

向 Nginx 发出请求：[http://127.0.0.1:80/api/login](http://127.0.0.1:80/api/login) 。

观察返回结果和控制台的输出，你会发现触发执行的是 @RequestMapping("/api/login") 。

> /api 没有被"砍掉"。

### 3.3 验证规则二

> 先减，再拼，会"砍" /api 。

#### 第 1 步：加 Nginx 配置

为 Nginx 的配置文件添加如下配置段：

```ini
location /api/ {
    proxy_pass http://192.172.0.200:8080/;
}
```

#### 第 2 步：分析

由于配置中的 proxy_pass 配置项的值『做减法』后，还有东西剩下，所以，使用"先减后拼"的规则。**就是 URI 减 location，再拼 proxy_pass** 。

URI 是 /api/xxx，location 是 /api/ ，两者先减就是：xxx 。

URI 减 location 的结果<small>（xxx）</small>再和 proxy_pass 拼，结果就是：[http://192.172.0.200:8080/xxx](http://192.172.0.200:8080/xxx) 。

所以，Nginx 就把<small>（收到的以 /api 开头的）</small>请求转到了 [http://192.172.0.200:8080/xxx](http://192.172.0.200:8080/xxx) 。

#### 第 3 步：验证

重启 Nginx<small>（触发重新加载配置文件）</small>：

```sh
# kill
taskkill /f /t /im nginx.exe

# 再次启动
start nginx

# 观察启动
tasklist /fi "imagename eq nginx.exe"
```

向 Nginx 发出请求：[http://127.0.0.1:80/api/login](http://127.0.0.1:80/api/login) 。

观察返回结果和控制台的输出，你会发现触发执行的是 @RequestMapping("/login") 。

> /api 被砍掉了


## 4. 一个完整的 http 配置片段

其中绝大多数内容都是默认配置：

```
error_log  logs/error.log  info;    # 打开错误日志的 INFO 级别，方便观察错误信息。
…
http {
    include       mime.types;
    default_type  application/octet-stream;
    sendfile        on;
    keepalive_timeout  65;  # TCP 连接存活 65 秒
    server {
        # Nginx 监听 localhost:80 端口
        listen       80;
        server_name  localhost;

        # 访问 URI 根路径时，返回 Nginx 根目录下的 html 目录下的 index.html 或 index.htm
        location / {
            root   html;
            index  index.html index.htm;
        }

        # URI 路径以 /api 开头的将转交给『别人』处理
        location /api {
            proxy_pass http://localhost:8080/api;
        }

        # 出现 500、502、503、504 错误时，返回 Nginx 根目录下的 html 目录下的 50x.html 。
        error_page   500 502 503 504  /50x.html;    
        location = /50x.html {
            root   html;
        }
    }
}
```