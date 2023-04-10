# ElasticsearchTemplate

> 了解


```java
@Autowired
private ElasticsearchTemplate elasticsearchTemplate;
```


## 创建索引


-   方式一：直接用名称创建

    ```java
    elasticsearchTemplate.createIndex("book");
    ```

-   方式二：填入 class 对象

    ```java
    elasticsearchTemplate.createIndex(BookEntity.class);
    ```

## 创建、添加数据

-   创建、添加单条数据

    ```java
    BookEntity bookEntity = new BookEntity();
    bookEntity.setId("2");
    bookEntity.setAuthor("老李");
    bookEntity.setName("一起学习es");

    IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(bookEntity.getId())
                .withObject(bookEntity)
                .build();
    elasticsearchTemplate.index(indexQuery);
    ```

-   创建、添加多条数据

    ```java
    BookEntity bookEntity = …

    IndexQuery indexQuery1 = new IndexQueryBuilder()
        .withId(bookEntity.getId())
        .withObject(bookEntity)
        .build();
    indexQueries.add(indexQuery1);

    BookEntity bookEntity2 = …

    IndexQuery indexQuery2 = new IndexQueryBuilder()
        .withId(bookEntity2.getId())
        .withObject(bookEntity)
        .build();
    indexQueries.add(indexQuery2);

    elasticsearchTemplate.bulkIndex(indexQueries);
    ```


## 单字符串查询

```java
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.queryStringQuery("菜鸟"))
    .withPageable(new PageRequest(0, 20))
    .build();
List<BookEntity> list = elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
```

## 排序

```java
Pageable pageable= new PageRequest(0, 20,new Sort(Sort.Direction.DESC, "name"));
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.queryStringQuery("菜鸟"))
    .withPageable(pageable)
    .build();
Page<BookEntity> list = elasticsearchTemplate.queryForPage(searchQuery, BookEntity.class);
```

注意：如果出现此异常

```
java.lang.IllegalArgumentException: Fielddata is disabled on text fields by default
```

官方解释5.x后对排序，聚合这些操作用单独的数据结构(fielddata)缓存到内存里了，需要单独开启（主要是占用资源太多，所以是否开启，需要仔细斟酌）

关于fielddata更多内容：https://www.elastic.co/guide/en/elasticsearch/reference/current/fielddata.html

## 模糊查询

此模糊查询与mysql中的模糊查询不太一样，此模糊查询类似分词匹配。搜索条件会被分词。
比如有两条数据：1、我今天非常高兴 2、他摔倒很高兴
输入：今天高兴
这两条数据都能匹配上。

```java
Pageable pageable = new PageRequest(0, 10);
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.matchQuery("name", "菜鸟"))
    .withPageable(pageable)
    .build();
List<BookEntity> list = elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
```

## 其余匹配

其余匹配类似mysql中like "%word%"的模糊匹配

```java
Pageable pageable = new PageRequest(0, 10);
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.matchPhraseQuery("name", "菜鸟"))
    .withPageable(pageable)
    .build();
List<BookEntity> list = elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);

```

## Term全等查询

```java
Pageable pageable = new PageRequest(0, 10);
SearchQuery searchQuery = new NativeSearchQueryBuilder()
    .withQuery(QueryBuilders.termQuery("name", "菜鸟"))
    .withPageable(pageable)
    .build();
List<BookEntity> list = elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
```

## 组合查询

即boolQuery，可以设置多个条件的查询方式。它的作用是用来组合多个Query，有四种方式来组合，must，mustnot，filter，should。
must代表返回的文档必须满足must子句的条件，会参与计算分值；
filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
mustnot代表必须不满足子句的条件。

```java
QueryBuilder filterQuery = QueryBuilders
        .boolQuery()
        .filter(QueryBuilders.termQuery("name", "菜鸟"))
        .filter(QueryBuilders.termQuery("author", "小菜"));
List<BookEntity> list = elasticsearchTemplate.queryForList(filterQuery, BookEntity.class);
```
