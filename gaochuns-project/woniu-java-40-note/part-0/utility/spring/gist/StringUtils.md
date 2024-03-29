---
alias: Spring StringUtils 工具类
tags: spring, 工具类, 字符串, utility
---


```java
import static org.springframework.util.StringUtils.*;
```


-tx-
|# |方法 | 说明 
| :--------------: | -------------------------------: | :---------------------------------------------------- |
| 对字符串的判断   |                  isEmpty         | 字符串是否为 empty                                    |
| ^^               |                  hashLength      | 字符串是否为 empty                                    |
| ^^               |                  hashText        | 字符串是否为 blank                                    |
| ^^               |                  substringMatch  | 字符串中是否含有指定子串                              |
| 一               | 一                               | 一                                                    |
| 字符串的拼接     |           arrayToDelimitedString | 以参数 delim 作为分隔符，拼接字符串                   |
| ^^               |      collectionToDelimitedString | 以参数 delim 作为分隔符，拼接字符串                   |
| ^^               |      arrayToCommaDelimitedString | 以逗号 "," 作为分隔符，拼接字符串                     |
| ^^               | collectionToCommaDelimitedString | 以逗号 "," 作为分隔符，拼接字符串                     |
| 一               | 一                               | 一                                                    |
| 字符串的切分     |       delimitedListToStringArray | 以指定分隔符将字符串拆成数组                          |
| ^^               |  commaDelimitedListToStringArray | 以逗号","作为分隔符将字符串拆成数组                   |
| ^^               |          commaDelimitedListToSet | 以逗号","作为分隔符将字符串拆成 Set                   |
| ^^               |                            split | 按指定参数切分，且最多只切成 2 份。即数组长度最大为 2 |
| ^^               |            tokenizeToStringArray | 指定多个分隔符对字符串进行拆分                        |
| 一               | 一                               | 一                                                    |
| 删除空白符       | trimWhitespace                   | 去头尾空格                                            |
| ^^               | trimAllWhitespace                | 去头尾+中间空格                                       |
| ^^               | trimLeadingWhitespace            | 去头部空白符                                          |
| ^^               | trimTrailingWhitespace           | 去尾部空白符                                          |
| 删除指定字符     | trimLeadingCharacter             | 去头部指定字符                                        |
| ^^               | trimTrailingCharacter            | 去尾部指定字符                                        |
| ^^               | deleteAny                        | 去除所有位置的指定字符。这里删除的是字符，而不是子串。|
| 一               | 一                               | 一                                                    |
| 摘除与替换子串   |                           delete | 方法用于从长串中删除指定子串。                        |
| ^^               |                          replace | 方法用于替换长传中的指定子串。                        |
| 一               | 一                               | 一                                                    |
| 文件路径名和后缀 |                        cleanPath | 获得「紧凑」的路径字符串，会优化掉路径字符串中的 ".." |
| ^^               |                      getFilename | 获得文件名，即去掉文件的路径。返回的文件名中会含有后缀|
| ^^               |            getFilenameExtendsion | 获得文件后缀                                          |
| ^^               |           stripFilenameExtension | 和上个方法"互补"，返回一个无后缀的路径字符串          |
| ^^               |                        unqualify | 类似 getfilenameExtendsion，可获得最后一个 . 之后的内容。|
| ^^ | ^^ | 有个重载方法能指定分隔符 | 
| 一               | 一                               | 一                                                    |
| 操作字符串数组   |                    toStringArray | 字符串集合转成字符串数组                              |
| ^^               |                  sortStringArray | 集合数组转字符串数组                                  |
| ^^               |          concatenateStringArrays | 两个字符串数组二合一，返回新数组                      |
| ^^               |                mergeStringArrays | 同上，有去重功能。但标识为"被废弃"                    |
| ^^               |           removeDuplicateStrings | 字符串数组去重                                        |
| ^^               |                trimArrayElements | 对数组的每一项执行 trim 方法                          |
| ^^               |                 addStringToArray | 字符串数组后追加内容，返回新数组                      |
| 一               | 一                               | 一                                                    |
| 其它方法         |               countOccurrencesOf | 方法用于统计 xxx 在字符串中出现次数。index 是 0-based 整数。|          
| ^^               | splitArrayElementsIntoProperties | 字符串数组转 properties 。                            |
| ^^               | ^^                               | 字符串数组中的元素的内容格式类似于 `"key1,value1"`       |                        
| ^^               |              parseTimeZoneString | 字符串转 TimeZone                                     |


### 附：TimeZone String 


| TimeZone                        | 地点 |
| ------------------------------: | :------------------ |
| "Asia/Shanghai"                 | 中国标准时间 (北京)|
| "Asia/Hong_Kong"                | 香港时间 (香港)|
| "Asia/Taipei"                   | 台北时间 (台北)|
| "Asia/Seoul"                    | 首尔|
| "Asia/Tokyo"                    | 日本时间 (东京)|
| "America/New_York"              | 美国东部时间 (纽约)|
| "America/Denver"                | 美国山区时间 (丹佛)|
| "America/Costa_Rica"            | 美国中部时间 (哥斯达黎加)|
| "America/Chicago"               | 美国中部时间 (芝加哥)|
| "America/Mexico_City"           | 美国中部时间 (墨西哥城)|
| "America/Regina"                | 美国中部时间 (里贾纳)|
| "America/Los_Angeles"           | 美国太平洋时间 (洛杉矶)|
| "Pacific/Majuro"                | 马朱罗|
| "Pacific/Midway"                | 中途岛|
| "Pacific/Honolulu"              | 檀香山|
| "America/Anchorage"             | 安克雷奇|
| "America/Tijuana"               | 美国太平洋时间 (提华纳)|
| "America/Phoenix"               | 美国山区时间 (凤凰城)|
| "America/Chihuahua"             | 奇瓦瓦|
| "America/Bogota"                | 哥伦比亚时间 (波哥大)|
| "America/Caracas"               | 委内瑞拉时间 (加拉加斯)|
| "America/Barbados"              | 大西洋时间 (巴巴多斯)|
| "America/Manaus"                | 亚马逊标准时间 (马瑙斯)|
| "America/St_Johns"              | 纽芬兰时间 (圣约翰)|
| "America/Santiago"              | 圣地亚哥|
| "America/Argentina/Buenos_Aires"| 布宜诺斯艾利斯|
| "America/Godthab"               | 戈特霍布|
| "America/Montevideo"            | 乌拉圭时间 (蒙得维的亚)     |
| "America/Sao_Paulo"             | 圣保罗                      |
| "Atlantic/South_Georgia"        | 南乔治亚                    |
| "Atlantic/Azores"               | 亚述尔群岛                  |
| "Atlantic/Cape_Verde"           | 佛得角                      |
| "Africa/Casablanca"             | 卡萨布兰卡                  |
| "Europe/London"                 | 格林尼治标准时间 (伦敦)     |
| "Europe/Amsterdam"              | 中欧标准时间 (阿姆斯特丹)   |
| "Europe/Belgrade"               | 中欧标准时间 (贝尔格莱德)   |
| "Europe/Brussels"               | 中欧标准时间 (布鲁塞尔)     |
| "Europe/Sarajevo"               | 中欧标准时间 (萨拉热窝)     |
| "Africa/Brazzaville"            | 西部非洲标准时间 (布拉扎维) |
| "Africa/Windhoek"               | 温得和克|
| "Asia/Amman"                    | 东欧标准时间 (安曼)|
| "Europe/Athens"                 | 东欧标准时间 (雅典)|
| "Asia/Beirut"                   | 东欧标准时间 (贝鲁特)|
| "Africa/Cairo"                  | 东欧标准时间 (开罗)|
| "Europe/Helsinki"               | 东欧标准时间 (赫尔辛基)|
| "Asia/Jerusalem"                | 以色列时间 (耶路撒冷)|
| "Africa/Harare"                 | 中部非洲标准时间 (哈拉雷)|
| "Europe/Minsk"                  | 明斯克|
| "Asia/Baghdad"                  | 巴格达|
| "Europe/Moscow"                 | 莫斯科|
| "Asia/Kuwait"                   | 科威特|
| "Africa/Nairobi"                | 东部非洲标准时间 (内罗毕)|
| "Asia/Tehran"                   | 伊朗标准时间 (德黑兰)|
| "Asia/Baku"                     | 巴库|
| "Asia/Tbilisi"                  | 第比利斯|
| "Asia/Yerevan"                  | 埃里温|
| "Asia/Dubai"                    | 迪拜|
| "Asia/Kabul"                    | 阿富汗时间 (喀布尔)|
| "Asia/Karachi"                  | 卡拉奇|
| "Asia/Oral"                     | 乌拉尔|
| "Asia/Yekaterinburg"            | 叶卡捷林堡|
| "Asia/Calcutta"                 | 加尔各答|
| "Asia/Colombo"                  | 科伦坡|
| "Asia/Katmandu"                 | 尼泊尔时间 (加德满都)|
| "Asia/Almaty"                   | 阿拉木图|
| "Asia/Rangoon"                  | 缅甸时间 (仰光)|
| "Asia/Krasnoyarsk"              | 克拉斯诺亚尔斯克|
| "Asia/Bangkok"                  | 曼谷|
| "Asia/Irkutsk"                  | 伊尔库茨克时间 (伊尔库茨克) |
| "Asia/Kuala_Lumpur"             | 吉隆坡 |
| "Australia/Perth"               | 佩思 |
| "Asia/Yakutsk"                  | 雅库茨克时间 (雅库茨克) |
| "Australia/Darwin"              | 达尔文 |
| "Australia/Brisbane"            | 布里斯班 |
| "Asia/Vladivostok"              | 海参崴时间 (符拉迪沃斯托克) |
| "Pacific/Guam"                  | 关岛 |
| "Australia/Adelaide"            | 阿德莱德 |
| "Australia/Hobart"              | 霍巴特 |
| "Australia/Sydney"              | 悉尼 |
| "Asia/Magadan"                  | 马加丹时间 (马加丹) |
| "Pacific/Auckland"              | 奥克兰 |
| "Pacific/Fiji"                  | 斐济 |
| "Pacific/Tongatapu"             | 东加塔布 |


