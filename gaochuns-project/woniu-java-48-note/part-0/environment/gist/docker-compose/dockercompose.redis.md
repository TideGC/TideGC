---
alias: "Redis Run By docker-compose"
---

## Redis Run By docker-compose

[[docker.redis|前置知识点：Docker Run Redis]]

### docker-compose 示例

这里利用了一个[[docker.redis#小技巧：如何 弄到 默认配置文件|小技巧]]获得 Redis 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```yaml
version: '3'
volumes:
  redis-6379-data:
services:
  redis:
    image: redis:6.2.1
    container_name: redis-6379
    mem_limit: 512m
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - redis-6379-data:/data
      - ./redis.conf:/etc/redis.conf 
    ports:
      - 6379:6379
    command:
      redis-server /etc/redis.conf # 启动命令
```

