负载均衡<small>（ load balance ）</small>就是将负载分摊到多个操作单元上执行，从而提高服务的可用性和响应速度，带给用户更好的体验。

## 1. 负载均衡的配置

通过 Nginx 中的 **upstream** 指令可以实现负载均衡，再该指令中能够配置负载均衡服务器组。

目前负载均衡有 4 种典型的配置方式。分别是：

| # | 负载均衡方式 | 特点 | 
| :-: | :- | :- |
| 1 | 轮询方式 | 默认方式。每个请求按照时间顺序逐一分配到不同的后端服务器进行处理。如果有服务器宕机，会自动删除。|
| 2 | 权重方式 | 利用 weight 指定轮循的权重比率，与访问率成正比。用于后端服务器性能不均衡的情况。|
| 3 | ip_hash 方法 | 每个请求俺早访问 IP 的 hash 结果分配，这样可以使每个方可固定一个后端服务器，可以解决 Session 共享问题。 |
| 4 | 第三方模块 | 取决于所采用的第三方模块的分配规则。 |

在 upstream 指定的服务器组中，若每个服务器的权重都设置为 1<small>（默认值）</small>时，表示当前的负载均衡是一般轮循方式。


## 2. 准备工作

编写后台<small>（SpringBoot）</small>项目，简单起见，以占用不同的端口的形式启动 2 次，并在返回的信息中返回各自所占用的端口号。

```java
@Value("${server.port}")
String port;

@RequestMapping("/api/hello")
public Map<String, String> index(HttpServletRequest request) {
    HashMap<String, String> map = new HashMap<>();
    map.put("code", "10086");
    map.put("msg", "success");
    map.put("data", this.port);

    return map;
}
```

## 3. 负载：轮循

轮循方式的关键配置如下：

```text
server {
    ...
    location /api {
        proxy_pass http://xxx/api;
        ...

         
        # 解决 upstream 名称携带下划线（_）时，访问 400 问题。
        # proxy_set_header Host $http_host;
    }
}

upstream xxx {
    server 127.0.0.1:8080;
    server 127.0.0.1:9090;
}
```

上述的配置中有 2 点需要注意的：

1. `upstream` 配置项在 `http` 配置项内，但是在 `server` 配置项外，它们 3 者整体结构如下<small>（ 不要写错地方了 ）</small>：

  ```text
  http {                    # http 是爹
      server { ... }        # 它们俩是两儿子
      upstream { ...}
  }
  ```

2. 你所配置的 `upstream` 的 name 是自定义的，但是不要出现 `-` 号，否则会和 tomcat 有冲突。

你持续访问 `http://127.0.0.1/api/hello` 你会发现页面的内容会是交替出现 `8080` 端口和 `9090` 端口。


## 4. 负载：加权轮循

加权轮循就是在轮循的基础上，为每个单点加上权值。权值越重的单点，承担的访问量自然也就越大。

```text
upstream xxx {
    server 127.0.0.1:8080 weight=1;
    server 127.0.0.1:9090 weight=2;
}
```

按照上述配置，`9090` 端口的服务将承担 2/3 的访问量，而 `8080` 端口则承担 1/3 的访问量。

将配置改为上述样子并重启 Nginx 后，再持续访问 `http://127.0.0.1/api/hello` 你会发现 `8080` 端口和 `9090` 端口会以 `1-2-1-2-...` 的次数交替出现。


---


除了 **weight** 外，常见的状态参数还有：

| 配置方式 | 说明 |
| :- | :- |
| max_fails | 允许请求失败次数，默认为 1 。通常和下面的 fail_timeout 连用。 |
| fail_timeout | 在经历了 max_fails 次失败后，暂停服务的时长。<small>这段时间内，这台服务器 Nginx 不会请求这台 Server</small> |
| backup | 预留的备份机器。<small>它只有在其它非 backup 机器出现故障时或者忙碌的情况下，才会承担负载任务。</small>|
| down | 表示当前的 server 不参与负载均衡。|

例如：

```text
upstream web_server {
    server 192.168.78.128 weight=1 max_fails=1 fail_timeout=30s;
    server 192.168.78.200 weight=2 max_fails=1 fail_timeout=30s;
    server 192.168.78.201 backup;
    server 192.168.78.210 down;
}
```


## 5. 负载：ip_hash 负载

ip_hash 方式的负载均衡，是将每个请求按照访问 IP 的 hash 结果分配，这样就可以使来自同一个 IP 的客户端固定访问一台 Web 服务器，从而就解决了 Session 共享问题。

```text
upstream xxx {
    ip_hash;
    server 127.0.0.1:8080;
    server 127.0.0.1:9090;
}
```

使用上例配置后，你会发现无论你请求多少次 `http://127.0.0.1/api/hello` 你所看到的端口始终是 `8080` 和 `9090` 中的某一个。


## 6. 将客户端浏览器的 IP 传递到后台 

> 了解

对于后台而言，它所面对的『**客户端**』就是 Nginx，后台看不见『**客户端**』浏览器。

这就意味着，你如果你需要在后台获取客户端浏览器的 IP 地址，你需要明确指出让 Nginx 『**额外地多携带**』一些数据。

```text
location /api {
    proxy_pass http://xxx/api;
    proxy_set_header X-Real-IP $remote_addr;

    # 解决 nginx 不会“转发” Cookie 问题
    # proxy_set_header Cookie $http_cookie;
 
    # 解决 upstream 名称携带下划线（_）时，访问 400 问题。
    # proxy_set_header Host $http_host;
 
    # proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
}
```

在 Spring Boot 的 Controller 中你有 2 种方式来获得这个额外的信息：

```java
public Map<String, String> index(
        HttpServletRequest request,
        @RequestHeader("X-Real-IP") String realIP2) {
    String realIP1 = request.getHeader("X-Real-IP");
    ...
}
```
