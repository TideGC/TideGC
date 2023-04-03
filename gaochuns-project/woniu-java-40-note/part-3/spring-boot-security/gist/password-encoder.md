---
alias: Password Encoder
---

## Password Encoder

### 关于 spring-security 5.x 的新要求

> [!danger] 警告
> 从 5.x 开始，强制性要求必须使用密码加密器<small>（ PasswordEncoder ）</small>对原始密码<small>（注册密码）</small>进行加密。
> 
> 因此，如果忘记指定 PasswordEncoder 会导致执行时会出现 `There is no PasswordEncoder mapped for the id "null"` 异常。

在 IoC 容器中没有 PasswordEncoder 单例对象的情况下，你启动项目，会发现无法登录系统：

![spring-boot-security-helloworld-07](https://woniumd.oss-cn-hangzhou.aliyuncs.com/java/hemiao/20220627135552.png)


PasswordEncoder 在 2 处场景会被使用到，并且是遥相呼应地配合使用：

### 注册功能

当我们实现注册功能时，要将用户在前端页面输入的明文密码使用 PasswordEncoder 进行加密后存储、持久化。

使用 PasswordEncoder 的 _encode_ 方法。这需要我们手动调用。

### 登录功能

当用户在登录时，Spring Security 会将用户在前端页面输入的明文密码使用 PasswordEncoder 进行加密之后，再和由我们提供的密码「标准答案」进行比对。

使用 PasswordEncoder 的 _matches_ 方法。这个通常不需要我们手动调用。

### PasswordEncoder 的方法

Spring Security 使用 **PasswordEncoder** 对你提供的密码进行加密。该接口中有两个方法：加密方法，是否匹配方法。

- 加密方法<small>（ encode 方法 ）</small>在用户注册时使用。在注册功能处，我们<small>（程序员）</small>需要将用户提供的密码加密后存储<small>（至数据库）</small>。

- 匹配方法<small>（ matches** 方法 ）</small>是由 Spring Security 调用的。在登录功能处，Spring Security 要用它来比较登录密码和密码「标准答案」。

因此上面的配置改为如下形式更为合理：

```java
@Slf4j
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        String password = passwordEncoder().encode("123");
        log.info(password);

        auth.inMemoryAuthentication().withUser("tom")
                .password(password)
                .roles("admin");
    }
}
```


Spring Security 内置的 Password Encoder 有：

| 加密算法名称 | PasswordEncoder |
| :- | :- |
| NOOP      | NoOpPasswordEncoder.getInstance() |
| SHA256    | new StandardPasswordEncoder() |
| **BCRYPT**<small>（官方推荐）</small>| new BCryptPasswordEncoder() |
| LDAP      | new LdapShaPasswordEncoder() |
| PBKDF2    | new Pbkdf2PasswordEncoder() |
| SCRYPT    | new SCryptPasswordEncoder() |
| MD4       | new Md4PasswordEncoder() |
| MD5       | new MessageDigestPasswordEncoder("MD5") |
| SHA_1     | new MessageDigestPasswordEncoder("SHA-1") |
| SHA_256   | new MessageDigestPasswordEncoder("SHA-256") |


上述 Password Encoder 中有一个「无意义」的加密器：**NoOpPasswordEncoder** 。它对原始密码没有做任何处理<small>（ 现在也被标记为废弃 ）</small>。在开发中，你如果想看到密码的未加密的样子，以便于开发、调试，那么你可以使用它，用它去满足 Spring Security 的强制性要求。

> [!cite] 补充
> 记得使用 @SuppressWarnings("deprecation") 去掉 IDE 的警告信息。
