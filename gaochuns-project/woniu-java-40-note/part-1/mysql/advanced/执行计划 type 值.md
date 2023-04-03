执行计划的 type 字段的值表示查询语句的查询方式。查询方式的不同意味着查询速度的快慢差异。

下述各个值的讲解顺序是以从快到慢的顺序排布的。

## system

我们在使用 InnoDB 引擎的情况下不会遇到这个值。它在 MyISAM 引擎中使用，当且仅当表中有且仅有一条数据时，你查询它，就是以这种方式查询的。

## const

当我们根据唯一主键<small>（ primary key ）</small>或者唯一索引<small>（ unique ）</small>与常量等值匹配时，就会遇到这个值。

这是我们<small>（ 在使用 InnoDB 情况下 ）</small>能遇到的最快的情况了。

```sql
-- SQL 
explain select id from department where id =1;
```

关键词：
- 主键索引、唯一索引
- 等值判断

## eq_ref

执行连接查询时，如果被驱动表是通过主键，或者不允许为空的唯一索引<small>（ unique + not null ）</small>进行等值匹配的方式查询，那么对被驱动表的查询方式就是这个值。

```sql
-- SQL
explain 
    select e.id  
from employee e  
         left join department d on d.id = department_id;
```

关键词：
- 被驱动表
- 主键索引、非空且唯一索引
- 等值判断

## ref

当通过普通索引与常量进行等值匹配查询时，MySQL 就是以这种方式查询的。

```sql
-- SQL
explain 
    select id from department where name = 'SALES';
```

如果是连接查询，被驱动表中的某个普通索引与驱动表中的某个列进行等值匹配时，被驱动表的查询方式也是这种方式。

关键词：

- 普通索引
- 等值判断


## fulltext

我们遇不到，它是 MyISAM 引擎中查询时才会出现的情况。


## ref_or_null

当对普通索引进行等值匹配，且该索引列的值也有可能是 NULL 值时，对表的查询就是这种方式。

```sql
-- SQL
explain  
	select id from department where 
		location = 'Wuhan' or  location is null;
```


## index_merge
如果你使用了 2 个不同的索引作为查询条件，InnoDB 引擎就是以这种方式查询的。

```sql
-- SQL
explain  
	select id from department 
		where name = 'SALES' or location = 'BOSTON';
```


## unique_subquery

unique_subquery 是针对一些包含 IN 子查询的查询语句。

如果查询优化器决定将 IN 子查询转换为 EXISTS 子查询，且子查询在转换后可以使用主键，或者不为空的唯一索引进行等值匹配，那么该子查询的查询类型就是 unique_subquery 。

> 没试出来，可能是因为子查询被查询优化器优化成了连接查询。


## index_subquery

在访问子查询中的表时，使用的是普通索引。

> 没试出来，可能是因为子查询被查询优化器优化成了连接查询。

## range

如果使用索引获取某些单点扫描区间的记录，那么就可能用到 range 访问。

```sql
-- SQL
explain
	select * from department where id in (1, 2);
```


## index

触发索引覆盖<small>（不需要做回表查询）</small>，但需要扫描全部的索引记录。

```sql
-- SQL
explain  
	select id, name, job from employee where job = 'CLERK';
```

> [!question] 如何如何制造这种场景：
> 1. 为 employee 表创建联合索引 (name, job)
> 2. 因为没有使用联合索引的最左值作为查询条件，所以不会触发 type=ref 的情况。
> 3. 因为查询结果只要求返回 (id, name, job)，因此出发了索引覆盖，不再需要一次回表查询。

另外，还有一种特殊情况也会触发 index 方式的查询：全表扫描，且需要对主键排序。

```sql
-- SQL
explain  
	select * from employee order by id;
```


## all

这是就是传说中的全表扫描。

```sql
-- SQL
explain  
	select * from employee where salary = 3000;
```


## 与之有关的优化准则

### 在 WHERE 中尽量使用等值判断

之前在查询计划的 type 字段值的解释中大家已经看到了，等值判断才有可能触发索引，非等值判断无论时触发 range 查询，还是 all 查询，其速度都要低于等值判断的 查询。

### 尽量避免全表扫描

对查询进行优化，应尽量避免全表扫描，首先应考虑在 where 及 order by 涉及的列上建立索引。

### 用 UNION 来代替 OR

使用 OR 进行查询，在 explain 中它的查询方式是 range 方式，而使用 UNION 来代替之后，它的查询方式是 ref 或者是 const 。虽然看似 MySQL 做了更多的工作，但是效率却更高。

### like 语句避免前置百分号

like 中使用前置百分号，查询方式是 ALL ，全表扫描，很显然查询没有走索引，索引失效。如果仅使用后置百分号，查询方式是 range ，索引生效。

