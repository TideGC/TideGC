package com.example.config;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableOpenApi // 默认。可省略。
@Import(BeanValidatorPluginsConfiguration.class)
public class swagger3 {
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .apiInfo(apiInfo())
                .enable(true)   // 使用使用 swagger 开关。默认 true ，可省略。
                .select()
//      .apis(RequestHandlerSelectors.any())    // 扫描所有所有路径
//      .apis(RequestHandlerSelectors.none())   // 所有路径都不扫描
//       .apis(RequestHandlerSelectors.basePackage("com.example.controller")) // 扫描指定包路径
         .apis(RequestHandlerSelectors.withMethodAnnotation(Operation.class))  // 以 @ApiOperation 注解为依据进行扫描
//      .apis(RequestHandlerSelectors.withClassAnnotation(Api.class))            // 以 @Api 注解为依据进行扫描
                .paths(PathSelectors.any())     // 过滤器：对外暴露所有 uri
//      .paths(PathSelectors.none())    // 过滤器：一个 uri 都不对外暴露
//      .paths(PathSelectors.ant())     // 过滤器：对外暴露符合 ant 风格正则表达式的 uri
//      .paths(PathSelectors.regex()    // 过滤器：对外暴露符合正则表达式的 uri
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("门诊 项目接口文挡") //  可以用来自定义API的主标题
                .description("XXX Project Swagger3 UserService Interface") // 可以用来描述整体的API
                .version("1.0") // 可以用来定义版本。
                .build();
    }
}
