package com.xiaofan0408.idjava.common.config;

import io.swagger.models.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * @author xuzefan  2019/10/29 17:47
 */
@Configuration
public class SwaggerConfiguration {

    @Bean
    public Docket swaggerApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(swaggerApiInfo())
                .enable(true)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xiaofan0408.idjava.web"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo swaggerApiInfo() {
        return new ApiInfoBuilder().title("webflux-swagger2 API doc")
                .description("how to use this")
                .termsOfServiceUrl("https://github.com/xiaofan0408")
                .version("1.0")
                .build();
    }
}