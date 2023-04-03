---
alias: Redis 次常见命令
---

# Redis 次常见命令

## Hash 结构次常见命令 

### hsetnx 命令

Hsetnx 命令用于向某个哈希表中添加新的 *field - value* ，具体是哪个 HashTable 取决于参数 *key* 。

这里要求 *filed - value* 原本不存在于该 HashTable 中 。

如果 *filed - value* 不存在，则这个 *filed - value* 将被添加到 HashTable 中。

如果 *filed - value* 已经存在于哈希表中，操作无效。

如果 HashTable 不存在，一个新哈希表将被创建，并执行 HSETNX 命令。

语法：

```bash
hsetnx <key> <field> <value>
```

hsetnx 命令设置成功时，返回 1 。如果给定 *filed - value* 已经存在，则没有操作被执行，返回 0 。

```bash
# key 或 field 不存在时，才进行设值操作。
> HSETNX myhash field1 "foo"
(integer) 1 # 不存在，操作成功

> HSETNX myhash field1 "bar"
(integer) 0 # 存在，操作失败

> HGET myhash field1
"foo" # 不会有『覆盖』情况出现
```


### hmset 命令 

Hmset 命令用于同时将多个 *field - value* 设置到某个哈希表中，至于具体是哪个哈希表则取决于参数 *key* 。

如果 *field - value* 在 HashTable 中已存在，此命令会覆盖其旧值。

如果 HashTable 不存在，会创建一个空HashTable，再执行 Hmset 操作。

语法：

```bash
Hmset <key> <field1> <value1> [field2 value2] ... 
```

如果命令执行成功，返回 OK 。

```bash
# 一次性设置 2 个 filed-value 。
> HSET myhash field1 "foo" field2 "bar"
OK
```


### hexists 命令

Hexists 命令用于查看某个HashTable中是否存在某个 *filed - values* 。

语法：

```bash
Hexists key field
```

如果哈希表含有给定 *field - value* ，返回 1 ； 果哈希表不含有给定 *field - value* ，返回 0 。

```bash
> HSET myhash field1 "foo"
(integer) 1

> HEXISTS myhash field1
(integer) 1

> HEXISTS myhash field2
(integer) 0
```





## List 结构次常见命令

### Linsert 命令

Linsert 命令用于在某个列表的元素<small>（ 锚点元素 ）</small>前/后插入元素。

> [!command] 语法：  Linsert &lt;key> [before | after] &lt;pivot> &lt;value>

- 如果被操作键值对不是 list 类型，返回一个错误；
- 如果列表不存在或为空列表，返回 0 ；
- 如果 Linsert 命令执行成功，返回插入操作完成之后，列表的长度；
- 如果没有找到指定元素 ，返回 -1；

[[Redis 常用命令示例#Linsert 命令示例|示例]]


### Lrem 命令


Lrem 根据参数 *count* 的值，移除某个列表中与参数 *value* 相等的元素。

*count* 的值可以是以下几种：

- count > 0 : 从表头开始向表尾搜索，移除与 VALUE 相等的元素，数量为 *count* 。

- count < 0 : 从表尾开始向表头搜索，移除与 VALUE 相等的元素，数量为 *count* 的绝对值。

- count = 0 : 移除表中所有与 VALUE 相等的值。

> [!command] 语法： Lrem &lt;key> &lt;count> &lt;value>

Lrem 命令返回被移除元素的数量。 列表不存在时返回 0 。

[[Redis 常用命令示例#Lrem 命令|示例]]
## Set 结构次常见命令

### Spop 命令 

Spop 命令用于『**随机**』移除，并返回集合中的一个元素。

> [!command] 语法： Spop &lt;key>

被移除的随机元素。 当集合不存在或是空集时，返回 *nil* 。

[[Redis 常用命令示例#Spop 命令示例|示例]]

### Srandmember 命令 

Srandmember 命令用于从某个集合中返回一个或多个随机元素。

从 Redis 2.6 版本开始， Srandmember 命令接受可选的 *count* 参数：

如果 *count* 为正数，且小于集合基数，那么命令返回一个包含 *count* 个元素的数组，数组中的元素各不相同。如果 *count* 大于等于集合基数，那么返回整个集合。

如果 *count* 为负数，那么命令返回一个数组，数组中的元素可能会重复出现多次，而数组的长度为 count 的绝对值。

返回的元素并不会从集合中移除。

> [!command] 语法：Srandmember &lt;key> [ count ]

如果没有 *count* 参数，Srandmember 将返回一个元素；如果提供了 *count* 参数，那么返回一个数组；如果集合为空，返回空数组；如果集合为空，返回 nil 。

[[Redis 常用命令示例#Srandmember 命令示例|示例]]


### Smove 命令 

Smove 命令将指定成员 *member* 元素从 *source* 集合移动到 *destination* 集合。

SMOVE 是原子性操作。

如果 source 集合不存在或不包含指定的 member 元素，则 SMOVE 命令不执行任何操作，仅返回 0 。

否则， member 元素从 source 集合中被移除，并添加到 destination 集合中去。

当 destination 集合已经包含 member 元素时， SMOVE 命令只是简单地将 source 集合中的 member 元素删除。

当 source 或 destination 不是集合类型时，返回一个错误。

> [!command] 语法：SMOVE &lt;source> &lt;destination> &lt;member>

如果成员元素被成功移除，SMOVE 返回 1 。

如果成员元素不是 source 集合的成员，并且没有任何操作对 destination 集合执行，那么返回 0 。

[[Redis 常用命令示例#Smove 命令示例|示例]]






## ZSet 结构次常见命令

### Zrank 命令

Zrank 返回有序集中指定成员的排名。其中有序集成员按分数值递增<small>（从小到大）</small>顺序排列。

> [!command] 语法：ZRANK &lt;key> &lt;member>

如果成员是有序集的成员，ZRANK 返回 member 的排名；如果成员不是有序集的成员，返回 nil 。

[[Redis 常用命令示例#Zrank 命令示例|zrank 示例]]


### Zrevrank 命令

Zrevrank 命令返回有序集中成员的排名。其中有序集成员按分数值降序排序。

使用 Zrank 命令可以获得成员按分数值升序排序。

> [!command] 语法：Zrevrank &lt;key> &lt;member>

如果成员是有序集的成员，Zrevrank 返回成员的排名；如果成员不是有序集的成员，返回 nil 。

[[Redis 常用命令示例#Zrevrank 命令示例|Zrevrank 示例]]


### Zcount 命令

Zcount 命令用于计算某有序集合中指定分数区间的成员数量。

> [!command] 语法：Zcount &lt;key> &lt;min> &lt;max>

Zcount 返回分数值在 _min_ 和 _max_ 之间的成员的数量。

[[Redis 常用命令示例#Zcount 命令示例|Zcount 示例]]


