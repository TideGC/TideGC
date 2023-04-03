package com.woniu;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.util.Collections;


public class MybatisPlusGenerator {
    @Test
    public void generator() {
        String rootDir = "D:/generator";
        String outputDir = rootDir + "/java";                  //生成的文件放置目录
        String packageName = "com.woniu";    //设置父包名
        String mapperXmlDir = rootDir + "/resources/" + packageName.replaceAll("\\.", "/") + "/mapper"; //xml文件放置目录

        String url = "jdbc:mysql://localhost:3306/k15?useUnicode=true&characterEncoding=utf8&useSSL=false&nullCatalogMeansCurrent=true&serverTimezone=Asia/Shanghai";

        FastAutoGenerator.create(url, "root", "123456")
                .globalConfig(builder -> {
                    builder.author("wdn") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(outputDir); // 指定输出目录
                }).packageConfig(builder -> {
                    builder.parent(packageName) // 设置父包名
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, mapperXmlDir));
                }).strategyConfig(builder -> {
                    builder.addInclude("k15_member_card", "k15_member_consume")  //给该表生成
                            .addTablePrefix("rbac_", "k15_") //设置过滤表前缀
                            .entityBuilder().enableLombok()//entity使用lombok
                            .entityBuilder().columnNaming(NamingStrategy.no_change)  //是否开启驼峰
                            .controllerBuilder() //创建Controller
//                            .enableRestStyle() //使用restController
                            .mapperBuilder().enableMapperAnnotation();//开启mapper注解
                    // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                }).templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
