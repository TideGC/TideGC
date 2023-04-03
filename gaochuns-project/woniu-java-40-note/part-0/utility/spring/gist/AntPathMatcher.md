---
alias: AntPathMatcher
tags: spring utility 工具类 
---

## AntPathMatcher

AntPathMatcher 是 Spring 自带的路径匹配规则工具类，可以根据 ANT 风格路径规则来判断给定的字符串是否匹配。

### ANT 匹配规则


1.  \? 匹配 1 个字符<small>（ 除过操作系统默认的文件分隔符 ）</small>
2.  \* 匹配 0 个或多个字符
3.  \*\* 匹配 0 个或多个目录

### 规则示例

| Pattern | 匹配说明 |
| :- | :- |
| com/t?st.jsp | 匹配：com/test.jsp、com/tast.jsp、com.txst.jsp |
| com/\*.jsp  | 匹配：com 文件夹下的全部 .jsp 文件 |
| com/\*\*/tset.jsp | 匹配：com 文件夹和子文件夹下的全部 .jsp 文件 |
| org/springframework/\*/.jsp | 匹配：org/springframework 文件夹及其直接子文件夹下的全部 .jsp 文件 |
| org/\*\*/servlet/bal.jsp | 匹配：|


### 测试示例

```java
public class AntPathMatcherTest {

    public static void main(String[] args) {
        PathMatcher pathMatcher = new AntPathMatcher();
        System.out.println(pathMatcher.match("/*/retail/**", "/cir-node/retail/save"));     //true
        System.out.println(pathMatcher.match("*test*", "AnothertestTest"));     //true
        System.out.println(pathMatcher.match("/????", "/bala/bla"));            //false
    }
}
```