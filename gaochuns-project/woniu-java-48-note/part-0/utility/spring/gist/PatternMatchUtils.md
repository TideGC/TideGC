
---
alias:
- PatternMatchUtils 
tags:
- 工具类 
- spring utility 
---

## PatternMatchUtils


> [!details] 说明
> 判断字符串以 xxx 开始 / 结束 / 包含：PatternMatchUtils 类

PatternMatchUtils 类来自 org.springframework.util 包 。

它用于进行简单地正则匹配。判断规则有：`xxx*` 、`*xxx` 、`*xxx*` 和 `xxx*yyy` 。`*` 通配任意个字符。例如：`hello*` 、`*hello` 、`*hello*` 和 `hello*world` 。

下述方法的参数中，正则规则字符串在前。


```java
import static org.springframework.util.PatternMatchUtils.simpleMatch;

// 判断字符串是否符合规则。
boolean simpleMatch(String pattern, String str) 

// 判断字符串是否同时满足多个规则。
boolean simpleMatch(String[] patterns, String str) 
```


```java
PatternMatchUtils.simpleMatch("hello*", "hello world");      = true
PatternMatchUtils.simpleMatch("*hello", "hello world");      = false
PatternMatchUtils.simpleMatch("*hello*", "hello world");     = true
PatternMatchUtils.simpleMatch("hello*world", "hello world"); = true
```

