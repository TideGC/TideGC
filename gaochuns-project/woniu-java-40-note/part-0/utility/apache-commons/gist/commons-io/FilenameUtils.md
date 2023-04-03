---
alias: FilenameUtils 类
tags: FilenameUtils, commons-io
---

## FilenameUtils 类

``` java
/* Gets the extension of a filename. */
String getExtension(String filename) 


/* Gets the base name, minus the full path and extension, from a full filename. */
String getBaseName(String filename) 


/* Gets the name minus the path from a full filename. */
String getName(String filename) 


/* Concatenates a filename to a base path using normal command line style rules. */
String concat(String basePath, String fullFilenameToAdd) 


/* Removes the extension from a filename. */
String removeExtension(String filename) 


/* Normalizes a path, removing double and single dot path steps. */
String normalize(String filename) 


/* Checks a filename to see if it matches the specified wildcard matcher, always testing case-sensitive. */
boolean wildcardMatch(String filename, String wildcardMatcher) 


/* Converts all separators to the Unix separator of forward slash. */
String separatorsToUnix(String path) 


/* Gets the full path from a full filename, which is the prefix + path. */
String getFullPath(String filename) 


/* Checks whether the extension of the filename is that specified. */
boolean isExtension(String filename, String extension) 
```

