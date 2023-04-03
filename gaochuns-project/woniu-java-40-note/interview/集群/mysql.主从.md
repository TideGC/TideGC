---
alias: MySQL 主从库环境搭建
tags: mysql, 主从, 集群
---

## MySQL 主从库环境搭建


> [!cite] 提示
> 为了简便起见，下面的案例是基于 docker/docker-compose 实现的，以减少大家找/创建多台服务器的麻烦。

### 第 1 步：准备主库配置文件

在某路径下创建主库的配置文件 master-mysql.cnf ，其内容如下：

```ini
[mysqld]
default_time_zone='+8:00'
character_set_server=utf8mb4
collation_server=utf8mb4_bin
default_authentication_plugin=mysql_native_password

# 开启 binlog
log_bin=mysql-bin
sync-binlog=1
# 配置 serverid
server-id=1
# 配置不需要写入 binlog 库
binlog-ignore-db=information_schema
binlog-ignore-db=performance_schema
binlog-ignore-db=mysql
binlog-ignore-db=sys

[mysql]
default-character-set=utf8mb4
```

其中的 6 到 15 行的内容是我们在默认配置内容基础之上新增的、与主从配置有关的配置信息。

- *log_bin=mysql-bin* ：通过它指定了要求主库将执行的操作写入名为 *mysql-bin* 的 bin-log 文件中。<small>未来，从库就是要从主库这里拉取、下载这个文件。</small>

- *server-id=1*：为当前 mysql 手动指定一个 id 。在 mysql 的主从环境中，每一台 mysql 必须要有一个唯一的 id 作为自己的身份标识。

- *sync-binlog=1*：默认值为 0 。用来控制 mysql 是以同步还是异步的方式写 bin-log 文件。0 表示异步，异步的性能更好，但不是实时更新 bin-log；同步则相反。

- *binlog-ignore-db=...*：配置对于哪些数据库的操作不要记录在 bin-log 日志中。<small>这样从库就不会同步到这些数据库的中的数据。</small>


### 第 2 步：准备主库

我们在 docker-compose.yml 中写入如下内容片段，将来通过 docker-compose 来启动主库。<small>（ docker-compose.yml 和上面的 master-mysql.cnf 在同一级目录下 ）</small>：

```yaml
version: '3'
## docker container inspect --format='{{json .Mounts}}' mysql-master | jq

services:
  mysql-master:
    container_name: mysql-master
    image: mysql:8.0.26
    environment:
      - MYSQL_ROOT_PASSWORD=123456
    volumes:
      - ./master-mysql.cnf:/etc/mysql/conf.d/mysql.cnf
    ports:
      - "13306:3306"
```

在这里，我们挂载了前面自定义的主库配置文件<small>（ master-mysql.cnf ）</small>，对主库进行配置。

### 第 3 步：为从库的连接授权

通过 *docker-compose up -d mysql-master* 启动主库容器。

可以通过 *docker-compose logs -f mysql-master* 观察 mysal-master 的启动日志。

待 mysql-master 成功启动后，在宿主机上执行 *mysql -P 13306 -uroot -p123456* 连接 mysql-master 。<small>你愿意先执行 <em>docker exec -it mysql-master /bin/bash</em> 进入 mysql-master 容器内，然后再执行 <em>mysql -uroot -p123456</em> 连接 mysql-master 也行。</small>

在 mysql client 中执行如下命令，赋予<small>（ 未来的 ）</small>mysql 从库使用 root 账号的权力。<small>因为，从库未来连接要"连上"主服务器之后，才能从主服务器下载 bin-log 日志文件。既然是连接，就必然涉及到使用账号/密码的问题。</small>

执行 SQL 语句：

```sql
GRANT REPLICATION slave ON *.* TO 'root'@'%';
FLUSH PRIVILEGES;
```

### 第 4 步：查看主库状态，记录信息

执行如下命令查看当前数据库<small>（ 作为主库 ）</small>的状态：

执行 SQL 语句：

```sql
show master status;
```

你会看到类似如下内容：

```text
+------------------+----------+--------------+-------------------------------------------------+-------------------+
| File             | Position | Binlog_Do_DB | Binlog_Ignore_DB                                | Executed_Gtid_Set |
+------------------+----------+--------------+-------------------------------------------------+-------------------+
| mysql-bin.000003 |      535 |              | information_schema,performance_schema,mysql,sys |                   |
+------------------+----------+--------------+-------------------------------------------------+-------------------+
1 row in set (0.00 sec)
```

**在这里，你所看到的你的执行结果的 _File_ 和 _Position_ 两个字段的值要记下来，回头在从服务器那里里要用上的。**

### 第 5 步：准备从库配置文件

在 master-mysql.cnf 、docker-compose.yml 的同级目录下创建从库的配置文件 slaver-mysql.cnf ，其内容如下：

```ini
[mysqld]
default_time_zone='+8:00'
character_set_server=utf8mb4
collation_server=utf8mb4_bin
default_authentication_plugin=mysql_native_password

server-id=2
read_only=1
log_bin=mysql-bin
relay_log=mysql-relay-bin

[mysql]
default-character-set=utf8mb4
```

其中 6 到 9 行是我们在默认的配置文件的内容的基础上新增加的配置。

- *server-id*：之前提到过，是每一台 mysql 的唯一标识。

- *read_only=1*：设置当前 mysql 为只读状态。<small>主从同步的主要使用场景和目的主要就为了读写分离。</small>

- *log_bin=mysql-bin*：连接主库，从主库下载它那里的 mysql-bin 文件。这里要和主库中的 *log_bin* 配置遥相呼应。

- *relay_log=mysql-relay-bin*：从主库那里下载的 log_bin 文件<small>（ 的内容 ）</small>放在本地的 mysql-relay-bin 文件中。


### 第 6 步：准备从库

我们在 docker-compose 中再补充如下内容片段，将来通过 docker-compose 来启动从库

```yaml
version: '3'

## docker container inspect --format='{{json .Mounts}}' mysql-slaver | jq

services:
  mysql-slaver:
    container_name: mysql-slaver
    image: mysql:8.0.26
    environment:
      - MYSQL_ROOT_PASSWORD=123456
    volumes:
      - ./slaver-mysql.cnf:/etc/mysql/conf.d/mysql.cnf
    ports:
      - "23306:3306"
    depends_on:
      - mysql-master
```

在这里，我们挂载了前面所准备的从库配置文件<small>（ slaver-mysql.cnf ）</small>，对从库进行配置。

### 第 7 步：为从库配置主库地址

通过 docker-compose 启动从机容器后，直接从宿主机连接、或进入从库容器后连接从库。

执行如下语法的 SQL 语句：

```sql
stop slave;

change master to 
	master_host='<主库IP>', master_port=<主库端口>,
	master_user='<主库提供的账号>', master_password='<账号的密码>',
	master_log_file='<主库中File域的值>', master_log_pos=<主库中Position域的值>;
```

例如 SQL 语句：

```sql
stop slave;

change master to 
	master_host='mysql-master', master_port=3306,
	master_user='root',master_password='123456',
	master_log_file='mysql-bin.000003',master_log_pos=535;
```

因为我们使用的是 docker/docker-compose，所以这里，我们在 master_host 这里填的是主服务器的服务名，这里等加于主服务器的 IP 地址。

执行完成后，再次激活从机功能。执行 SQL 语句：

```sql
start slave;
```

连上从库，执行如下 SQL 语句查看从库状态：

```sql
show slave status \G
```

你会看到类如如下内容<small>（ 留意 12、13 行 ）</small>：

```
*************************** 1. row ***************************
               Slave_IO_State: Connecting to source
                  Master_Host: mysql-master
                  Master_User: root
                  Master_Port: 3306
                Connect_Retry: 60
              Master_Log_File: mysql-bin.000003
          Read_Master_Log_Pos: 930
               Relay_Log_File: 192-relay-bin.000001
                Relay_Log_Pos: 4
        Relay_Master_Log_File: mysql-bin.000003
             Slave_IO_Running: Connecting
            Slave_SQL_Running: Yes
            ...
```

### 第 8 步：测试

主服务中，创建一个测试库、表和增加记录；

```sql
create database test;
use test;
create table user( uid int primary key, uname varchar(20))engine=innodb charset=utf8;
insert into user values(1,'张三');
```

从服务器中直接去查看数据有没有过来。

发现已经同步过来啦，说明我们的主从同步已经实现了。

