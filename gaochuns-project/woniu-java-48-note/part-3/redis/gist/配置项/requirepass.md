### requirepass 配置项

#redis #配置 #requirepass

语法：

```ini
requirepass <密码>
```

**默认连接 Redis 是不需要密码的**，因为在配置文件中并没有指定认证密码。

如果有需要，则需要在 Redis 配置文件中使用 **requirepass** 属性。例如：

```conf
requirepass 123456
```

这样，客户端在连接时，就需要指定 `123456` 作为连接密码才能连接成功。

配置了 requirepass 属性之后，客户端在连接 Redis Server 时有 2 种方式：

#### 方式一：在连接时就提供连接密码

```sh
redis-cli -h <ip> -p <port> -a <password>
```

#### 方式二：连接之后再使用 **auth** 命令进行认证：

```sh
redis-cli -h <ip> -p <port>
auth <password>
```

> [!info] 注意
> 在 "方式二" 中，auth 命令必须是连接后的第一条命令。



