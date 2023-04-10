---
alias: SSM 的 Java 代码整合
---

# SSM 的 Java 代码整合

## SpringDaoConfig

> 替代了 spring-dao.xml

```java
@Configuration
public class SpringDaoConfig {
  …
}
```

在这个配置类中，核心的配置信息有 3 个：3 个 Bean 配置：

> [!cite]- 数据库连接池
> ```java
> // Hikari数据库连接池利用上了 Spring IoC 的 destroyMethod 机制。
> @Bean(destroyMethod = "close")
> public HikariDataSource dataSource() {
>   HikariDataSource ds = new HikariDataSource();
>   ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
>   ds.setJdbcUrl("jdbc:mysql://127.0.0.1:3306/******?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai");
>
>   ds.setUsername("******");
>   ds.setPassword("******");
>   return ds;
> }
> ```

> [!cite]- SqlSessionFactoryBean
> ```java
> /**
>  * 因为有它，Mybatis 核心配置文件中的内容被砍的剩不下点啥了。
>  */
> @Bean
> public SqlSessionFactoryBean sqlSessionFactoryBean(DataSource ds) throws IOException {
>   SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
>   factoryBean.setDataSource(ds);
>   factoryBean.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
>   factoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("mybatis/mapper/*.xml"));
> 
>   return factoryBean;
> }
> ```

> [!cite]- 包扫描
> ```java 
> /**
>  * 我们仍然需要『包扫描』，但是，不是原来『那样的包扫描』，而是现在『这样的包扫描』。
>  * MapperScannerConfigurer 去做的包扫描工作干了两件事情：
>  * 1. 它去动态实现 Mapper/Dao 接口的实现类，并创建对象。
>  * 2. 将 Mapper/Dao 对象交给 Spring IoC 容器管理。
>  */
> @Bean
> public MapperScannerConfigurer mapperScannerConfigurer() {
>   MapperScannerConfigurer configurer = new MapperScannerConfigurer();
>   configurer.setBasePackage("com.example.dao");
>   return configurer;
> }
> ```

如果你在这里用上了 @PropertySource 注解<small>（目的是将 JDBC 的四大配置信息摘出去，放到配置文件中）</small>，那么你要注意下，这里有个『坑』。

具体内容见笔记《[[202207162316|SSM 整合使用 @PropertySource 的一个"坑"]]》

## SpringServiceConfig

> 替代了 spring-service.xml

```java
@Configuration
…
public class SpringServiceConfig {
  …
}
```

在这个配置类中，核心的配置信息有 3 个：2 个注解配置和 1 个 Bean 配置：

> [!cite]- 包扫描 + 启用事务
> ```java
> @Configuration
> @ComponentScan("com.example.service")                 // 看这里
> @EnableTransactionManagement(proxyTargetClass = true) // 看这里
> public class SpringServiceConfig {
>   /*
>   @ComponentScan 是包扫描功能，扫描 @Service 。
>   @EnableTransactionManagement 是开启事务支持， 
>                  并强制指定使用 cglib 动态代理，
>                  对所有 AOP 都有影响。
>   */
>   …
> } 
> ```

> [!cite]- 事务管理器
> 
> ```java
> @Bean
> public DataSourceTransactionManager transactionManager(DataSource ds) {
>   DataSourceTransactionManager manager = new DataSourceTransactionManager();
>   manager.setDataSource(ds);
>   return manager;
> }
> ```

## SpringWebConfig

> 替代了 spring-web.xml 

如前面所说，在 Java 代码配置形式下，Spring 的 .xml 配置文件会被等价的标注了 **@Configuration** 的配置类所取代。

和 Dao 、Service 层的配置类不同，Web 层的配置类有个"额外"的要求：**它必须实现 WebMvcConfigurer 接口**<small>（实际上 Spring 之所以这么要求也是为了帮我们简化配置）</small>。

```java
@Configuration
…
public class SpringWebConfig implements WebMvcConfigurer {
  …
}
```

在这个配置类中，核心的配置信息有 3 个：2 个注解配置和 1 个 Bean 配置。

> [!cite]- 包扫描 + 注解驱动
> ```java
> @Configuration
> @EnableWebMvc                     // 看这里
> @ComponentScan("xxx.yyy.zzz.web") // 看这里
> public class SpringWebConfig implements WebMvcConfigurer {
>   /*
>   @EnableWebMvc 开启 Spring MVC 的注解驱动功能，
>                 这样我们在代码中才能用 @Controller 等注解。
>     @ComponentScan 包扫描
>   */
>   …
> }
> ```

> [!cite]- Bean 配置
> ```java
> @Bean // 配置视图解析器
> public InternalResourceViewResolver viewResolver() {
>   InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
>   viewResolver.setPrefix("/WEB-INF/jsp/");
>   viewResolver.setSuffix(".jsp");
> 
>   return viewResolver;
> }
> ```

Web 层除了最核心的 3 个配置外，你可能还会用到其它的配置：

> [!cite]- 解决『静态资源被拦截』的方案之一
> ```java
> // 配置启用 DefaultServletHandler。
> @Override
> public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
>   configurer.enable();
> }
> ```

> [!cite]- 另一种静态资源不拦截的配置
> ```java
> @Override
> public void addResourceHandlers(ResourceHandlerRegistry registry) {
>   registry.addResourceHandler("/static/**") // 过滤静态资源路径
>             .addResourceLocations("/static/");// 定位资源
> }
> ```

> [!cite]- 配置 URL 路径的匹配规则：是否忽略后缀
> ```java
> // 配置 URL 路径的匹配规则。
> @Override
> public void configurePathMatch(PathMatchConfigurer configurer) {
>         
>   // 默认值是 true，这种情况下 SpringMVC 会忽略掉 URL 请求中的后缀。
>   // 例如，URL hello.do 能触发 @RequestMapping("/hello")
>   // xml 中配置为：
>   // <mvc:annotation-driven>
>   //    <mvc:path-matching suffix-pattern="false" />
>   // </mvc:annotation-driven>
> 
>   configurer.setUseSuffixPatternMatch(false);
> }
> ```

## 在 web.xml 中引入 3 个配置类

````ad-cite
title: web.xml
collapse: close
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app 
    xmlns="http://xmlns.jcp.org/xml/ns/javaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
     http://xmlns.jcp.org/xml/ns/javaee > 
     http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
   version="3.1">
  
   <display-name>Archetype Created Web Application</display-name>
  
   <listener>
     <listener-class>
       org.springframework.web.context.ContextLoaderListener
     </listener-class>
   </listener>
 
   <context-param>
     <param-name>contextClass</param-name>
     <param-value><!-- 相较于 XML 配置，Java 代码配置『多』一个这个 -->
       org.springframework.web.context.support.AnnotationConfigWebApplicationContext
     </param-value>
   </context-param>
   <context-param>
     <param-name>contextConfigLocation</param-name>
     <param-value>
        com.example.config.SpringServiceConfig,
        com.example.config.SpringDaoConfig
      </param-value>
   </context-param>
  
    <servlet>
     <servlet-name>HelloWeb</servlet-name>
     <servlet-class>
        org.springframework.web.servlet.DispatcherServlet
     </servlet-class>
     <init-param><!-- 这个是『多』出来的一个 -->
       <param-name>contextClass</param-name>
       <param-value>
         org.springframework.web.context.support.AnnotationConfigWebApplicationContext
       </param-value>
     </init-param>
     <init-param>
       <param-name>contextConfigLocation</param-name>
       <param-value>com.example.config.SpringWebConfig</param-value>
     </init-param>
     <load-on-startup>1</load-on-startup>
   </servlet>
  
   <servlet-mapping>
     <servlet-name>HelloWeb</servlet-name>
     <url-pattern>*.do</url-pattern> <!--    后缀拦截 -->
   </servlet-mapping>                  <!-- /  默认/兜底拦截 -->
 </web-app>                              <!-- /* 路径拦截，拦截所有请求-->
```
````

## 新特性 WebAppInitializer 代替 web.xml

[[202207171017|SSM 整合利用 Servlet 新特性 WebAppInitializer 的原理]]

我们需要直接<small>（或间接）</small>实现 **WebApplicationInitializer**，并在 **onStartup** 方法中实现我们曾将在 web.xml 中所实现的内容。

> [!cite]- WebAppInitializer 
> ```java
> public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
>  
>   @Override
>   protected Class<?>[] getRootConfigClasses() {
>     return new Class<?>[]{
>        SpringServiceConfig.class,
>        SpringDaoConfig.class
>     };
>   }
>  
>   @Override
>   protected Class<?>[] getServletConfigClasses() {
>     return new Class<?>[] {
>       SpringWebConfig.class
>     };
>   }
>  
>   @Override
>   protected String[] getServletMappings() {
>     return new String[]{"/"};
>   }
>  
>   @Override
>   protected Filter[] getServletFilters() {
>     Filter encodingFilter = new CharacterEncodingFilter("UTF-8", true);
> 
>     return new Filter[]{ encodingFilter };
>   }
> }
> ```
 


