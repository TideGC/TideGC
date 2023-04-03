---
alias: Swagger2 和 Swagger3 的不同
---

## Swagger2 和 Swagger3 的不同

SpringBoot 整合 Swagger3 和 Swagger2 的主要区别如下：

### 区别一：引入不同的依赖

- 如果使用的是 Swagger 3

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-boot-starter</artifactId>
    <version>3.0.0</version>
</dependency>
```

- 如果使用的是 Swagger 2

```xml
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>

<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.9.2</version>
</dependency>
```

### 区别二：使用不同的注解

- 如果使用的是 Swagger 3

```java
@Configuration
@EnableOpenApi // 可省略
public class SwaggerConfig {
    ...
}
```

- 如果使用的是 Swagger 2

```java
@Configuration
@EnableSwagger2 
public class SwaggerConfig {
    ...
}
```

### 区别三：使用不同的枚举变量
    
- 如果使用的是 Swagger 3

```java
new Docket(DocumentationType.OAS_30)
```

- 如果使用的是 Swagger 2

```java
new Docket(DocumentationType.SWAGGER_2)
```

### 区别四：访问不同的 swagger ui 页面路径
 
- 如果使用的是 Swagger 3：[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
    
- 如果使用的是 Swagger 2：[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

### 区别五：暴露的 json 接口不同

- 如果使用的是 Swagger 3：[http://localhost:8080//v3/api-docs](http://localhost:8080//v3/api-docs)
    
- 如果使用的是 Swagger 2：[http://localhost:8080//v2/api-docs](http://localhost:8080//v2/api-docs)



