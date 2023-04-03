---
alias: Spring IoC 的 XML 配置
---


# Spring IoC 的 XML 配置

## 1. Spring 创建 Bean 

Spring 允许你在一个<small>（或多个）</small>XML 配置文件中配置 Bean，对于 Spring IoC 容器，这个配置文件就是创建、管理 Bean 的依据。

一个 Spring XML 配置文件的基本样式是：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
            http://www.springframework.org/schema/beans/spring-beans.xsd">

</beans>
```

### 使用类自身的构造方法

每个 Bean 都必须提供一个唯一的名称或 id，以及一个完全限定的类名，用来让 Spring IoC 容器对其进行创建。Spring 通过类的构造方法来创建 Bean 对象。

```xml
<bean id="xxx" class="xxx.xxx.Xxx"></bean>
```

### 使用工厂类提供的工厂方法


```xml
<bean id="tom" class="工厂类" factory-method="工厂方法"></bean>
```


### 使用工厂对象提供的工厂方法

```xml
<!-- 声明创建工厂对象 -->
<bean id="factory" class="工厂类"/>

<bean id="tom" factory-bean="工厂对象id" factory-method="工厂方法"></bean>
```


## 2. Spring 装配简单类型属性 

装配，即为 Bean 的属性进行『**初始化**』、『**赋值**』。

『**简单类型**』是指：基本数据类型、基本数据类型包装类和字符串。

装配方式有两种：

- 通过『**构造方法**』装配

- 通过『**setter**』装配


### 构造方法装配

通过构造方法装配，即通过设置，要求 Spring 通过调用『**有参构造方法**』来创建对象。<small>默认是调用无参构造方法</small>。

在 **\<bean\>** 元素内使用 **constructor-arg** 子元素，即可触发构造方法装配。

```xml
<bean id="..." class="...">
    <constructor-arg index="x" value="xx"/>
    ...
</bean>
```

**index** 属性表示构造函数形参『**索引**』<small>（从 0 开始）</small>。如果参数的类型具有唯一性，<small>即参数的类型互不相同</small>，那么可以使用 **type** 属性，通过『**参数类型**』来指定构造方法和参数值。

为了简化配置，Spring 提供了一个名为 **c** 的 schema，来简化配置。

```xml
<beans xmlns="..."
       xmlns:xsi="..."
       xmlns:c="http://www.springframework.org/schema/c"
       xsi:schemaLocation="...">

    <bean id="..." class="..." c:_0="xxx" c:_1="xxx" ... />
</beans>
```

注意，这里出现的下划线 `_`，它是必须的，不要省略掉了。

使用这种简写方式，完全不用出现 **costructor-arg** 子元素，只需在 **bean** 元素中多增加几个『**c:_索引="参数值"**』这样的属性。


### setter 装配

通过 setter 装配，即设置 Spring 在<small>（通过『**无参**』构造器）</small>创建对象后，通过调用对象的属性的 setter 方法来为对象的属性赋值。在 bean 元素内使用 **property** 子元素，即可触发 setter 装配。

```xml
<bean id="..." class="...">
  <property name="xxx" value="xxx" />
  ...
</bean>
```

**property** 元素的 **name** 属性用于指定对象的属性名，**value** 属性用于指定要设置值。

为了简化配置，Spring 提供了一个名为 **p** 的 schema，来简化配置。

```xml
<beans xmlns="..."
       xmlns:xsi="..."
       xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="...">

    <bean id="tom" class="com.example.bean.Human" p:name="tom" p:age="20" />

    ...
</beans>
```

使用这种简写方式，完全不用出现 **property** 子元素，只需在 **bean** 元素中多增加几个『**p:属性名="属性值"**』这样的属性。


## 3. Spring 装配引用类型属性 

对象的属性不一定都是简单类型，还有可能有『**引用类型**』，即对象间有 **has-a** 关系。为引用类型的属性赋值不同于基本类型的属性，不是使用 *value*，而是 **ref** 。


### 构造方法装配

```xml
<bean id="jerry" class="com.example.bean.Cat" p:name="jerry" p:age="2"/>

<bean id="tom" class="com.example.bean.Human">
    <constructor-arg index="0" value="tom"/>
    <constructor-arg index="1" value="20"/>
    <constructor-arg index="2" ref="jerry"/>
</bean>
```


构造方法装配的简写形式中，对于引用类型必须写成：**c:_索引-ref="参数值"** 。

```xml
<bean id="jerry" class="com.example.bean.BlackCat" c:_0="jerry" c:_1="2"/>
<bean id="tom" class="com.example.bean.Human" c:_0="tom" c:_1="20" c:_2-ref="jerry"/>
```


### setter 装配

```xml
<bean id="jerry" class="com.example.bean.Cat" p:name="jerry" p:age="2"/>

<bean id="tom" class="com.example.bean.Human">
    <property name="name" value="tom"/>
    <property name="age" value="20"/>
    <property name="cat" ref="jerry"/>
</bean>
```

属性赋值的简写形式中，对于引用类型必须写成：**p:属性名-ref** 。

```xml
<bean id="jerry" class="com.example.bean.Cat" p:name="jerry" p:age="2"/>
<bean id="tom" class="com.example.bean.Human" p:name="tom" p:age="20" p:cat-ref="jerry"/>
```

## 4. Spring 装配集合类型属性 

更复杂的属性类型是集合类型属性：数组、List、Set、Map 。

Bean 的属性可能远不止基本类型这么简单，还有可能是基本类型的集合<small>（List、Set 和 Map）</small>。这种情况下，属性的赋值不再是 **property - value**  这种结构，而是 **property - list - value** 三层结构。

```xml
<bean id="..." class="...">

    <property name="数组属性名">
        <array>
            <value>xxx</value>
            ...
        </array>
    </property>

    <property name="List属性名">
        <list>
            <value>xxx</value>
            ...
        </list>
    </property>

    <property name="Set属性名">
        <set>
            <value>xxx</value>
            ...
        </set>
    </property>

    <property name="Map属性名">
        <map>
            <entry key="xxx" value="xx"/>
            ...
        </map>
    </property>

</bean>
```

如果集合是引用类型的集合，那么使用的子元素就从 *value* 改为 **ref**。map 使用的是 **key-ref** 和 **value-ref** 。

```xml
<list>
    <ref bean="myDataSource" />
    <ref bean="..." />
    <ref bean="..." />
</list>

<set>
    <ref bean="myDataSource" />
    <ref bean="..." />
    <ref bean="..." />
</set>

<map>
    <entry key ="a ref" value-ref="myDataSource"/>
    <entry key ="..." value-ref="..."/>
    <entry key ="..." value-ref="..."/>
</map>
```


## 5. 自动装配 

> 尽管自动装配很强大，但是代价是降低了 Bean 配置的可读性。在实践中，建议仅在依赖关系不复杂的应用中使用。

当一个 Bean 需要访问另一个 Bean 时，你可以显示指定引用装配它。不过，Spring IoC 容器提供自动装配功能，只需要在 **bean** 的 **autowire** 属性中指定自动装配模式就可以了。

| 装配模式          | 说明 |
| :-                | :- |
| **no**          | 默认值。不执行自动装配。你必须显示地装配所依赖对象  |
| **byName**      | 以 Bean 的属性名为依据，装配一个与属性名同名的 Bean |
| **byType**      | 以 Bean 的属性类型为依据，装配一个与之同类型的 Bean |
| **constructor** | 通过构造方法初始化 Bean 的属性，并依据参数的类型，装配一个与参数同类型的 Bean |



## 6. 注解替代 XML 配置 


在 XML 配置文件中加上 **\<context:component-scan base-package="Bean 所在的包路径"/\>** 即可开启 Spring 的『**自动扫描**』功能，这是使用注解替代 XML 配置的前提。



## 7. Bean 的作用域 


默认情况下，Spring IoC 容器只会对一个 Bean 创建一个实例。即单例。Spring IoC 提供了 4 种『**作用域**』，它决定了 Spring IoC 是否闯将一个新的对象、何时创建一个新的对象。

常见有：

- singleton（单例）：默认值。在整个应用中，Spring 只为其生成一个 Bean 的实例。

- prototype（原型）：Spring 每次都会生成一个 Bean 的实例。


在 XML 配置文件中， 通过 bean 元素的 **scope** 属性进行设置。该属性取值：`singleton` | `prototype` | 其他 。




## 8. Spring IoC 配置数据库连接池 

> [!warning]
> 由于 *.xml* 配置文件中不能出现 `&` 符号，因此 **url** 中需要的 `&` 符号，要使用它的 `&amp;` 编码形式来代替。

```xml
<!-- Druid 数据库连接池-->
<bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource" init-method="init" destroy-method="close">
  <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
  <property name="url" 
            value="jdbc:mysql://localhost:3306/scott?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false" />
  <property name="username" value="root"/>
  <property name="password" value="123456"/>
</bean>

<!-- HikariCP 数据库连接池 -->
<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
  <property name="driverClassName" value="com.mysql.cj.jdbc.Driver" />
  <property name="jdbcUrl" 
            value="jdbc:mysql://localhost:3306/scott?useUnicode=true&amp;characterEncoding=utf8&amp;useSSL=false&amp;serverTimezone=Asia/Shanghai" /> <!-- 这里的 name 和 Druid、dbcp2 的 name 不一样 -->
  <property name="username" value="root" />
  <property name="password" value="123456" />
</bean>
```

> 这里有一个小细节，Druid 数据库连接池和 HikariCP 数据库连接池的数据库 URL 属性名并不一样，一个叫 **url** ，一个叫 **jdbcUrl** 。

## 9. property-placeholder 

```xml
<context:property-placeholder location="classpath:jdbc.properties"/>
```

有时有些配置我们并不是直接『写死』在 *.xml* 配置文件中，而是写在 **.properties** 配置文件中，再让 Spring 从 **.properties** 配置文件中读取配置，进行利用。

Spring 通过 **\<context:property-placeholder\>** 元素提供了这个功能。

默认情况下，整个程序中无论有多少个 *.xml* Spring 配置文件，但是只能有一个 **\<context:property-placeholder ... /\>**，此时需要开启它的 **ignore-unresolvable** 告知 Spring 当前的 \<context:property-placeholder ... /\> 并非是唯一的一个。

例如：

```xml
<context:property-placeholder location="classpath:jdbc.properties" ignore-unresolvable="true" />

<bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
    <property name="driverClassName" value="${jdbc.driver-class-name}"/>
    <property name="jdbcUrl" value="${jdbc.url}"/>
    <property name="username" value="${jdbc.username}"/>
    <property name="password" value="${jdbc.password}"/>
</bean>
```

这里有个小细节，*jdbc.properties* 中的属性的 *key* 不要和 DataSource 的 driverClassName、jdbcUrl、username、password 属性同名，否则会有问题：数据库连接池初始化不成功。

配置文件中的配置项按惯例都会有自定义的前缀，用以表明这个<small>（这些）</small>配置用于什么环境。


