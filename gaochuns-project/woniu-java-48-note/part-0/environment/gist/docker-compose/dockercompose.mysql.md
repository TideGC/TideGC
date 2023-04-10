---
alias: MySQL Run By docker-compose
---

## MySQL Run By docker-compose

[[docker.mysql|前置知识点：Docker Run MySQL]]

### docker-compose 示例

这里利用了一个[[docker.mysql#小技巧：如何 弄到 默认配置文件|小技巧]]获得 MySQL 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```yaml
version: '3'
volumes:
  mysql-3306-data:
services:
  mysql-3306:
    image: mysql:8.0
    container_name: mysql-3306
    mem_limit: 512m # 限定 docker 容器内存大小
    environment:
      - MYSQL_ROOT_PASSWORD=123456
    volumes:
      - /etc/localtime:/etc/localtime:ro
      - mysql-3306-data:/var/lib/mysql
      - ./mysql.config.d:/etc/mysql
    ports: 
      - 3306:3306
```
