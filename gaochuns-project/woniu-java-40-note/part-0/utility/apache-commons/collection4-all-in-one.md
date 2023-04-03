---
alias: ["apache commons collection4"]
tags: [commons-collection4, 工具类, CollectionUtils]
---

```xml
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.4</version>
</dependency>
```

## CollectionUtils 工具类下的常用方法


| 方法        | 说明                                   |
| ----------: | :------------------------------------  |
| [[202208272000#isEmpty 方法\|isEmpty 方法]] | 判断集合是否为空。"预期" 为空。 |
| [[202208272000#isNotEmpty 方法\|isNotEmpty 方法]] | 判断集合是否为空。"预期" 不为空。|
| [[202208272000#addAll 方法\|addAll 方法]] | 向集合中添加多个元素。|
| [[202208272000#isEqualCollection 方法\|isEqualCollection 方法]] | 判断两个集合是否相等。"预期" 相等。|
| [[202208272000#select 方法\|select 方法]] | 筛选元素。选中的元素通过新集合返回。|
| [[202208272000#filter 方法\|filter 方法]] | 筛选元素。选中的元素留下，未选中的从源集合中删除。|
| [[202208272000#transform 方法\|transform 方法]] | 形变。源集合会发生变化。|
| [[202208272000#collect 方法\|collect 方法]] | 形变。源集合不变，形变结果通过新集合返回。|


## IterableUtils 工具类下的常用方法


| 方法        | 说明                                   |
| ----------: | :------------------------------------  |
| [[202208272042#IterableUtils find 方法\|find 方法]] | 从数组和集合中选出满足条件的第一个元素 |
| [[202208272042#IterableUtils forEach 方法\|forEach 方法]] | 循环数组和集合遍历 |


## commons-collection4 数组和集合的遍历总结

| 方法 |   备注 |
| :- | :- |
| IterableUtils.forEach     | 仅仅是遍历，源数据不变。适用于数组和集合 |
| IterableUtils.find        | 选中并返回一个元素，源数据不变。适用于数组和集合。| 
| CollectionUtils.select    | 选中并返回一批数据，源数据不变。适用于集合。|
| CollectionUtils.filter    | 选中一批数据，源数据有变动（未选中的会被删除）。适用于集合。 |
| CollectionUtils.transform | 形变，源数据有变动。适用于集合。|
| CollectionUtils.collect   | 形变并返回，源数据不变。适用于集合。|


