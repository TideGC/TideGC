## Spring Boot 中使用 Redis Repositories

Spring Data Redis 从 1.7 开始提供 Redis Repositories ，可以无缝的转换并存储 domain objects，使用的数据类型为哈希<small>（ hash ）</small>。

Spring Data Redis 的 Repository 的基本实现为：***CrudRepository*** 。

基础用法<small>（ Usage ）</small>分为以下步：

### 第 1 步：启用 Repository 功能

编写一个配置类<small>（ 或直接利用 Spring Boot 的入口类 ）</small>，在其上标注 _@EnableRedisRepositories(basePackages = "...")_ ，表示启用 Repository 功能。

属性 _basePackages_ 如果不赋值，那么默认是扫描入口类平级及之下的所有类，看它们谁的头上有 _@Repository_ 注解。如果是同时使用 spring-data-jpa 和 spring-data-redis 时，由于它们的 Repository 的祖先中都有 CrudRepository 因此会造成冲突。虽有，最好还是加上 _basePackages_ 属性并为它赋值，指定各自扫描的路径，以避免冲突。

### 第 2 步：注解需要缓存的实体

添加关键的两个注解 _@RedisHash_ 和 _@Id_ ;

```java
@RedisHash("user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String userName;
    private String password;
    private String email;
}
```

| 注解 | 说明 |
| :- | :- |
| @RedisHash | 表示将 User 类的对象都对于 Redis 中的名为 user 的 Set 中。|
| @Id | 标注于对象的唯一性标识上。|

如果将多个 User 对象通过 Repository 存储于 Redis 中，那么，它们每个的 key 分别是：_user:\<Id>_ 。例如：_user:1_ 、_user:2_ 、_user:3_ 、...

获取它们每个对象的属性的命令为：

```bash
hget user:1 userName
```

### 第 3 步：创建一个 Repository 接口

自定的 Repository 接口必须继承 CrudRepository ，才能「天生」具有存取数据的能力。

```java
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
}
```

