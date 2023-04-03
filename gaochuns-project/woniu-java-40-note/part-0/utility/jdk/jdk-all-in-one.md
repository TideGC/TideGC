
## JDK Objects 工具类

-tx-
|工具类|           方法 | 概要   | 详情 |
|:-| -------------: | :----- | :- |
|Objects|         isNull | 判空   | [[202211161045\|详情]] |
|^^|        nonNull | ^^     | ^^ |
|^^| requireNonNull | ^^     | ^^ |
|^^|         equals | 判等   | [[202211171004\|详情]] |
|^^|     deepEquals | ^^     | ^^ |
|^^|       hashCode | 哈希值 | [[202211171005\|详情]] |
|^^|           hash | ^^     | ^^ |
|^^|       toString | 字符串 | [[202211171006\|详情]]|


## JDK Arrays 工具类

-tx-
|工具类|方法 | 概要 | 详情
|:-|-: |:-  | :-
|Arrays|asList | 数组转列表（列表有特殊性：不可变）| [[202211161129\|详情]] 
|^^|binarySearch | 在数组中进行二分查找 | [[202211161133\|详情]]
|^^|copyOf | 复制数组 | [[202211161134\|详情]]
|^^|copryOfRange | 复制数组（只复制其中一段）| [[202211161135\|详情]]
|^^|equals | 比较 | [[202211161138\|详情]]|
|^^|deepEquals |^^|^^|
|^^|fill | 填充数组 | [[202211161139\|详情]]  
|^^|sort | 对数组排序 | [[202211161146\|详情]]
|^^|stream | 生成 Stream 流对象 | [[202211161213\|详情]]
|^^|toString | 字符串 | [[202211161217\|详情]]


## JDK Collections 工具类

Collections 类是 Java 在 `java.util` 包下提供的一个操作 Set、List 和 Map 等集合的工具类。Collections 类提供了许多操作集合的静态方法，借助这些静态方法可以实现集合元素的排序、查找替换和复制等操作。


-tx-
|工具类|方法 | 概要 | 详情|
|:-|-: |:-  | :-|
|Collections|sort|调序 |[[202211111520\|详情]]|
|^^|reverse|^^|^^|
|^^|shuffle|^^|^^|
|^^|swap|^^|^^|
|^^|rotate|^^|^^|
|^^|binarySearch|查找|[[202211111522\|详情]]|
|^^|max|^^|^^|
|^^|min|^^|^^|
|^^|frequency|^^|^^|
|^^|indexOfSubList|^^|^^|
|^^|lastIndexOfSubList|^^|^^|
|^^|fill|填充|[[202211111523\|详情]]|
|^^|replaceAll|替换|[[202211171119\|详情]]|
|^^|copy|复制|[[202211111524\|详情]]|
|^^| empty...|其它| [[202211111525\|详情]] |
|^^| synchronized...| ^^ | ^^ |
|^^| unmodifiable...| ^^ | ^^ |
