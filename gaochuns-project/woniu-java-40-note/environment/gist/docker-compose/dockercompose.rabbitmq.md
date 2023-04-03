---
alias: RabbitMQ Run By docker-compose
---

## RabbitMQ Run By docker-compose

[[docker.rabbitmq|前置知识点：Docker Run RabbitMQ]]

### docker-compose 示例

这里利用了一个[[docker.rabbitmq#Docker Run RabbitMQ#小技巧：如何 弄到 默认配置文件|小技巧]]获得 RabbitMQ 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```yaml
version: '3'
volumes:
  rabbitmq-5672-data:
services:
  rabbitmq-5672:
    image: rabbitmq:3.10-management
    container_name: rabbitmq-5672
    hostname: rabbitmq-5672
    mem_limit: 512m
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - rabbitmq-5672-data:/var/lib/rabbitmq
      - ./rabbitmq.config.d:/etc/rabbitmq
    ports:
      - 5672:5672
      - 15672:15672
```

