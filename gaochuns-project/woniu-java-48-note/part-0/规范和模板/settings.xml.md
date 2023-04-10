```xml﻿
<?xml version="1.0" encoding="UTF-8"?>

<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
    http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>releases</id>
            <username>woniu</username>
            <password>123456</password>
        </server>
        <server>
            <id>snapshots</id>
            <username>woniu</username>
            <password>123456</password>
        </server>
    </servers>
    <mirrors>
        <mirror>
          <id>local-repository</id>
          <name>内网私服</name> 
          <mirrorOf>central</mirrorOf> <!-- mirrorOf* 有“坑”，慎用 -->
          <url>http://192.172.0.8:8081/repository/maven-public</url>
        </mirror>
    </mirrors>

    <profiles>
        <profile>
            <id>jdk-1.8</id>    
            <activation>    
                <activeByDefault>true</activeByDefault>    
                <jdk>1.8</jdk> <!-- 当 jdk 环境版本为 1.8 时，此 profile 被激活 -->
            </activation>    
            <properties>
                <maven.compiler.source>1.8</maven.compiler.source>    
                <maven.compiler.target>1.8</maven.compiler.target>    
                <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
            </properties>
        </profile>
        <profile>
            <id>阿里云</id>
			<repositories>
				<repository>
					<id>neiwang</id>
					<name>内网私服</name>
                    <url>http://192.172.0.8:8081/repository/maven-public</url>
				</repository>
				<repository>
					<id>aliyun 镜像仓库</id>
					<name>Repository for aliyun</name>
					<url>https://maven.aliyun.com/repository/public</url>
				</repository>
				<repository>
					<id>centralRep</id>
					<name>maven 中央仓库</name>
					<url>https://repo.maven.apache.org/maven2</url>
				</repository>
			</repositories>
        </profile>
    </profiles>
    <activeProfiles>
		<activeProfile>阿里云</activeProfile>
	</activeProfiles>
<!--
几个注意事项：
1.同一个profile中，先搜索先定义的仓库。
2.多个profile时，先搜索后定义的profile中的仓库。
3.镜像会实现对被镜像仓库的完全替代，万一镜像了中央仓库，但是镜像里又没有你想要的组件，就挂了，所以镜像一定要慎重配置。
-->
</settings>
```
