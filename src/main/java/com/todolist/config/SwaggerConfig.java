package com.todolist.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
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
public class SwaggerConfig extends WebMvcConfigurationSupport {

    @Value("${app.swagger.title}")
    private String title;

    @Value("${app.swagger.description}")
    private String description;

    @Value("${app.swagger.version}")
    private String version;

    @Value("${app.swagger.license.name}")
    private String license;

    @Value("${app.swagger.license.url}")
    private String licenseUrl;

    @Value("${app.swagger.license.contact.name}")
    private String contactName;

    @Value("${app.swagger.license.contact.url}")
    private String contactUrl;

    @Value("${app.swagger.license.contact.mail}")
    private String contactMail;

    @Value("${app.swagger.basepackage}")
    private String basePackage;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.any())
                .apis(RequestHandlerSelectors.basePackage(basePackage)).build().apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title(title).description(description).version(version).license(license).licenseUrl(licenseUrl)
                .contact(new Contact(contactName, contactUrl, contactMail)).build();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
