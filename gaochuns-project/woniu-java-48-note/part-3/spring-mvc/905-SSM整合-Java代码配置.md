# SSM 整合（代码配置）

## 1. 基本形式

Spring 的 Java 代码配置与 XML 配置文件配置有对应关系，本质上太大区别。

核心关键点有两处：

- **.xml** 配置文件演变为一个配置类，其头上标注 **@Configuation** 注解；

- **\<bean>** 配置演变为配置类中的一个方法，其头上标注 **@Bean** 注解。

## 2. 无 web.xml 的理论基础

为了支持脱离 web.xml，Servlet 定义了一个 **javax.servlet.ServletContainerInitializer** 的接口，并且要求在 Servlet 容器启动 web 项目时，在项目的的 jar 包中的 **META-INF/services** 去找一个名字是它的完全限定名的文件。

在 Spring MVC 项目中，spring-web 包下的 **META-INF/services** 目录下就存在这样一个 **javax.servlet.ServletContainerInitializer** 的文件。

按照约定，这个文件的内容可以放 *ServletContainerInitializer* 接口的实现类的完全限定名，在 Spring MVC 中这个实现了是 **org.springframework.web.SpringServletContainerInitializer** 。

Servlet 容器在启动时会创建这个文件中所记载的 *ServletContainerInitializer* 接口的实现类的对象，并调用它的 **onStartup** 。

在 **SpringServletContainerInitializer** 的 **onStartup** 方法中，它会去查找、调用 Spring MVC 项目中所有 **WebApplicationInitializer** 接口直接<small>（或间接）</small>实现类的 **onStartup** 方法。

所以，我们需要直接<small>（或间接）</small>实现 **WebApplicationInitializer**，并在 **onStartup** 方法中实现我们曾将在 *web.xml* 中所实现的内容。


## 3. WebInitializer 替代 web.xml 

当然，我们通常并不会直接实现 **WebApplicationInitializer** 接口，我们可以通过继承 **AbstractAnnotationConfigDispatcherServletInitializer** 来间接实现 *WebApplicationInitializer* 接口：

```java
public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    // 第一次加载配置时机所加载的配置类。
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[] {
            SpringServiceConfig.class,
            SpringDaoConfig.class
        };
    }

    // 第二次加载配置时机所加载的配置类。 
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {
            SpringWebConfig.class
        };
    }

    // 设置 DispatcherServlet 的映射
    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

考虑到我们曾经在 **web.xml** 中配置过一个 Filter 来解决 GET 请求中文乱码问题，如果要在 **WebInitializer** 中实现同样的效果，则需要多重写一个父类的方法：

```java
@Override
protected Filter[] getServletFilters() {
    Filter encodingFilter = new CharacterEncodingFilter("UTF-8", true);
    return new Filter[] { 
        encodingFilter 
    };
}
```

## 4. SpringWebConfig 替代 spring-web.xml

```java
@Configuration
@EnableWebMvc   // 注解驱动
@ComponentScan("xxx.yyy.zzz.web") // 包扫描
public class SpringWebConfig implements WebMvcConfigurer {

    @Bean   // 视图解析器
    public InternalResourceViewResolver viewResolver() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        return viewResolver;
    }
}
```

除了上面三项关键性配置之外，还有一些附加功能配置可供选择。

## 5. 配置『静态资源不拦截』

> 当然 DispatcherServlet 配置的是拦截的是 `*.后缀` 形式，那么就不存在静态资源拦截问题。所以，需要考虑静态资源拦截问题时，通常意味着 DispacherServlet 配置的是 `/` 。另外，通常不会、不建议使用 `/**` 。

实现静态资源不拦截的方式有两种<small>（二选一）</small>，一是启用 **DefaultServletHandler**，二是配置 **ResouceHandler** 。

### 方案一

配置启用 **DefaultServletHandler** 意味着：像 **.js**、**.css** 这样的 URL 没有 Controller 处理则由 **DefaultServletHandler** 处理，而它的处理方式就是在对应目录下找到这些文件并发送给客户端<small>（浏览器）</small>。

```java
@Override
public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
    configurer.enable();
}
```

### 方案二

配置 **ResouceHandler** 就是明确告诉 Spring MVC 当 URL 路径中出现何种关键词<small>（或后缀）</small>时，去哪个路径下找静态资源文件。

```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
        .addResourceHandler("/css/**")// 过滤静态资源路径
        .addResourceLocations("classpath:/css");// 定位资源
    registry
        .addResourceHandler("/js/**")
        .addResourceLocations("classpath:/js");
    registry
        .addResourceHandler("/img/**")
        .addResourceLocations("classpath:/img");
    }
}
```

## 6. 配置 URL 后缀生效/失效

<small>常规的 Java Web 一般不会考虑这个问题。在 Restful 的 Java Web 中才会针对『一个奇怪的现象』考虑这个配置。</small>

默认情况下 Spring MVC 会忽略掉 URL 请求中的后缀。也即是说 
，在 URL 中使用 **hello.do** 能触发 **@RequestMapping("/hello")** 的执行。

有时可能需要 Spring MVC 更『严谨』一些。如果是在 **.xml** 配置文件中是配置成这样：

```xml
<mvc:annotation-driven>
    <mvc:path-matching suffix-pattern="false" />
</mvc:annotation-driven>
```

在代码配置中，对等的配置是如下：

```java
@Override
public void configurePathMatch(PathMatchConfigurer configurer) {
    // 显而易见，这个值默认是 true 。
    configurer.setUseSuffixPatternMatch(false);
}

```

## 7. 整合 Service 层

这里通过配置类 **SpringServiceConfig** 来等价替代 *spring-service.xml* 配置文件。

```java
@Configuration
@ComponentScan("xxx.yyy.zzz.service")   // 包扫描
@EnableTransactionManagement            // 激活/启用事务注解（@Transactional）
public class SpringServiceConfig {

    // txManager
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        DataSourceTransactionManager manager = new DataSourceTransactionManager();
        manager.setDataSource(dataSource);
        return manager;
    }

}
```

## 8. 整合 Dao 层 

这里通过配置类 **SpringDaoConfig** 来等价替代 **spring-dao.xml** 配置文件。

大体上，以下配置类就是 **spring-dao.xml** 配置文件的对等转换结果。不过有一处需要注意的地方。

```java
@Configuration
@PropertySource("classpath:jdbc.properties")
public class SpringDaoConfig {

    @Value("${datasource.driver-class-name}")
    private String driverClassName;

    @Value("${datasource.url}")
    private String url;

    @Value("${datasource.username}")
    private String userName;

    @Value("${datasource.password}")
    private String password;

    @Bean   // 数据库连接池
    public HikariDataSource dataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setJdbcUrl(url);
        ds.setUsername(userName);
        ds.setPassword(password);
        return ds;
    }

    @Bean   // sqlSessionFactory
    public SqlSessionFactoryBean sqlSessionFactory(DataSource dataSource) throws IOException {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        sqlSessionFactory.setConfigLocation(new ClassPathResource("mybatis/mybatis-config.xml"));
        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("mybatis/mapper/*.xml"));

        return sqlSessionFactory;
    }

    @Bean   // 包扫描
    public MapperScannerConfigurer mapperScannerConfigurer() {
        MapperScannerConfigurer configurer = new MapperScannerConfigurer();
        configurer.setBasePackage("com.softeem.dao");
        return configurer;
    }
}
```

如果是 `spring-dao.xml` 无脑改 `SpringDaoConfig`，那么

```java
sqlSessionFactory.setConfigLocation("…");
sqlSessionFactory.setMapperLocations("…");
```

为 sqlSessionFactory 的 configLocation 和 mapperLocations 属性赋值时『想当然地』是提供两个字符换（因为 `.xml` 配置文件中就是如此）。

但是，在实际中，sqlSessionFactory 的这两个属性需要的是 Resource 对象和 Resource 对象的数组。因此，需要以这两个字符串为依据，生成与之对应的对象和对象的数组，后再使用：

```java
new ClassPathResource("mybatis/mybatis-config.xml"));

new PathMatchingResourcePatternResolver().getResources("mybatis/mapper/*.xml")
```

## 9. Mybatis 相关配置文件

Mybatis 的核心配置文件和映射文件还是需要以 `.xml` 形式提供。

略。

## 10. 其它

```java
@Configuration
@EnableWebMvc
@ComponentScan("com.xja.hemiao.web.controller") // 包扫描
public class SpringWebConfig implements WebMvcConfigurer {



  @Bean(name = "multipartResolver") // bean 必须写 name 属性且必须为 multipartResolver
  protected CommonsMultipartResolver multipartResolver() {
    CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
    commonsMultipartResolver.setMaxUploadSize(5 * 1024 * 1024);
    commonsMultipartResolver.setMaxInMemorySize(0);
    commonsMultipartResolver.setDefaultEncoding("UTF-8");
    return commonsMultipartResolver;
  }


  @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        super.configureMessageConverters(converters);

        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat
        );
        fastConverter.setFastJsonConfig(fastJsonConfig);

        converters.add(fastConverter);
    }
}
```

