package org.wangfeng.panda.app.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User: wangfeng
 * Date: 2020/8/24
 */
@Configuration
public class SwaggerDocumentionConfiguration {


    @Value("${swagger.show}")
    private Boolean swaggerShow;

    ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Wangfeng panda ServiceApi")
                .description("wangfeng-panda-app")
                .license("")
                .licenseUrl("http://unlicense.org")
                .termsOfServiceUrl("")
                .version("1.0.0")
                .contact(new Contact("", "", ""))
                .build();
    }

    @Bean
    public Docket customImplementation() {
        return new Docket(DocumentationType.SWAGGER_2).enable(swaggerShow)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.wangfeng.panda"))
                .build()
                .directModelSubstitute(LocalDate.class, java.sql.Date.class)
                .directModelSubstitute(LocalDateTime.class, java.util.Date.class)
                .apiInfo(apiInfo());
    }
}