---
alias: Redis 常用命令
---


# Redis 常用命令汇总


## 1. 通用命令 

| 命令                          | 说明                                                                          |
|:----------------------------- |:----------------------------------------------------------------------------- |
| dbsize                        | 计算 key 的总数                                                               |
| keys \*                       | 遍历所有的 key 。<br>keys 命令一般不在生产环境中使用                          |
| type &lt;key>                 | 返回 key 的数据模型，即值的类型。                                             |
| exists &lt;key>               | 存在则返回 1，不存在返回 0                                                    |
| del &lt;key> [ &lt;key> ... ] | 删除指定的键值对                                                              |
| expire &lt;key> &lt;seconds>  | key 在 seconds 秒后过期。                                                     |
| ttl &lt;key>                  | 查询 key 的剩余过期时间。<br>返回 -1 表示没有过期设置；返回 -2 表示过期删除。 |
| persist &lt;key>              | 去掉 key 的过期设置                                                           |


## 2. 字符串命令 

Redis 中的键<small>（ key ）</small>都是字符串，但是值可以有多种类型<small>（ 常见 5 种 ）</small>。

字符串类型的值<small>（ Value ）</small>最大不能超过 512M<small>（ 已经足够大了 ）</small>。

一般情况下，考虑到并发、流量等问题，通常字符串类型的值最大也只是『百K』级别。

主要的专有命令是 ***get*** 和 ***set*** 命令。

删除 string 类型键值对可使用通用 *del* 命令。 

[[202209082231|字符串结构命令：set 命令]]

[[202209082231#带参数的 set 命令\|带参数的 set 命令]]

[[202209082236#get 命令|get 命令]]

[[202209082236#get 命令示例|get 命令示例]]

## 3. 哈希命令 

在 Hash 结构中，键值对的值又分为 2 个部分：**field** 和 **value** 。

Hash 类型的价值在于，在 Redis 中存储了一个对象的信息后，可以单独更新该对象的某个属性的值，而不需要：取出-更新-序列化-存入 。

注意，与数据库中的列不同，Hash 结构中不强求两个键值对中必须有同样数量/名称的 field 。

主要的专有命令是 *hset* 、 *hget* 和 *gdel* 命令。


### hset 命令 

- [[202209120858#hset 命令|hset 命令]]

- [[202209120858#Hset 命令示例|hset 命令示例]]

### hget 命令

- [[202209120851#hget 命令|hget 命令]]

- [[202209120851#hget 命令示例|hget 命令示例]]

### hdel 命令
- [[202209120915#hdel 命令]]
- [[202209120915#hdel 命令示例]]


### 其它可能遇到的 Hash 结构命令

- [[Redis 次常见命令汇总#hsetnx 命令|hsetnx 命令]]
- [[Redis 次常见命令汇总#hmset 命令|hmset 命令]]
- [[Redis 次常见命令汇总#hexists 命令|hexists 命令]]


## 4. 集合（Set）命令

特点：

- 无序
- 无重复
- 集合间操作

| 集合内 API | 集合间 API |
| :- | :- |
| sadd | sdiff |
| srem | sinter |
| scard | sunion |
| sismember ||
| srandmember ||
| smembers ||
| spop ||
[[202209120941#Smembers 命令|smsmbers 命令]]

[[202209120941#Smembers 命令示例|smembers 命令示例]]

### Sadd 命令 

Sadd 命令将一个或多个成员元素加入到某个集合中，已经存在于集合的成员元素将被忽略。

假如集合不存在，则创建一个新的集合，而后再执行 Sadd 操作。

当 *key* 对应的并非集合类型时，返回一个错误。

> [!command] 语法
> ```sh
> SADD key value [ value ... ]
> ```

Sadd 命令将被添加到集合中的新元素的数量，不包括被忽略的元素。

[[Redis 常用命令示例#Sadd 命令示例|示例]]

### Srem 命令 

Srem 命令用于移除集合中的一个或多个成员元素，不存在的成员元素会被忽略。

当 key 对应的不是集合类型时，返回一个错误。

> [!command] 语法
> ```sh
> SREM key member [ member ... ]
> ```

[[Redis 常用命令示例#Srem 命令示例|Srem 示例]]


### Scard 命令 

Scard 命令返回某个集合中元素的数量。

> [!command] 语法
> ```sh
> SCARD key
> ```

Scard 命令将返回集合中元素的数量；当集合 key 不存在时，返回 0 。

[[Redis 常用命令示例#Scard 命令示例|Scard 示例]]


### Sismember 命令 

Sismember 命令判断成员元素是否是某个集合的成员。

> [!command] 语法
> ```sh
> SISMEMBER key value
> ```

如果成员元素是集合的成员，Sismsmber 将返回 1 ；如果成员元素不是集合的成员，或集合不存在，返回 0 。

[[Redis 常用命令示例#Sismember 命令示例|示例]]

### 其它可能会遇到的 set 结构命令

- [[Redis 次常见命令汇总#Spop 命令|Spop 命令]]
- [[Redis 次常见命令汇总#Srandmember 命令|Srandmember 命令]]
- [[Redis 次常见命令汇总#Smove 命令|Smove 命令]]



## 5. 链表命令 

| 增 | 删 | 改 | 查 |
| :- | :- | :- | :- |
| lpush   | lpop  | lset   | lrange |
| linsert | rpop  | lindex |        |
|         | lrem  | llen   |        |

### Lrange 命令

Lrange 返回某个列表中指定区间内的元素，区间以参数 *start* 和 *end* 指定。其中 0 表示列表的第一个元素， 1 表示列表的第二个元素，以此类推。

你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

> [!command] 语法
> ```sh
> LRANGE key start end
> ```

Lrange 命令返回一个列表，包含指定区间内的元素。

[[Redis 常用命令示例#Lrange 命令|Lrange 示例]]


### Lpush / Rpush 命令

- *Lpush* 命令将一个或多个值插入到某个列表头部<small>（ 左侧 ）</small>，具体是哪个链表由参数 *key* 决定。
- *Rpush* 命令用于将一个或多个值插入到某个列表的尾部<small>（ 最右边 ）</small>，具体是哪个列表由参数 *key* 决定。

> [!command] 语法
> ```sh
> LPUSH key value [ value ... ] 
> RPUSH key value [ value ... ]
> ```

- 如果链表不存在，一个空列表会被创建，而后再执行 Lpush / Rpush 操作；
- 执行 Lpush / Rpush 命令后，将返回列表的长度。
- 如果 *key* 对应的类型不是列表类型时，返回一个错误。

[[Redis 常用命令示例#lpush 命令示例|Lpush 示例]] | [[Redis 常用命令示例#rpush 命令示例|Rpush 示例]]


### Lpop 命令

Lpop / Rpop 命令用于移除，并返回某个列表的第一个元素<small>（ 最左/右侧的元素 ）</small>。

> [!command] 语法
> ```sh
> Lpop key
> Rpop key
> ```

- Lpop/Rpop 将返回列表的最左/右侧的第一个元素；
- 当列表不存在时，返回 nil 。

[[Redis 常用命令示例#lpop 命令示例|Lpop 示例]] | [[Redis 常用命令示例#Rpop 命令示例|Rpop 示例]]


### Lindex 命令

Lindex 命令用于通过索引获取某个列表中的元素，具体是哪个列表由参数 *key* 决定。

索引都是从头向尾（从左到右）的方向算，从 0 开始。

你也可以使用负数下标，以 -1 表示列表的最后一个元素， -2 表示列表的倒数第二个元素，以此类推。

> [!command] 语法
> ```sh
> LINDEX key index
> ```

Lindex 命令返回列表中下标所指定的位置的值。如果指定索引值不在列表的区间范围内，返回 nil 。

[[Redis 常用命令示例#lindex 命令示例|Lindex 示例]]


### Llen 命令

Llen 命令用于返回某个列表的长度。

如果列表不存在，则返回 0 。

如果 key 对应的不是列表类型，则返回一个错误。

> [!command] 语法
> ```sh
> LLEN key
> ```

Llen 返回列表的长度。

[[Redis 常用命令示例#llen 命令示例|Llen 示例]]

### 其它可能遇到的 list 结构命令

- [[Redis 次常见命令汇总#Linsert 命令|Linsert 命令]]
- [[Redis 次常见命令汇总#Lrem 命令|Lrem 命令]]


## 6. 有序集合命令 

和『哈希』有点类似，有序集合中的键值对的值中，也是有两个部分：**score** 和 **value** 。

score 的值决定了与之对应的 value 的顺序

### Zrange 命令

Zrange 返回某有序集中，指定区间内的成员。<small>如果需要逆序显示，请使用 *Zrevrange* 命令。</small>


> [!command] 语法
> ```sh
> ZRANGE key start stop [ WITHSCORES ]
> ```

Zrange 命令将指定区间内，带有分数值<small>（ 可选 ）</small>的有序集成员的列表。

[[Redis 常用命令示例#Zrange 命令示例|Zrange 示例]]


### Zadd 命令

Zadd 命令用于将一个或多个成员元素及其分数值加入到某个有序集当中。

分数值可以是整数值或双精度浮点数，不过通常使用整数值。

> [!command] 语法
> ```sh
> ZADD key score value [scoren value ... ]
> ```

- 如果有序集合不存在，则创建一个空的有序集并执行 Zadd 操作；
- 如果某个成员还不是有序集合的成员，那么执行的就是新增逻辑；
- 如果某个成员已经是有序集的成员，那么执行的就是更新分数值逻辑，<small>有序集合内部会重新调整成员元素的位置，来保证这个集合的有序性</small>；
- 如果 *key* 所对应的并非有序集类型时，返回一个错误。

Zadd 命令将被成功添加的新成员的数量，不包括那些被更新的、已经存在的成员。

[[Redis 常用命令示例#Zadd 命令示例|Zadd 示例]]




### Zincrby 命令

Zincrby 命令对某有序集合中指定成员的分数加上增量 *increment* 。

可以通过传递一个负数值 *increment* ，让分数减去相应的值，比如 `ZINCRBY key -5 member` ，就是让 member 的 score 值减去 5 。

当有序集合不存在，或有序集合中不存在指定分数时， Zincrby 等同于 Zadd 。

当 key 对应的不是有序集时，返回一个错误。

分数值可以是整数值或双精度浮点数。

> [!command] 语法
> ```sh
> ZINCRBY key increment member
> ```

Zincrby 命令返回参数 _member_ 的新的分数值<small>（以字符串形式表示）</small>。

[[Redis 常用命令示例#Zincrby 命令示例|Zincrby 示例]]


### Zrem 命令

Zrem 命令用于移除某个有序集中的一个或多个成员，不存在的成员将被忽略。

如果 key 对应的并非是有序集类型，则返回一个错误。

> [!command] 语法
> ```sh
> ZREM key member
> ```

[[Redis 常用命令示例#Zrem 命令示例|Zrem 示例]]

### Zscore 命令

Zscore 命令返回有序集中，成员的分数值。

如果成员元素不是有序集的成员，或有序集合不存在，返回 nil 。

> [!command] 语法
> ```sh
> ZSCORE key member
> ```

Zscore 返回成员的分数值<small>（ 以字符串形式表示 ）</small>。

[[Redis 常用命令示例#Zscore 命令示例|Zscore 示例]]


### Zcard 命令

Zcard 命令用于计算某个集合中元素的数量。

> [!command] 语法
> ```sh
> Zcard key
> ```

当集合存在时，Zcard 返回有序集的基数；当集合不存在时，返回 0 。

[[Redis 常用命令示例#Zcard 命令示例|Zcard 示例]]

### 其它可能遇到的 Zset 结构命令

- [[Redis 次常见命令汇总#Zrank 命令|Zrank 命令]]：查询有序集中指定成员的排名。
- [[Redis 次常见命令汇总#Zrevrank 命令|Zrevrank 命令]]：查询有序集中指定成员的逆序排名。
- [[Redis 次常见命令汇总#Zcount 命令|Zcount 命令]]：用于计算某有序集合中指定分数区间的成员数量。

