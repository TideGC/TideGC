
# RedisTemplate 和 Serializer

_RedisTemplate\<Object, Object\>_ 看起来比 _StringRedisTemplate_ 更『牛逼』一些，因为它不强求键和值的类型必须是 String 。

但是很显然，这和 Redis 的实际情况是相违背的：**在最小的存储单元层面，Redis 本质上只能存字符串，不可能存其它的类型**。

> [!info] 这么看来，StringRedisTemplate 更贴合 Redis 的存储本质。

那么 RedisTemplate 是如何实现以任何类型<small>（只要是实现了 <em>Serializable</em> 接口）</small>作为键值对的？通过 Serializer 。

RedisTemplate 会将你交给它的作为键或值的任意类型对象<small>（唯一要求是实现了 <em>Serializable</em> 接口）</small>使用 Serializer 进行转换，转换成字符串，然后再存入 Redis 中。这样就没有违背 "Redis 的最小存储单元中只能存字符串" 的准则。

RedisTemplate 默认使用的是 ***JdkSerializationRedisSerializer*** 进行 Object 到 String 的双向转换工作。<small><em>JdkSerializationRedisSerializer</em> 的转换规则是将对象转换为字节数组的字符串形式。</small>

> [!info] 对象的字节数组的字符串形式如下
> 如果你通过命令行去 get 由 JdkSerializationRedisSerializer 转换后再存入 Redis 中的对象，你会看到一个奇怪的字符串，它就是这个对象的字节数组<small>（的字符串形式）</small>。
> ```sh
> > get "\xac\xed\x00\x05t\x00\x0cdepartment:1"
> "\xac\xed\x00\x05sr\x00\"com.woniu.example1.bean.Department\x00…"
> ```

考虑到『对象的字节数组的字符串形式』不便于阅读，因此，你可以考虑将默认的 _JdkSerializationRedisSerializer_ 替换掉。这种情况下，你就需要自己去声明 RedisTemplate<small>（@Bean）</small>。

spring-data-redis 已经与定义好了一些 Serializer 供大家选择、使用：

> [!dice-1] _RedisSerializer.string()_  方法所返回的 Serializer
> 可用于 String 类型的键和值。它会把 String 原样存入 Redis 。
> 
> 注意，如果你强行传入一个非 String 类型的对象，RedisTemplate 会对它进行强转<small>（Class Cast）</small>成 String ，强转失败自然就会抛出异常。

> [!dice-2] _RedisSerializer.java()_ 方法所返回的 Serializer 
> 可用于 Object 类型的键和值。它会把 Object 转换成字节数组<small>（byte[]</small>形式的字符串。

> [!dice-3] RedisSerializer.json() 所返回的 Serializer
> 可用于 Object 类型的键和值。它会把 Object 转换成 JSON 格式字符串。
> 不过，要使用它，你需要先引入 jackson 包<small>（com.fasterxml.jackson.core:jackson-databind）</small>


```java
@Configuration
public class RedisConfig {

    @Bean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(RedisSerializer.string());
        template.setValueSerializer(RedisSerializer.json());
        return template;
    }
}
```
