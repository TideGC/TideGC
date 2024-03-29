# pom.xml

为避免包版本冲突，以下版本信息大多来源自 **spring-boot-dependencies** 。

## properties

```xml
<properties>
  <!-- version just like spring-boot-starter 2.1.6.RELEASE -->
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <java.version>1.8</java.version>
  <maven.compiler.source>1.8</maven.compiler.source>
  <maven.compiler.target>1.8</maven.compiler.target>
  <lombok.version>1.18.12</lombok.version>
  <logback.version>1.2.3</logback.version>
  <commons-lang3.version>3.8.1</commons-lang3.version>
  <guava.version>28.2-jre</guava.version> <!-- 单独指定 -->
  <spring.version>5.1.17.RELEASE</spring.version>
  <aspectj.version>1.9.6</aspectj.version>
 
  <servlet.version>3.1.0</servlet.version>
  <jsp.version>2.2</jsp.version>
  <jstl.version>1.2</jstl.version>
  <jackson.version>2.9.10</jackson.version>
  <mysql.version>8.0.21</mysql.version>
  <!--<druid.version>1.1.22</druid.version>-->
  <hikaricp.version>3.2.0</hikaricp.version>
  <mybatis.version>3.5.1</mybatis.version>
  <mybatis-spring.version>2.0.1</mybatis-spring.version>
  <pagehelper.version>5.1.8</pagehelper.version> <!-- 单独指定 -->
  <junit.version>4.12</junit.version>
</properties>
```

## dependencies 和 build

!FILENAME dependencies 和 build 部分
```xml
<dependencies>

    <!-- Base -->

    <dependency> <!-- lombok 工具库 -->
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>${lombok.version}</version>
        <scope>provided</scope>
    </dependency>

    <dependency> <!-- logback 日志包 -->
        <groupId>ch.qos.logback</groupId>
        <artifactId>logback-classic</artifactId>
        <version>${logback.version}</version>
    </dependency>

    <dependency> <!-- core/beans/expression/aop 被依赖，自动导入 -->
        <groupId>org.springframework</groupId>
        <artifactId>spring-context</artifactId>
        <version>${spring.version}</version>
    </dependency>

    <dependency> <!-- spring aop 的 aspectj 功能 -->
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjrt</artifactId>          
        <version>${aspectj.version}</version>
    </dependency>

    <dependency> <!-- spring aop 的 aspectj 功能 -->
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjweaver</artifactId>      
        <version>${aspectj.version}</version>
    </dependency>

    <dependency> <!-- jackson: 处理 json 的库 -->
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>   
        <version>${jackson.version}</version>
    </dependency>

    <!-- for Web -->
    
    <dependency> <!-- spring mvc：spring-web 被依赖，自动导入 -->
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId> 
        <version>${spring.version}</version>
    </dependency>

    <dependency> <!-- servlet 接口声明 -->
        <groupId>javax.servlet</groupId>
        <artifactId>javax.servlet-api</artifactId>  
        <version>${servlet.version}</version>
        <scope>provided</scope>
    </dependency>

    <dependency> <!-- jsp 接口声明 -->
        <groupId>javax.servlet.jsp</groupId>
        <artifactId>jsp-api</artifactId>            
        <version>${jsp.version}</version>
        <scope>provided</scope>
    </dependency>

    <dependency> <!-- jsp 上使用 jstl 标签库所 -->
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>               
        <version>${jstl.version}</version>
    </dependency>

    <!-- for Dao -->
    <dependency> <!-- Spring 整合 Dao 需要 -->
        <groupId>org.springframework</groupId>
        <artifactId>spring-jdbc</artifactId>
        <version>${spring.version}</version>
    </dependency>

    <dependency> <!-- Hikaricp 数据库连接池 -->
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>${hikaricp.version}</version>
    </dependency>

    <!--druid 数据库连接池 
    <dependency> 
        <groupId>com.alibaba</groupId>
        <artifactId>druid</artifactId>
        <version>${druid.version}</version>
    </dependency>-->

    <dependency> <!-- mysql 数据库驱动 -->
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>   
        <version>${mysql.version}</version>
    </dependency>

    <dependency> <!-- spring 整合 mybatis -->
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis-spring</artifactId>     
        <version>${mybatis-spring.version}</version>
    </dependency>

    <dependency> <!-- mybatis -->
        <groupId>org.mybatis</groupId>
        <artifactId>mybatis</artifactId>            
        <version>${mybatis.version}</version>
    </dependency>

    <dependency> <!-- mybatis 分页插件 -->
        <groupId>com.github.pagehelper</groupId>
        <artifactId>pagehelper</artifactId>
        <version>${pagehelper.version}</version>
    </dependency>

    <!-- for Test -->
    <dependency> <!-- sprint test -->
        <groupId>org.springframework</groupId>
        <artifactId>spring-test</artifactId> 
        <version>${spring.version}</version>
        <scope>test</scope>
    </dependency>

    <dependency> <!-- junit -->
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>

</dependencies>

<build>
    <finalName>${project.artifactId}</finalName>
    <plugins>
        <plugin> <!-- tomcat 7 插件 -->
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat7-maven-plugin</artifactId>
            <version>2.2</version>
            <configuration>
                <port>8080</port>
                <path>/${project.artifactId}</path>
                <uriEncoding>UTF-8</uriEncoding>
            </configuration>
        </plugin>

<!-- tomcat 8 插件
        <plugin>
            <groupId>org.apache.tomcat.maven</groupId>
            <artifactId>tomcat8-maven-plugin</artifactId>
            <version>3.0-r1655215</version>
            <configuration>
                <port>8080</port>
                <path>/${project.artifactId}</path>
            </configuration>
         </plugin>
-->

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

<!-- tomcat 8 插件下载路径
    <pluginRepositories>
        <pluginRepository>
            <id>alfresco</id>
            <url>https://artifacts.alfresco.com/nexus/content/repositories/public/</url>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </pluginRepository>
    </pluginRepositories>
-->
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