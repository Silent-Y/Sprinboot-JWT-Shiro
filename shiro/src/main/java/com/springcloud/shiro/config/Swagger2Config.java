package com.springcloud.shiro.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                /**
                 * apiInfo对象的信息，在页面中有描述
                 */
                .apiInfo(apiInfo())
                .select()
                /**
                 * 指定生成api文档的包名
                 */
                .apis(RequestHandlerSelectors.basePackage("com.springcloud.shiro.controller"))
                /**
                 * 指定生成api的路径
                 */
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                /**
                 * 标题
                 */
                .title("REST API of Springboot JWT Shiro System")
                /**
                 * 描述
                 */
                .description("Springboot集成JWT和Shiro系统的api")
                /**
                 * 服务条款
                 */
                .termsOfServiceUrl("www.baidu.com")
                /**
                 * 通讯
                 */
                .contact(new Contact("Silent-Y","www.baidu.com","1206435482@qq.com"))
                /**
                 * 版本
                 */
                .version("1.0")
                .build();
    }
}
