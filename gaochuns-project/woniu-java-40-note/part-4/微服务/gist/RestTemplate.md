
---
alias: RestTemplate
tags:  RestTemplate
---

## 关于 RestTemplate

RestTemplate 类似于 Slf4J ，它本身并没有做「更多」的事情，它的主要功能和目的是对背后真正干活的「人」做二次包装，以提供统一的、简洁的使用方式。

默认情况下，RestTemplate "包装"的是 JDK 中的 HttpURLConnection 。如此之外，RestTemplate 还支持"包装" HttpClient 和 OkHTTP 库，<small>前提是你要引入它们</small>。

> [!cite] 说明
> 你的项目只要直接或间接引入了 spring-web 包，你就可以使用 **RestTemplate** 。

由于 RestTemplate 只是一个"壳子"，所以如果有需要的话，你可以去 [[202207270006|切换它的底层实现]] 。

## RestTemplate 发起 HTTP 请求

#RestTemplate #HTTP请求

在以前的 Spring Boot 版本中，Spring IoC 容器中已经有一个创建好了的 RestTemplate 供我们使用，不过到了新版本的 Spring Boot 中，需要我们自己创建 RestTemplate 的单例对象：

```java
@Bean
public RestTemplate restTemplate() {
    RestTemplate restTemplate = new RestTemplate(); // 默认实现
//  RestTemplate restTemplate = new RestTemplate(new SimpleClientHttpRequestFactory()); // 等同默认实现
}
```

| #| 笔记 |
|-:|:-|
| 1| [[202207290746\|RestTemplate API 方法介绍]] |
| 2| [[202207290746\|RestTemplate 返回值]] |
| 3| [[202207290744\|RestTemplate 发 GET 请求，无参]] |
| 4| [[202207290741\|RestTemplate 发 GET 请求，使用参数占位符]] |
| 5| [[202207290737\|RestTemplate 发 POST 请求，无参]] |
| 6| [[202207290735\|RestTemplate 发 POST 请求，带 query-string 参数]] |
| 7| [[202207290734\|RestTemplate 发 POST 请求，带 json-string 参数]] |
| 8| [[202207290731\|添加请求头和获取响应头]] |
