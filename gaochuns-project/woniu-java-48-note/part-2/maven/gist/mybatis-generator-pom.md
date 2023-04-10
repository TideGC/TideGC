# pom.xml

## dependencies 和 build

!FILENAME dependencies 和 build 部分
```xml

<build>
    <plugins>
        <!-- mybatis.generator begin -->
        <plugin>
            <groupId>org.mybatis.generator</groupId>
            <artifactId>mybatis-generator-maven-plugin</artifactId>
            <version>1.4.0</version> <!-- 不要低于 1.3.7 版本 -->
            <dependencies>
                <dependency>
                    <groupId>mysql</groupId>
                    <artifactId>mysql-connector-java</artifactId>
                    <version>5.1.39</version>
                </dependency>
                <dependency>
                    <groupId>org.mybatis.generator</groupId>
                    <artifactId>mybatis-generator-core</artifactId>
                    <version>1.4.0</version> <!-- 不要低于 1.3.7 版本 -->
                </dependency>
            </dependencies>
            <executions>
                <execution>
                    <id>Generate MyBatis Artifacts</id>
                    <phase>package</phase>
                    <goals>
                        <goal>generate</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <verbose>true</verbose> <!--允许移动生成的文件 -->
                <overwrite>true</overwrite> <!-- 是否覆盖 -->
                <!-- 自动生成的配置 -->
                <configurationFile>
                    src/main/resources/mybatis/mybatis-generator-config.xml
                </configurationFile>
            </configuration>
        </plugin>
        <!-- mybatis.generator end-->
    </plugins>

</build>
```

## mybatis-generator-config.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE generatorConfiguration
        PUBLIC "-//mybatis.org//DTD MyBatis Generator Configuration 1.0//EN"
        "http://mybatis.org/dtd/mybatis-generator-config_1_0.dtd">
<generatorConfiguration>

    <!-- 数据库驱动包位置 -->
    <!-- 由于在 pom.xml 中加入插件时已经配置数据库驱动包，所以此处不必配置了 -->
    <!--     
        <classPathEntry location="C:\Users\59960549\.m2\repository\mysql\mysql-connector-java\5.1.47\mysql-connector-java-5.1.47.jar" />
    -->

    <context id="scott" targetRuntime="MyBatis3">

        <!-- 解决覆盖生成 XML 文件的 Bug -->
        <plugin type="org.mybatis.generator.plugins.UnmergeableXmlMappersPlugin" />

        <commentGenerator>
            <!-- 去掉注解中的生成日期 -->
            <property name="suppressDate" value="true"/>
            <!--关闭注释 -->
            <property name="suppressAllComments" value="true"/>
        </commentGenerator>

        <!--数据库链接地址账号密码-->
        <jdbcConnection driverClass="com.mysql.jdbc.Driver"
                        connectionURL="jdbc:mysql://localhost:3306/scott?useSSL=false"
                        userId="root"
                        password="123456">
        </jdbcConnection>

        <javaTypeResolver>
            <!--
            true： 无论什么情况，都是使用 BigDecimal 对应 DECIMAL 和 NUMERIC 数据类型
            false： 默认值,
                scale>0;length>18：使用 BigDecimal;
                scale=0;length[10,18]：使用 Long；
                scale=0;length[5,9]：使用 Integer；
                scale=0;length<5：使用 Short；
            -->
            <property name="forceBigDecimals" value="true"/>
        </javaTypeResolver>

        <!-- PO 类的相关设置 -->
        <javaModelGenerator targetProject="src/main/java"
                            targetPackage="com.demo.pojo">
            <!-- 在 targetPackage 的基础上，根据数据库的 schema 再生成一层 package，最终生成的类放在这个 package下，默认为false  -->
            <property name="enableSubPackages" value="false"/>
            <property name="trimStrings" value="true"/>
        </javaModelGenerator>

        <!-- Mapper.xml 配置文件的相关设置 -->
        <sqlMapGenerator targetProject="src/main/resources"
                         targetPackage="mapper">
            <property name="enableSubPackages" value="false"/>
        </sqlMapGenerator>

        <!-- DAO 接口的相关设置-->
        <javaClientGenerator targetProject="src/main/java"
                             targetPackage="com.demo.dao"
                             type="XMLMAPPER">
            <property name="enableSubPackages" value="false"/>
        </javaClientGenerator>

        <!--生成对应表及类名-->
        <table tableName="department" 
               domainObjectName="Department"
               enableCountByExample="false"
               enableUpdateByExample="false"
               enableDeleteByExample="false"
               enableSelectByExample="false"
        />
    </context>
</generatorConfiguration>
```