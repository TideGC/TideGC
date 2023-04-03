---
alias: FileUtils
tags: FileUtils, commons-io
---


## FileUtils 类

``` java
// 递归地删除文件/文件夹
void deleteDirectory(File directory)
```

``` java
// 将文件中的内容读入字符串
String readFileToString(File file, Charset encoding)
String readFileToString(File file, String encoding)
```

``` java
// 删除一个文件。无论如何不会抛出异常。
boolean deleteQuietly(File file)
```

``` java
void copyFile(File srcFile, File destFile) // 拷贝文件，保留源文件的日期
void copyFile(File srcFile, File destFile, boolean preserveFileDate) // 是否保留文件日期取决于第三个参数
long copyFile(File input, OutputStream output)
```

``` java
void writeStringToFile(File file, String data, Charset encoding) // 向文件中写入字符串。若文件不存在，则创建文件。
void writeStringToFile(File file, String data, Charset encoding, boolean append)
void writeStringToFile(File file, String data, String encoding)
void writeStringToFile(File file, String data, String encoding, boolean append)
```

``` java
void forceMkdir(File directory) // 创建文件夹
void forceMkdirParent(File file) // 创建文件夹，及其父文件夹。
```

``` java
void write(File file, CharSequence data, Charset encoding)
void write(File file, CharSequence data, String encoding)
void write(File file, CharSequence data, Charset encoding, boolean append)
void write(File file, CharSequence data, String encoding, boolean append)
```

``` java
Collection<File> listFiles(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
// Finds files within a given directory (and optionally its subdirectories).
Collection<File> listFiles(File directory, String[] extensions, boolean recursive)
// Finds files within a given directory (and optionally its subdirectories) which match an array of extensions.
Collection<File> listFilesAndDirs(File directory, IOFileFilter fileFilter, IOFileFilter dirFilter)
// Finds files within a given directory (and optionally its subdirectories).
```

``` java
void copyDirectory(File srcDir, File destDir) // Copies a whole directory to a new location preserving the file dates.
void copyDirectory(File srcDir, File destDir, boolean preserveFileDate) // Copies a whole directory to a new location.
void copyDirectory(File srcDir, File destDir, FileFilter filter) // Copies a filtered directory to a new location preserving the file dates.
void copyDirectory(File srcDir, File destDir, FileFilter filter, boolean preserveFileDate) // Copies a filtered directory to a new location.
```

``` java
void forceDelete(File file) // Deletes a file.
```

