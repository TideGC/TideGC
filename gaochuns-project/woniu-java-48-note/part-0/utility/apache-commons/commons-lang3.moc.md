---
alias: 
- StringUtils
tags: 
- 工具类
- commons-lang3
---

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-lang3</artifactId>
    <version>3.11</version>
</dependency>
```

- [[202302011857.StringUtils|StringUtils 工具类常见方法]]
- [[202302011954.ArrayUtils]]

## ObjectUtils 工具类常见方法

-tx-
| 方法          | 说明                                                     |
| :------------ | :------------------------------------------------------- |
| [[202208271914\|defaultIfNull 方法]] | 用于提供默认值。检查第一个参数是否为 null 。             |\
|                    | 不为 null，则返回第一个参数；为 null，则返回第二个参数。 |
| [[202208271915\|allNotNull 方法]]    | 用于参数的批量非空检查                                   |
| [[202208271917\|equals 方法]]        | 空安全的比较。被标记为过期，不推荐使用。                 |\
|                    | 被 java 7 中的 _Objects.equals(Object, Object)_ 所替代。|
| [[202208271923\|isEmpty 方法]]       | 判断字符串, 数组, 集合, Map 是否为空 |


## 其它

- [[99-反射工具类|反射相关的工具类]]


