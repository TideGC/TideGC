# Sharding JDBC

## 简介

- https://github.com/apache/shardingsphere
- https://shardingsphere.apache.org

sharding-jdbc 是 Sharding Sphere 家族中的第一个产品。定位为轻量级 Java 框架，在 Java 的 JDBC 层提供的额外服务。 它使用客户端直连数据库，以 jar 包形式提供服务，无需额外部署和依赖，可理解为增强版的 JDBC 驱动，完全兼容 JDBC 和各种 ORM 框架。

-   适用于任何基于 JDBC 的 ORM 框架，如：JPA 、Hibernate、Mybatis、Spring JDBC Template 或直接使用 JDBC 。
    
-   支持任何第三方的数据库连接池，如：DBCP、C3P0、BoneCP、Druid、HikariCP 等。
    
-   支持任意实现 JDBC 规范的数据库。目前支持 MySQL、Oracle、SQLServer、PostgreSQL 以及任何遵循 SQL92 标准的数据库。

> [!info] 提示
> sharding-jdbc 现在已经被收纳进 Apache 基金会，所以，从 4.0 版本开始，它的 groupId 就发生了变化，另外配置项的名称<small>（ 前缀 ）</small>发生了变化。所以，留意你所使用的版本以及你在官网上所查看的文档的版本是否对应。

|            | Sharding-JDBC	| Sharding-Proxy |
| :--------- | :--------------- | :------------- |
| 数据库     | 任意             | MySQL          |
| 连接消耗数 | 高               | 低             |
| 异构语言   | 仅Java	        | 任意           |
| 性能	     | 损耗低           | 损耗略高       |
| 无中心化   | 是               | 否             |
| 静态入口   | 无               | 有             |

## 什么是分库分表

分库分表指的是当单库单表的数据量增大从而出现过高的访问压力后，需要对数据的存储进行拆分，从而降低单库单表的压力。

### 什么是分表

简单来说 "分表" 就是将原来的 "单表" 拆分成 "多表" 。原来单表中的数据，自然就分散在了多个表中。

按照 "切分" 的方向，分表分为 "水平切分" 和 "垂直切分" ：

|    | 特点 |
| :- | :- |
| 水平分表 | 1. 横切后单表数据量会变少。<br>&nbsp;&nbsp;&nbsp;&nbsp;例如从 1000 万变为 500 万；<br> 2. 就单条数据而言，数据仍然是完整的。<br>&nbsp;&nbsp;&nbsp;&nbsp;例如，单表仍是 20 列。|
| 垂直分表 | 1. 垂直分表后单表数据量不变。<br>&nbsp;&nbsp;&nbsp;&nbsp;例如，单表仍是 1000 万条数据。<br> 2. 就单条数据而言，逻辑上，数据变得不完整。<br>&nbsp;&nbsp;&nbsp;&nbsp;例如，20 列的单表被分为 8 + 12 两张表。|


### 什么是分库

简单来说 "分库" 就是将原来的单数据库拆分成多数据库。原来单数据库中的表和数据，自然就分散在了多个数据库中。

按照 "切分" 的方向，分库分为 "水平切分" 和 "垂直切分" ：

|    | 特点 |
| :- | :- |
| 水平分库 | 和水平分表的情况类似，立足于 "数据量" 角度进行考量。分库后，单库的数据量降低。|
| 垂直分库 | 立足于 "专库专用" 的目的进行考量，将一个数据库中的多个表，分散到多个数据库中。|

> [!info] 注意
并不是一定要同时使用分库分表。你也可以只分库不分表，或者只分表不分库。
> 
> - 水平分库的优点是：多个独立的 MySQL 分别提供服务，因此就没有磁盘 IO 的竞争。性能会比分表要好。
> 
> - 水平分表的优点是：无论分成多少张表，大家都还是在同一个数据库中，因此可以使用数据库事务。
## hello world

### 引入 maven 依赖

```xml
<sharding-sphere.version>4.1.1</sharding-sphere.version>

<dependency>
  <groupId>org.apache.shardingsphere</groupId>
  <artifactId>sharding-jdbc-spring-boot-starter</artifactId>
  <version>${sharding-sphere.version}</version>
</dependency>

<dependency>
  <groupId>org.apache.shardingsphere</groupId>
  <artifactId>sharding-jdbc-spring-namespace</artifactId>
  <version>${sharding-sphere.version}</version>
</dependency>
```

> 因为 sharding-jdbc 现在已经被收纳进 Apache 基金会，所以，从 4.0 版本开始，它的 groupId 就发生了变化。

### application.properties 配置

```properties
spring.shardingsphere.datasource.names=ds

spring.shardingsphere.datasource.ds.type=com.zaxxer.hikari.HikariDataSource
spring.shardingsphere.datasource.ds.driver-class-name=com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.ds.jdbc-url=jdbc:mysql://localhost:3306/ds\
  ?serverTimezone=Asia/Shanghai\
  &useUnicode=true\
  &characterEncoding=utf-8\
  &useSSL=false
spring.shardingsphere.datasource.ds.username=root
spring.shardingsphere.datasource.ds.password=123456

spring.shardingsphere.sharding.tables.department.actual-data-nodes=ds.department$->{0..1}
spring.shardingsphere.sharding.tables.department.table-strategy.inline.sharding-column=id
spring.shardingsphere.sharding.tables.department.table-strategy.inline.algorithm-expression=department$->{id % 2}

logging.level.root=INFO
logging.level.xxx.yyy.zzz=DEBUG
logging.pattern.console=${CONSOLE_LOG_PATTERN:\
  %clr(${LOG_LEVEL_PATTERN:%5p}) \
  %clr(|){faint} \
  %clr(%-40.40logger{39}){cyan} \
  %clr(:){faint} %m%n\
  ${LOG_EXCEPTION_CONVERSION_WORD:%wEx}}
```

上述配置文件中配置了：

-   数据库（database）名为 _**ds**_ 。
-   _**ds**_ 数据库中有 2 张 department 表，分别名为 _**department0**_ 和 _**department1**_ 。
-   以 department 表的 _**id**_ 列中的数据为基准，进行水平分片。分片规则为：_**id % 2**_ 。

### 测试代码

```java
@Autowired
private JdbcTemplate template;

@Test
public void department() {
    final String sql = "insert into department values(?, ?, ?)";
    template.update(sql, 1, "ACCOUNTING", "NEW YORK");
    template.update(sql, 2, "RESEARCH", "DALLAS");
    template.update(sql, 3, "SALES", "CHICAGO");
    template.update(sql, 4, "OPERATIONS", "BOSTON");
}
```

## 1. sharding-jdbc 配置读写分离（无分库分表）

参看下面的示例中：

- spring.shardingsphere.datasource 部分：

> <small>因配置文件内容过长，以下配置全部省略了 <em>spring.shardingsphere.datasource </em> 前缀</small>

```properties
.names=master,slave0
     
.master.type=com.zaxxer.hikari.HikariDataSource
.master.driver-class-name=com.mysql.cj.jdbc.Driver
.master.jdbc-url=jdbc:mysql://localhost:3306/scott?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
.master.username=root
.master.password=123456

.slave0.type=com.zaxxer.hikari.HikariDataSource
.slave0.driver-class-name=com.mysql.cj.jdbc.Driver
.slave0.jdbc-url= jdbc:mysql://localhost:3306/scott_test?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&useSSL=false
.slave0.username=root
.slave0.password=123456
```

- spring.shardingsphere.masterslave 部分：

> <small>因配置文件内容过长，以下配置全部省略了 <em>spring.shardingsphere.masterslave</em> 前缀</small>

``` properties
.name=ms
.master-data-source-name=master
.slave-data-source-names=slave0
.load-balance-algorithm-type=round_robin
```

Sharding JDBC 和 Mybatis /JPA 可以无缝整合。唯一需要注意的地方就是，配置文件和配置类中出现的是逻辑表名<small>（ <em>department</em> ）</small>，而不是物理表名<small>（ <em>department0</em> 、<em>department1</em> ）</small>。


## 分表，不分库

- spring.shardingsphere.datasource 部分：

> <small>因配置文件内容过长，以下配置全部省略了 <em>spring.shardingsphere.datasource </em> 前缀</small>

```properties
.names=ds

.ds.type=com.zaxxer.hikari.HikariDataSource
.ds.driver-class-name=com.mysql.jdbc.Driver
.ds.jdbc-url=jdbc:mysql://localhost:3306/demo_ds?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8
.ds.username=root
.ds.password=123456
```


- spring.shardingsphere.sharding.tables 部分：

> <small>因配置文件内容过长，以下配置全部省略了 <em>spring.shardingsphere.sharding.tables</em> 前缀</small>

```properties
.department.actual-data-nodes=ds.department$->{0..1}
.department.table-strategy.inline.sharding-column=id
.department.table-strategy.inline.algorithm-expression=department$->{id % 2}
```

## 分库，不分表

略，未来我们只分表，不分库，因此这里就不展开讲解了。详情参见 [官方示例](https://github.com/apache/shardingsphere/tree/4.1.1/examples/sharding-jdbc-example/sharding-example/sharding-spring-boot-mybatis-example/src/main/resources)

## 分库，又分表

略，未来我们只分表，不分库，因此这里就不展开讲解了。详情参见 [官方示例](https://github.com/apache/shardingsphere/tree/4.1.1/examples/sharding-jdbc-example/sharding-example/sharding-spring-boot-mybatis-example/src/main/resources)

## 分表+读写分离

- x

spring.shardingsphere.datasource

```properties
.names=xxx,yyy,zzz  
  
.xxx.type=com.zaxxer.hikari.HikariDataSource  
.xxx.driver-class-name=com.mysql.cj.jdbc.Driver  
.xxx.jdbc-url=jdbc:mysql://192.172.0.16:3307/ds?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8  
.xxx.username=root  
.xxx.password=123456  
  
.yyy.type=com.zaxxer.hikari.HikariDataSource  
.yyy.driver-class-name=com.mysql.cj.jdbc.Driver  
.yyy.jdbc-url=jdbc:mysql://192.172.0.16:3307/ds?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8  
.yyy.username=root  
.yyy.password=123456  
  
.zzz.type=com.zaxxer.hikari.HikariDataSource  
.zzz.driver-class-name=com.mysql.cj.jdbc.Driver  
.zzz.jdbc-url=jdbc:mysql://192.172.0.16:3307/ds?serverTimezone=Asia/Shanghai&useSSL=false&useUnicode=true&characterEncoding=UTF-8  
.zzz.username=root  
.zzz.password=123456  
```

- x

spring.shardingsphere.sharding.tables


``` properties
.department.actual-data-nodes=ds.department$->{0..1}  
.department.table-strategy.inline.sharding-column=id  
.department.table-strategy.inline.algorithm-expression=department$->{id % 2}  
```

这里有个特殊的地方，actual-data-nodes 中出现的是 _**ds**_ ，而是不是 _xxx_ 。


- x

spring.shardingsphere.sharding.master-slave-rules

```properties
.ds.master-data-source-name=xxx  
.ds.slave-data-source-names=yyy, zzz
```

## 5. 绑定表

绑定表的概念是用于提高关联查询时的速度。 

*department* 表和 *employee* 表，均以部门 ID 作为分区键进行分片，那么它们互为绑定表关系。绑定表之间的多表关联查询不会出现笛卡尔积关联，关联查询效率将大大提升。例如：

查询部门编号为 1 的部门信息，并随带查出其下的所有员工信息。逻辑上，应该执行的是如下 SQL 语句：

``` sql
select * from department d join employee e on d.id = e.department_id where d.id = 1;
```

虽然 sharding-jdbc 可以根据分表规则计算出 *id=1* 的部门信息是在 *department0* 还是在 *department1* 中，但是 *employee* 表是 *employee0* 和 *employee1* 两个，而 *department_id = 1* 的员工在这两张表中可能都存在！

这种情况下，sharding-jdbc 只能  *employee0* 和 *employee1* 两个表都查一遍。因此，实际上 sharding-jdbc 执行列 2 条关联查询 SQL 语句：

``` sql
select * from department1 d join employee0 e on d.id = e.department_id where d.id = 1;
select * from department1 d join employee1 e on d.id = e.department_id where d.id = 1;
```

如果在配置中将 *employee* 和 *department* 以相同的分区键进行分片，并将它们设置为绑定表，那么上述的情况会得到简化：与 *department0* 有关的员工信息必然在 *employee0* 中；与 *department1* 有关的员工信息必然在 *employee1* 中。

```properties
...
spring.shardingsphere.sharding.tables.department.table-strategy.inline.sharding-column=id
...
spring.shardingsphere.sharding.tables.employee.table-strategy.inline.sharding-column=department_id
...
spring.shardingsphere.sharding.binding-tables[0]=employee,department
```


这样，sharding-jdbc 未来就只执行 1 条 SQL 语句：

``` sql
select * from department1 d join employee1 e on d.id = e.department_id where d.id = 1;
```

## 6. 内置雪花算法

sharding-jdbc 通过『主键生成策略』提供了内置的雪花算法来生成主键 ID 。当然，与之对应的你的表的主键 ID 的类型应该是 BIGINT 。

``` properties
# department 表配置
...
spring.shardingsphere.sharding.tables.department.key-generator.column=id
spring.shardingsphere.sharding.tables.department.key-generator.type=SNOWFLAKE
```

```java
@Autowired
private JdbcTemplate template;

@Test
public void department() {
    final String sql = "insert into department(name, location) values(?, ?)";
    template.update(sql, "ACCOUNTING", "NEW YORK");
    template.update(sql, "RESEARCH", "DALLAS");
    template.update(sql, "SALES", "CHICAGO");
    template.update(sql, "OPERATIONS", "BOSTON");
}
```

## 8. 公共表

公共表属于系统中数据量较小，变动少，而且属于高频联合查询的依赖表。参数表、数据字典表等属于此类型。可以将这类表在每个数据库都保存一份，所有更新操作都同时发送到所有分库执行。接下来看一下如何使用 Sharding-JDBC 实现公共表。

### 建表

在 _**ds0**_ 和 _**ds1**_ 中都建立 _**department**_ 表，且表名一样，没有 0、1 之类的后缀以示区别。

### application.properties 配置

``` properties
spring.shardingsphere.sharding.broadcast-tables=department
```

### 执行测试代码

```java
@Test
public void department() {
    final String sql = "insert into department(id, name, location) values(?, ?, ?)";
    template.update(sql, 1, "ACCOUNTING", "NEW YORK");
    template.update(sql, 2, "RESEARCH", "DALLAS");
    template.update(sql, 3, "SALES", "CHICAGO");
    template.update(sql, 4, "OPERATIONS", "BOSTON");
}
```

你会发现 *ds0* 和 *ds1* 中的的 *department* 表都有完整的数据。

