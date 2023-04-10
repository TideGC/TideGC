---
alias: Nacos Run By docker-compose
---

## Nacos Run By docker-compose

[[docker.nacos|前置知识点：Docker Run Nacos]]

### docker-compose 示例

```yaml
version: "3"

volumes:
  nacos-8848-data:
    
services:
  nacos-standalone:
    image: nacos/nacos-server:1.4.2
    container_name: nacos-8848
    mem_limit: 1024m 
    environment:
      - MODE=standalone
      - JVM_XMX=512m # jvm 最大内存
      - JVM_XMS=512m # jvm 启动（初始化）时占用内存
    volumes:
      - /etc/localtime:/etc/localtime:ro 
      - nacos-8848-data:/home/nacos/data 
    ports:
      - "8848:8848"
```
