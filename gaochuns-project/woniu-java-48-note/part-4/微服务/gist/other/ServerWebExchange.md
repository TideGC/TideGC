# WebFlux 的 ServerWebExchange

```expander
file:202208240809
$lines
```
## ServerWebExchange 接口说明

tags: #gateway #webflux #ServerWebExchange

ServerWebExchange 的注释： 

> [!cite] 提示
> ServerWebExchange 是『Spring Reactive Web 世界中』HTTP 请求与响应交互的契约。提供对 HTTP 请求和响应的访问，并公开额外的服务器端处理相关属性和特性，如请求属性。

其实，ServerWebExchange 命名为『**服务网络交换器**』，存放着重要的请求-响应属性、请求实例和响应实例等等，有点像 Servlet 中的 Context 的角色。

```java
public interface ServerWebExchange {

    // 日志前缀属性的 KEY，值为 org.springframework.web.server.ServerWebExchange.LOG_ID
    // 可以理解为 attributes.set("org.springframework.web.server.ServerWebExchange.LOG_ID", "日志前缀的具体值");
    // 作用是打印日志的时候会拼接这个 KEY 对应的前缀值，默认值为 ""
    String LOG_ID_ATTRIBUTE = ServerWebExchange.class.getName() + ".LOG_ID";
    String getLogPrefix();

    // 获取 ServerHttpRequest 对象
    ServerHttpRequest getRequest();

    // 获取 ServerHttpResponse 对象
    ServerHttpResponse getResponse();
    
    // 返回当前 exchange 的请求属性，返回结果是一个可变的 Map
    Map<String, Object> getAttributes();
    
    // 根据 KEY 获取请求属性
    @Nullable
    default <T> T getAttribute(String name) {
        return (T) getAttributes().get(name);
    }

    // 根据 KEY 获取请求属性，做了非空判断
    @SuppressWarnings("unchecked")
    default <T> T getRequiredAttribute(String name) {
        T value = getAttribute(name);
        Assert.notNull(value, () -> "Required attribute '" + name + "' is missing");
        return value;
    }

     // 根据 KEY 获取请求属性，需要提供默认值
    @SuppressWarnings("unchecked")
    default <T> T getAttributeOrDefault(String name, T defaultValue) {
        return (T) getAttributes().getOrDefault(name, defaultValue);
    } 

    // 返回当前请求的网络会话
    Mono<WebSession> getSession();

    // 返回当前请求的认证用户，如果存在的话
    <T extends Principal> Mono<T> getPrincipal();  

    // 返回请求的表单数据或者一个空的 Map，只有 Content-Type为application/x-www-form-urlencoded 的时候这个方法才会返回一个非空的 Map  --  这个一般是表单数据提交用到
    Mono<MultiValueMap<String, String>> getFormData();   

    // 返回 multipart 请求的 part 数据或者一个空的 Map，只有 Content-Type 为 multipart/form-data 的时候这个方法才会返回一个非空的 Map  --  这个一般是文件上传用到
    Mono<MultiValueMap<String, Part>> getMultipartData();

    // 返回 Spring 的上下文
    @Nullable
    ApplicationContext getApplicationContext();   

    // 这几个方法和 lastModified 属性相关
    boolean isNotModified();
    boolean checkNotModified(Instant lastModified);
    boolean checkNotModified(String etag);
    boolean checkNotModified(@Nullable String etag, Instant lastModified);

    // URL 转换
    String transformUrl(String url);    
  
    // URL 转换映射
    void addUrlTransformer(Function<String, String> transformer); 

    // 注意这个方法，方法名是：改变，这个是修改 ServerWebExchange 属性的方法，返回的是一个 Builder 实例，Builder 是 ServerWebExchange 的内部类
    default Builder mutate() {
         return new DefaultServerWebExchangeBuilder(this);
    }

    // ServerWebExchange 构造器
    interface Builder {      
         
        // 覆盖 ServerHttpRequest
        Builder request(Consumer<ServerHttpRequest.Builder> requestBuilderConsumer);
        Builder request(ServerHttpRequest request);
        
        // 覆盖 ServerHttpResponse
        Builder response(ServerHttpResponse response);
        
        // 覆盖当前请求的认证用户
        Builder principal(Mono<Principal> principalMono);
    
        // 构建新的 ServerWebExchange 实例
        ServerWebExchange build();
    }
}
```

注意到 **ServerWebExchange#mutate** 方法，ServerWebExchange 实例可以理解为不可变实例。

如果我们想要修改它，需要通过 **ServerWebExchange#mutate** 方法生成一个新的实例，后面会修改请求以及响应时会用到，暂时不做介绍。

<-->

```expander
file:202208240811
$lines
```
## ServerWebExchange 对比 Servlet

tags: #gateway #webflux #ServerWebExchange

这里总结部分我在写代码中遇到的一些不同与相应代替办法

### request.setAttribute("key", "value") 的替代

ServerHttpRequest 中并无 Attribute 相关操作，要通过 exchange 来操作。
    
exchange.getAttributes().put(「key」, "value"); 

### request.getHeader("test") 的替代

request.getHeaders().getFirst(「test」)
    
遍历 Header 如下：

```java
HttpHeaders headers = request.getHeaders();
for (Map.Entry<String,List<String>> header:headers.entrySet()) {
  String key = header.getKey();
  List<String> values = header.getValue()；
}
```



<-->


```expander
file:202208240813
$lines
```
## ServerHttpRequest 及其"祖先"

tags: #gateway #webflux #ServerWebExchange 

ServerHttpRequest 实例是用于承载请求相关的属性和请求体，Spring Cloud Gateway 中底层使用 Netty 处理网络请求。

通过追溯源码，可以从 ReactorHttpHandlerAdapter 中得知 ServerWebExchange 实例中持有的 ServerHttpRequest 实例的具体实现是 ReactorServerHttpRequest 。

之所以列出这些实例之间的关系，是因为这样比较容易理清一些隐含的问题，例如：ReactorServerHttpRequest 的父类 AbstractServerHttpRequest 中初始化内部属性 headers 的时候把请求的 HTTP 头部封装为只读的实例：

```java
// HttpHeaders 类中的 readOnlyHttpHeaders 方法，其中 ReadOnlyHttpHeaders 屏蔽了所有修改请求头的方法，直接抛出 UnsupportedOperationException
public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
    Assert.notNull(headers, "HttpHeaders must not be null");
    if (headers instanceof ReadOnlyHttpHeaders) {
        return headers;
    } else {
        return new ReadOnlyHttpHeaders(headers);
    }
}
```

### ServerHttpRequest 接口

```java
public interface ServerHttpRequest extends HttpRequest, ReactiveHttpInputMessage {
    
    // 连接的唯一标识或者用于日志处理标识
    String getId();   
    
    // 获取请求路径，封装为 RequestPath 对象
    RequestPath getPath();
    
    // 返回查询参数，是只读的 MultiValueMap 实例
    MultiValueMap<String, String> getQueryParams();

    // 返回 Cookie 集合，是只读的 MultiValueMap 实例
    MultiValueMap<String, HttpCookie> getCookies();  
    
    // 远程服务器地址信息
    @Nullable
    default InetSocketAddress getRemoteAddress() {
       return null;
    }

    // SSL 会话实现的相关信息
    @Nullable
    default SslInfo getSslInfo() {
       return null;
    }  
    
    // 修改请求的方法，返回一个建造器实例 Builder，Builder 是内部类
    default ServerHttpRequest.Builder mutate() {
        return new DefaultServerHttpRequestBuilder(this);
    } 

    interface Builder {

        // 覆盖请求方法
        Builder method(HttpMethod httpMethod);
         
        // 覆盖请求的 URI、请求路径或者上下文，这三者相互有制约关系，具体可以参考 API 注释
        Builder uri(URI uri);
        Builder path(String path);
        Builder contextPath(String contextPath);

        // 覆盖请求头
        Builder header(String key, String value);
        Builder headers(Consumer<HttpHeaders> headersConsumer);
        
        // 覆盖 SslInfo
        Builder sslInfo(SslInfo sslInfo);
        
        // 构建一个新的 ServerHttpRequest 实例
        ServerHttpRequest build();
    }         
}
```

### ServerHttpRequest 接口的父接口之一：HttpRequest 接口

```java
public interface HttpRequest extends HttpMessage {

    // 返回 HTTP 请求方法，解析为 HttpMethod 实例
    @Nullable
    default HttpMethod getMethod() {
        return HttpMethod.resolve(getMethodValue());
    }
    
    // 返回 HTTP 请求方法，字符串
    String getMethodValue();    
    
    // 请求的 URI
    URI getURI();
}    
```

### ServerHttpRequest 接口的父接口之二：ReactiveHttpInputMessage 接口

```java
public interface ReactiveHttpInputMessage extends HttpMessage {
    
    // 返回请求体的 Flux 封装
    Flux<DataBuffer> getBody();
}
```

### ServerHttpRequest 接口的"爷爷"接口：HttpMessage 接口

```java
public interface HttpMessage {

    // 获取请求头，目前的实现中返回的是 ReadOnlyHttpHeaders 实例，只读
    HttpHeaders getHeaders();
}    



```

<-->

```expander
file:202208240819
$lines
```
tags: #gateway #webflux #ServerWebExchange 


## ServerHttpResponse 及其"祖先"

ServerHttpResponse 实例是用于承载响应相关的属性和响应体，Spring Cloud Gateway 中底层使用 Netty 处理网络请求。

通过追溯源码，可以从 ReactorHttpHandlerAdapter 中得知 ServerWebExchange 实例中持有的 ServerHttpResponse 实例的具体实现是 ReactorServerHttpResponse 。

之所以列出这些实例之间的关系，是因为这样比较容易理清一些隐含的问题，例如：ReactorServerHttpResponse 构造函数初始化实例的时候，存放响应 Header 的是 HttpHeaders 实例，也就是响应 Header 是可以直接修改的。

```java
public ReactorServerHttpResponse(HttpServerResponse response, DataBufferFactory bufferFactory) {
    super(bufferFactory, new HttpHeaders(new NettyHeadersAdapter(response.responseHeaders())));
    Assert.notNull(response, "HttpServerResponse must not be null");
    this.response = response;
}
```

### ServerHttpResponse 接口

```java
public interface ServerHttpResponse extends ReactiveHttpOutputMessage {
    
    // 设置响应状态码
    boolean setStatusCode(@Nullable HttpStatus status);
    
    // 获取响应状态码
    @Nullable
    HttpStatus getStatusCode();
    
    // 获取响应 Cookie，封装为 MultiValueMap 实例，可以修改
    MultiValueMap<String, ResponseCookie> getCookies();  
    
    // 添加响应 Cookie
    void addCookie(ResponseCookie cookie);  
}
```

### ServerHttpResponse 接口的父接口：ReactiveHttpOutputMessage 接口

```java
public interface ReactiveHttpOutputMessage extends HttpMessage {
    
    // 获取 DataBufferFactory 实例，用于包装或者生成数据缓冲区 DataBuffer 实例（创建响应体）
    DataBufferFactory bufferFactory();

    // 注册一个动作，在 HttpOutputMessage 提交之前此动作会进行回调
    void beforeCommit(Supplier<? extends Mono<Void>> action);

    // 判断 HttpOutputMessage 是否已经提交
    boolean isCommitted();
    
    // 写入消息体到 HTTP 协议层
    Mono<Void> writeWith(Publisher<? extends DataBuffer> body);

    // 写入消息体到 HTTP 协议层并且刷新缓冲区
    Mono<Void> writeAndFlushWith(Publisher<? extends Publisher<? extends DataBuffer>> body);
    
    // 指明消息处理已经结束，一般在消息处理结束自动调用此方法，多次调用不会产生副作用
    Mono<Void> setComplete();
}
```

### ServerHttpResponse 接口的"爷爷"接口：HttpMessage 接口

```java
public interface HttpMessage {
    // 获取响应 Header，目前的实现中返回的是 HttpHeaders 实例，可以直接修改
    HttpHeaders getHeaders();
}  


```



<-->
## The End
