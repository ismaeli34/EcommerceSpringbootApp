package com.ecommerce.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket docket() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2);
        docket.apiInfo(getApiInfo());


        docket.securityContexts(Arrays.asList(getSecurityContext()));
        docket.securitySchemes(Arrays.asList(getSchemes()));

        ApiSelectorBuilder select = docket.select();
        select.apis(RequestHandlerSelectors.any());
        select.paths(PathSelectors.any());
        Docket build = select.build();
        return build;
    }


    private SecurityContext getSecurityContext() {

        SecurityContext context = SecurityContext
                .builder()
                .securityReferences(getSecurityReferences())
                .build();
        return context;
    }

    private List<SecurityReference> getSecurityReferences() {
        AuthorizationScope[] scopes = {new AuthorizationScope("Global", "Access Every Thing")};
        return Arrays.asList(new SecurityReference("JWT", scopes));

    }

    private ApiKey getSchemes() {
        return new ApiKey("JWT", "Authorization", "header");
    }


    private ApiInfo getApiInfo() {

        ApiInfo apiInfo = new ApiInfo(
                "E-commerce Portal: APIS ",
                "This is backend project created by Ronney Ismael",
                "1.0.0V",
                "https://my-portfolio-23524.web.app/#home",
                new Contact("Ronney", "https://www.instagram.com/xyz", "https://my-portfolio-23524.web.app/#home"),
                "License of APIS",
                "https://my-portfolio-23524.web.app/#home",
                new ArrayDeque<>()
        );

        return apiInfo;

    }


}
