
---
alias: 
- IOUtils 类
tags: 
- IOUtils
- commons-io
---

## IOUtils 类

IOUtils 主要操作 IO 流进行文件的读写操作。

IOUtils 常用方法如下：

``` java
//「安静地」关闭流对象。2.6 被废弃，建议使用 try-with-resource
void closeQuietly(Closeable closeable)

// 从输入流中读取数据
String toString(InputStream input, Charset encoding)
String toString(InputStream input, String encoding)
```

``` java
// 从输入流 拷贝至 输出流
int copy(InputStream input, OutputStream output)
void copy(InputStream input, Writer output, Charset inputEncoding)
void copy(Reader input, OutputStream output, String outputEncoding)
void copy(Reader input, OutputStream output, Charset outputEncoding)
```

``` java
// 从 输入流 中读取字节
byte[] toByteArray(InputStream input)
byte[] toByteArray(InputStream input, int size)
byte[] toByteArray(InputStream input, long size)
// 2.5 中被废弃，使用下个方法替代
byte[] toByteArray(Reader input)
byte[] toByteArray(Reader input, Charset encoding)
byte[] toByteArray(Reader input, String encoding)
```

``` java
// write()
void write(byte[] data, OutputStream output)
void write(char[] data, OutputStream output, Charset encoding)
void write(char[] data, OutputStream output, String encoding)

void write(char[] data, Writer output)
void write(byte[] data, Writer output, Charset encoding)
void write(byte[] data, Writer output, String encoding)

void write(CharSequence data, Writer output)
void write(CharSequence data, OutputStream output, Charset encoding)
void write(CharSequence data, OutputStream output, String encoding)

void write(String data, Writer output)
void write(String data, OutputStream output, Charset encoding)
void write(String data, OutputStream output, String encoding)
```


``` java
// 生成 InputStream 对象，并写入参数内容
InputStream toInputStream(CharSequence input, Charset encoding)  
InputStream toInputStream(CharSequence input, String encoding)

InputStream toInputStream(String input, Charset encoding)
InputStream toInputStream(String input, String encoding)
```

``` java
List<String> readLines(Reader input)   // 按行读入内容。
List<String> readLines(InputStream input, Charset encoding)
List<String> readLines(InputStream input, String encoding)
```

``` java
long copyLarge(InputStream input, OutputStream output) // 用于超过 2 GB 的数据拷贝
long copyLarge(InputStream input, OutputStream output, byte[] buffer)
long copyLarge(InputStream input, OutputStream output, long inputOffset, long length)
long copyLarge(InputStream input, OutputStream output, long inputOffset, long length, byte[] buffer)

long copyLarge(Reader input, Writer output)
long copyLarge(Reader input, Writer output, char[] buffer)
long copyLarge(Reader input, Writer output, long inputOffset, long length)
long copyLarge(Reader input, Writer output, long inputOffset, long length, char[] buffer)
```

``` java
LineIterator lineIterator(Reader reader) // 生成 Iterator 迭代器。可逐行读取文件内容。
LineIterator lineIterator(InputStream input, String encoding)
LineIterator lineIterator(InputStream input, Charset encoding)
```

``` java
void readFully(InputStream input, byte[] buffer) // 读取指定数量的内容，或失败。
void readFully(InputStream input, byte[] buffer, int offset, int length)

void readFully(Reader input, char[] buffer)
void readFully(Reader input, char[] buffer, int offset, int length)

byte[] readFully(InputStream input, int length)
```
