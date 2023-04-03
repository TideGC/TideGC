---
alias: [Redis 常用命令示例]
---

## Set 命令示例

- [[202209082231#set 命令示例]]

- [[202209082236#get 命令示例]]

## Hash 命令示例

[[202209120851]]

[[202209120858#Hset 命令示例]]

[[202209120915#hdel 命令示例]]

## List 命令示例

### lpush 命令示例




```bash
> LPUSH 9527 10
(integer) 1

> LPUSH 9527 20
(integer) 2

> LRANGE 9527 0 -1
1) "10"
2) "20"
```

### rpush 命令示例

```bash
> RPUSH 9528 "hello"
(integer) 1

> RPUSH 9528 "world"
(integer) 2

> RPUSH 9528 "goodbye"
(integer) 3

> LRANGE 9528 0 -1
1) "hello"
2) "world"
3) "goodbye"
```

### lindex 命令示例

```bash
> LPUSH list_1 "World"
(integer) 1
    
> LPUSH list_1 "Hello"
(integer) 2
    
> LINDEX list_1 0
"Hello"
    
> LINDEX list_1 -1
"World"
    
> LINDEX list_1 3        # index不在 mylist 的区间范围内
(nil)
```

### llen 命令示例


```bash
> RPUSH list1 "foo"
(integer) 1

> RPUSH list1 "bar"
(integer) 2

> LLEN list1
(integer) 2
```

### lpop 命令示例


```bash
> RPUSH list1 "foo"
(integer) 1

> RPUSH list1 "bar"
(integer) 2

> LPOP list1
"foo"
```

### Rpop 命令示例


```bash
> RPUSH mylist "hello"
(integer) 1

> RPUSH mylist "hello"
(integer) 2

> RPUSH mylist "foo"
(integer) 3

> RPUSH mylist "bar"
(integer) 4

> RPOP mylist
OK

> LRANGE mylist 0 -1
1) "hello"
2) "hello"
3) "foo"
```

### Linsert 命令示例


```bash
> RPUSH list1 "foo"
(integer) 1

> RPUSH list1 "bar"
(integer) 2

> LINSERT list1 BEFORE "bar" "Yes"
(integer) 3

> LRANGE mylist 0 -1
1) "foo"
2) "Yes"
3) "bar"
```

### Lrange 命令

```bash
> LPUSH list1 "foo"
(integer) 1

> LPUSH list1 "bar"
(integer) 2

> LPUSHX list1 "bar"
(integer) 0

> LRANGE list1 0 -1
1) "foo"
2) "bar"
3) "bar"
```

### Lrem 命令




```bash
> RPUSH mylist "hello"
(integer) 1

> RPUSH mylist "hello"
(integer) 2

> RPUSH mylist "foo"
(integer) 3

> RPUSH mylist "hello"
(integer) 4

> LREM mylist -2 "hello"
(integer) 2
```

## Set 命令示例

### Sadd 命令示例


```bash
> SADD myset "hello"
(integer) 1

> SADD myset "foo"
(integer) 1

> SADD myset "hello"
(integer) 0

> SMEMBERS myset
1) "hello"
2) "foo"
```

### Scard 命令示例


```bash
> SADD myset "hello"
(integer) 1

> SADD myset "foo"
(integer) 1

> SADD myset "hello"
(integer) 0

> SCARD myset
(integer) 2
```

### Sismember 命令示例


```bash
> SADD myset1 "hello"
(integer) 1

> SISMEMBER myset1 "hello"
(integer) 1

> SISMEMBER myset1 "world"
(integer) 0
```

[[202209120941#Smembers 命令示例|smembers 命令示例]]

### Srandmember 命令示例

```bash
> SADD myset1 "hello"
(integer) 1

> SADD myset1 "world"
(integer) 1

> SADD myset1 "bar"
(integer) 1

> SRANDMEMBER myset1
"bar"

> SRANDMEMBER myset1 2
1) "Hello"
2) "world"
```

### Smove 命令示例


```bash
> SADD myset1 "hello"
(integer) 1

> SADD myset1 "world"
(integer) 1

> SADD myset1 "bar"
(integer) 1

> SADD myset2 "foo"
(integer) 1

> SMOVE myset1 myset2 "bar"
integer) 1

> SMEMBERS myset1
1) "World"
2) "Hello"

> SMEMBERS myset2
1) "foo"
2) "bar"
```

### Spop 命令示例 


```bash
> SADD myset1 "hello"
(integer) 1

> SADD myset1 "world"
(integer) 1

> SADD myset1 "bar"
(integer) 1

> SPOP myset1
"bar"

> SMEMBERS myset1
1) "Hello"
2) "world"
```

### Srem 命令示例

Srem 返回被成功、实际移除的元素的数量，不包括被忽略的元素。

```bash
> SADD myset1 "hello"
(integer) 1

> SADD myset1 "world"
(integer) 1

> SADD myset1 "bar"
(integer) 1
> SREM myset1 "hello"
(integer) 1

> SREM myset1 "foo"
(integer) 0

> SMEMBERS myset1
1) "bar"
2) "world"
```

## ZSet 命令示例

### Zadd 命令示例


```bash
> ZADD set1 1 "hello"
(integer) 1
> ZADD set1 1 "foo"
(integer) 1
> ZADD set1 2 "world" 3 "bar"
(integer) 2
    
> ZRANGE set1 0 -1 WITHSCORES
1) "hello"
2) "1"
3) "foo"
4) "1"
5) "world"
6) "2"
7) "bar"
8) "3"
```

### Zcard 命令示例


```bash
> ZADD myset 1 "hello"
(integer) 1

> ZADD myset 1 "foo"
(integer) 1

> ZADD myset 2 "world" 3 "bar"
(integer) 2

> ZCARD myzset
(integer) 4
```

### Zcount 命令示例


```bash
> ZADD myzset 1 "hello"
(integer) 1

> ZADD myzset 1 "foo"
(integer) 1

> ZADD myzset 2 "world" 3 "bar"
(integer) 2

> ZCOUNT myzset 1 3
(integer) 4
```

### Zincrby 命令示例

```bash
> ZADD myzset 1 "hello"
(integer) 1

> ZADD myzset 1 "foo"
(integer) 1

> ZINCRBY myzset 2 "hello"
(integer) 3

> ZRANGE myzset 0 -1 WITHSCORES
1) "foo"
2) "2"
3) "hello"
4) "3"
```

### Zrem 命令示例

```bash
# 基本示例
> ZRANGE page_rank 0 -1 WITHSCORES
1) "bing.com"
2) "8"
3) "baidu.com"
4) "9"
5) "google.com"
6) "10"

# 移除单个元素
> ZREM page_rank google.com
(integer) 1
    
> ZRANGE page_rank 0 -1 WITHSCORES
1) "bing.com"
2) "8"
3) "baidu.com"
4) "9"

# 移除多个元素
> ZREM page_rank baidu.com bing.com
(integer) 2

> ZRANGE page_rank 0 -1 WITHSCORES
(empty list or set)


# 移除不存在元素
> ZREM page_rank non-exists-element
(integer) 0
```

### Zrange 命令示例


```bash
# 显示整个有序集成员
> ZRANGE salary 0 -1 WITHSCORES
1) "jack"
2) "3500"
3) "tom"
4) "5000"
5) "boss"
6) "10086"

# 显示有序集下标区间 1 至 2 的成员
> ZRANGE salary 1 2 WITHSCORES
1) "tom"
2) "5000"
3) "boss"
4) "10086"

# 测试 end 下标超出最大下标时的情况
> ZRANGE salary 0 200000 WITHSCORES
1) "jack"
2) "3500"
3) "tom"
4) "5000"
5) "boss"
6) "10086"

# 测试当给定区间不存在于有序集时的情况
> ZRANGE salary 200000 3000000 WITHSCORES
(empty list or set)
```

### Zrank 命令示例


```bash
# 显示所有成员及其 score 值
> ZRANGE salary 0 -1 WITHSCORES
1) "peter"
2) "3500"
3) "tom"
4) "4000"
5) "jack"
6) "5000"

# 显示 tom 的薪水排名，第二
> ZRANK salary tom
(integer) 1
```

### Zrevrank 命令示例




```bash
> ZRANGE salary 0 -1 WITHSCORES     # 测试数据
1) "jack"
2) "2000"
3) "peter"
4) "3500"
5) "tom"
6) "5000"
    
> ZREVRANK salary peter     # peter 的工资排第二
(integer) 1

> ZREVRANK salary tom       # tom 的工资最高
(integer) 0
```

### Zscore 命令示例


```bash
> ZRANGE salary 0 -1 WITHSCORES    # 测试数据
1) "tom"
2) "2000"
3) "peter"
4) "3500"
5) "jack"
6) "5000"
    
> ZSCORE salary peter              # 注意返回值是字符串
"3500"
```

