# spring-boot-configuration-processor 的作用

## 配置信息的元数据

当我们在 IDEA 中编写 spring boot 项目的配置文件时，总会有很多配置提示信息：

![spring-boot-configuration-processor-01|500](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627172346.png)

之所以 IDEA 能够这么贴心和只能，是因为我们的项目中<small>（spring-boot-autoconfigure.jar）</small> 存在有一个 spring-configure-metadata.json 文件，这个定义了配置项的相关信息。

![spring-boot-configuration-processor-02|500](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627172351.png)

这些数据就被称为 spring 配置元数据。

这些数据并不是在项目运行中有什么作用，而是在开发期间能够通过 ide 的处理给我们更多的便捷提示。

## spring-boot-configuration-processor

显而易见，我们自定义的配置项的相关信息并不在这里。

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-configuration-processor</artifactId>
    <optional>true</optional>
</dependency>
```

spring-boot-configuration-processor 的作用就是「帮」我们去生成一个专属于我们自己项目的 metadata.json 文件，将我们的自定义配置项信息添加到其中，这样，在 IDEA 中就会有提示信息。

当你编写好属性文件，并添加注解之后, 需要你编译一下项目才能在配置文件编写的时候弹出提示。如果不编译是不会有提示的。

```java
@Component
//@EnableConfigurationProperties(value = {GatewayConfig.class})
@ConfigurationProperties(prefix = GatewayConfig.PREFIX)
public class HelloConfig {
  …
}
```

这个配置类必须是 IoC 容器中的单例对象。使用 @EnableConfigurationProperties 逻辑上相当于是批量声明单例对象。


编译好的文件在 target 包下可以看到

![spring-boot-configuration-processor-03|500](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627172354.png)

