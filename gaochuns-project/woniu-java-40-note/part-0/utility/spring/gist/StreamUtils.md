---
alias: StreamUtils
tags: spring utility 工具类 
---

## StreamUtils

**StreamUtils** 类位于 org.springframework.util 包下 。

### for 输出

```java
void copy(byte[] in, OutputStream out)

int copy(InputStream in, OutputStream out)

void copy(String in, Charset charset, OutputStream out)

long copyRange(InputStream in, OutputStream out, long start, long end)
```

### for 输入

```java
byte[] copyToByteArray(InputStream in)

String copyToString(InputStream in, Charset charset)

// 舍弃输入流中的内容
int drain(InputStream in) 
```

### for 实例化

```java
InputStream emptyInput()

// 所生成的 输入流 close() 方法无效
InputStream nonClosing(InputStream in) 

// 所生成的 输出流 close() 方法无效
OutputStream nonClosing(OutputStream out) 
```
