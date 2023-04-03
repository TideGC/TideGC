### masterauth 配置项

语法：

```
masterauth <密码>
```

由于 Redis 是可以通过配置来设置连接密码的，所以，一旦主机设置了连接密码，那么从机在连接主机时，需要提供正确的主机密码。这就是通过 **masterauth** 配置项进行设置。例如：

```conf
masterauth "123456"
```

