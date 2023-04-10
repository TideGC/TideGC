---
alias: SpringBoot 中使用 Redis
---

## Spring Boot 中使用 RedisTemplate

Spring Boot 提供了对 Redis 集成的组件包：**spring-boot-starter-data-redis**，它依赖于 **spring-data-redis** 和 **lettuce** 。

> [!cite] 了解
> spring-data-redis 在 1.x 时代使用的是 Jedis 作为底层驱动包，而到了 2.x 时代换成了 Lettuce 。从功能和性能上讲，Lettuce 都要优于 Jedis 一点。
>
> 不过，但是 Lettuce 有一个偶发的 *Redis command timed out* 问题，会导致连接 Redis 失败。原因和解决方案见笔记 [[Lettuce 连接池连接超时问题|《lettuce 连接池连接超时》]]。

### 第 1 步：引入依赖包

- 在界面上选择

![](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220917234933.png)

- 或者手动在 pom.xml 中添加

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
    <!-- 版本信息在 spring-boot-dependencies 中有 -->
</dependency>

<!--早期的低版本的 lettuce 依赖于 commons-pool2 。但是不知道为什么，它又不在 lettuce 的依赖关系中。所以要手动引入。
  现在似乎是不需要了。
<dependency>
  <groupId>org.apache.commons</groupId>
  <artifactId>commons-pool2</artifactId>
</dependency>
-->
```

### 第 2 步：为 application.properties 添加配置

```properties
spring.redis.host=localhost # 默认值就是 localhost
spring.redis.port=6379      # 默认值就是 6379
spring.redis.database=0     # 默认值就是 0
spring.redis.password=      # 默认值就是空
```

### 第 3 步：解决可能的 Command timed out 问题

为 Redis 在 Spring Boot 项目中添加一个配置类，其内容如下：

```java
@Configuration  
public class RedisConfig implements InitializingBean {  
          
     private final RedisConnectionFactory redisConnectionFactory;  
  
    // 构造注入  
    public RedisConfig(RedisConnectionFactory redisConnectionFactory) {  
        this.redisConnectionFactory = redisConnectionFactory;  
    }
  
    @Override  
    public void afterPropertiesSet() {  
        if (redisConnectionFactory instanceof LettuceConnectionFactory) {  
            LettuceConnectionFactory factory = (LettuceConnectionFactory) redisConnectionFactory;  
            // System.out.println(factory.getValidateConnection()); // false  
            factory.setValidateConnection(true);  
        }  
    }

}
```

关于 Lettuce 的 *Redis command timed out* 问题，详情参见笔记[[Lettuce 连接池连接超时问题|《lettuce 连接池连接超时》]]。

### 第 4 步：简单测试

spring-data-redis 在 Spring IoC 容器中为我们准备了 2 个 RedisTemplate 单例对象：

- _**RedisTemplate\<Object, Object\>**_ 对象；
- _**StringRedisTemplate**_ 对象。

我们可以直接依赖注入并使用它们<small>（中的某一个）</small>。

> [!danger] 注意
> 由于，_StringRedisTemplate_ 是 _RedisTemplate_ 的子类，
> ```
> StringRedisTemplate == RedisTemplate<String, String>
> ```
> 所以，当你 @Autowired RedisTemplate 的时候，Spring 在 IoC 容器中会找到 2 个！

```java
@SpringBootTest
public class TestRedisTemplate {

    @Autowired
    private StringRedisTemplate template;
 
    @Test
    public void testString() {
        template.opsForValue().set("hello", "world");
        Assertions.assertEquals("world", template.opsForValue().get("hello"));
    }
}
```

在这个单元测试中，我们使用 redisTemplate 存储了一个字符串 "world" ，存储之后获取进行验证，多次进行 set 相同的 key，键对应的值会被覆盖。

### 第 5 步：观察、学习 StringRedisTemplate 

StringRedisTemplate 比 RedisTemplate\<Object, Object\> 更简单、常见。RedisTemplate\<Object, Object\> 会涉及一个转换器<small>（Serializer）</small>的概念。所以，优先考虑使用 StringRedisTemplate 。

当你使用 StringRedisTeamplte 时：

- 如果你操作的是 Redis String 结构，key 的类型 "应该是" String ，value 的类型 "应该是" String ；
- 如果你操作的是 Redis List 结构，key 的类型 "应该是" String ，值的类型是一个 list ，list 中的每一个元素 "应该是" String ；
- 如果你操作的是 Redis Set/ZSet 结构，key 的类型 "应该是" String ，值的类型是一个 set/zset ，set/zset 中的每一个元素 "应该是" String ；
- 如果你操作的是 Redis Hash 结构，key 的类型 "应该是" String ，field 的类型 "应该是" String ，field-value 的类型 "应该是" String 。

反正啥啥都 "应该是" String ，不是 String 后果自负。

### 第 6 步：观察、学习各种 Operations 

spring-data-redis 针对 api 进行了重新归类与封装，将同一类型的操作封装为 **Operation** 接口：

| 专有操作 | 说明 |
| :- | :- |
| ValueOperations | string 类型的数据操作 |
| HashOperations | map 类型的数据操作 |
| ListOperations | list 类型的数据操作 |
| SetOperations | set 类型数据操作 |
| ZSetOperations | zset 类型数据操作 |

### 第 7 步：在配置类中添加 @Bean 方便以后偷懒

> 了解。可以不使用、不偷懒。

考虑到未来在代码中可能用不上这么多的 Operations ，你可以少定义几个 @Bean 。

```java
@Bean  
public ValueOperations<String, String> stringValueOperations(StringRedisTemplate stringRedisTemplate) {  
    return stringRedisTemplate.opsForValue();  
}  
  
@Bean  
public HashOperations<String, String, String> stringHashOperations(StringRedisTemplate stringRedisTemplate) {  
    return stringRedisTemplate.opsForHash();  
}  
  
@Bean  
public ListOperations<String, String> stringListOperations(StringRedisTemplate stringRedisTemplate) {  
    return stringRedisTemplate.opsForList();  
}  
  
@Bean  
public SetOperations<String, String> stringSetOperations(StringRedisTemplate stringRedisTemplate) {  
    return stringRedisTemplate.opsForSet();  
}  
  
@Bean  
public ZSetOperations<String, String> stringZSetOperations(StringRedisTemplate stringRedisTemplate) {  
    return stringRedisTemplate.opsForZSet();  
}
```

### 第 8 步：改进测试代码

根据具体需要，注入所需的 Operations 。在之前的测试代码中，我们是向 Redis 中存入了一个 String 类型的键值对，所以我们需要的是 ValueOperations 。

```java
@SpringBootTest  
class RedisDemoApplicationTests {  
  
    @Autowired  
    private ValueOperations<String, String> stringValueOperations;  
  
    @Test  
    void contextLoads() {  
        stringValueOperations.set("hello", "world");  
        Assertions.assertEquals("world", stringValueOperations.get("hello"));  
    }  
  
}
```

在这个单元测试中，我们使用 redisTemplate 存储了一个字符串 "world" ，存储之后获取进行验证，多次进行 set 相同的 key，键对应的值会被覆盖。

### 第 9 步：学习、验证超时失效（自动删除）功能

Redis 在存入每一个数据的时候都可以设置一个超时间，过了这个时间就会自动删除数据。

新建一个 Student 对象，存入 Redis 的同时设置 100 毫秒后失效，设置一个线程暂停 1000 毫秒之后，判断数据是否存在并打印结果。

```java
@SpringBootTest  
class RedisDemoApplicationTests {  
  
    @Autowired  
    private StringRedisTemplate template;  
  
    @Autowired  
    private ValueOperations<String, String> stringValueOperations;  
  
    @Test  
    public void testExpire() throws InterruptedException {  
        final String key = "expire" ;  
  
        stringValueOperations.set(key, "你应该看不见我的…", 100, TimeUnit.MILLISECONDS);  
  
        Thread.sleep(1000);  
  
        boolean exists = template.hasKey(key);  
        System.out.println( exists ? "exists is true" : "exists is false" );  
    }  
}
```

输出结果:

```
exists is false
```

从结果可以看出，Reids 中的 "expire - 你应该看不见我的…" 键值对已经不在了。因为键值对已经超时过期，同时我们在这个测试的方法中使用了 `hasKey("expire")` 方法，可以判断 key 是否存在。

### 第 9 步：学习、验证直接删除功能

有些时候，我们需要对过期的缓存进行删除，下面来测试此场景的使用。首先 set 一个字符串 `hello world`，紧接着删除此 key 的值，再进行判断。

```java
redisTemplate.delete("key");
```


### 其它常见操作

> 了解

- [[202209181949|操作 Hash 结构]]
- [[202209181945|操作 List 结构]]
- [[202209181941|操作 Set 结构]]
- [[202209181936|操作 ZSet 结构]]
