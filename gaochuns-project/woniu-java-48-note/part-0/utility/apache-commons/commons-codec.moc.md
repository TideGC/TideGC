---
alias: 
- "apache commons-codec"
tags: 
- apache
- commons-codec
- 工具类 
---

apache commons-codec 工具库的主要目的是封装常见的加密和编码算法，屏蔽掉复杂的、专业的实现过程，进而为程序员提供简单方便的 API 。

``` xml
<dependency>
    <groupId>commons-codec</groupId>
    <artifactId>commons-codec</artifactId>
    <version>1.15</version>
</dependency>
```

commons-codec 的情况与 commons-io 的情况类似，一提到 java 领域的编解码库，第一选择就是它，几乎想不到它有什么竞品与之竞争。


## 基本概念

最常用工具类是 ***DigestUtils*** 类：*org.apache.commons.codec.digest.DigestUtils* 类

|方法     |说明|
|--------:| :- |
|md5Hex   | MD5 加密，返回 32 位 |
|sha1Hex  | SHA-1 加密 |
|sha256Hex| SHA-256 加密 |
|sha512Hex| SHA-512 加密 |
|md5      | MD5 加密，返回 16 位 |
  
## 加密算法

[[202212011450|《关于加密算法》]]
  
## sha512Hex 方法
  
以 SHA512 加密算法对数据源进行加密，返回加密后的十六进制形式字符串
  
``` java
String sha512Hex(byte[] data)
String sha512Hex(InputStream data)
String sha512Hex(String data)
```
  
## sha256Hex 方法
  
以 SHA256 加密算法对数据源进行加密，返回加密后的十六进制形式字符串
  
``` java
String sha256Hex(byte[] data)
String sha256Hex(InputStream data)
String sha256Hex(String data)
```
  
## sha1Hex 方法
  
以 SHA1 加密算法对数据源进行加密，返回加密后的十六进制形式字符串
  
``` java
String sha1Hex(byte[] data)
String sha1Hex(InputStream data)
String sha1Hex(String data)
```
  
## shaHex 方法
  
以 SHA1 加密算法对数据源进行加密，返回加密后的十六进制形式字符串
  
从 1.11 开始被标记为废弃，建议使用 sha1Hex 方法替代。
  
``` java
String shaHex(byte[] data)
String shaHex(InputStream data)
String shaHex(String data)
```
  
## md5Hex 方法
  
以 `MD5` 加密算法对数据源进行加密，返回加密后的十六进制形式字符串
  
``` java
String md5Hex(byte[] data)
String md5Hex(InputStream data)
String md5Hex(String data)
```
  
## 其它
  
``` java
//  同上，只不过返回的不是十六进制字符串，而是加密后的二进制的字节数据
md5()
sha()
sha1()
sha256()
sha512()
```

