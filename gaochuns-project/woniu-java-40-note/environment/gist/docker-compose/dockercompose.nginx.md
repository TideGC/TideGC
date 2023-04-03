---
alias: ["Nginx Run By docker-compose"]
tags: [docker-compose, nginx]
---

## Nginx Run By docker-compose

[[docker.nginx|前置知识点：Docker Run Nginx]]

### docker-compose 示例

这里利用了一个[[docker.nginx#小技巧：如何 弄到 默认配置文件|小技巧]]获得 Nginx 容器的默认配置，然后我们可以基于默认配置再进行修改，而不用重头开始编写配置文件。

```yml
version: '3'

volumes:
  nginx-80-html:
  nginx-80-log:
  
services:
  nginx-80:
    image: nginx:stable
    container_name: nginx-80
    mem_limit: 512m
    volumes:
      - /etc/localtime:/etc/localtime:ro 
      - nginx-80-html:/usr/share/nginx/html 
      - nginx-80-log:/var/log/nginx
      - ./nginx.config.d:/etc/nginx/conf.d
    ports:
      - 80:80
```

docker-compose 中的挂载可以使用 "." 来表示当前目录。这比 docker run 要方便一点点。
