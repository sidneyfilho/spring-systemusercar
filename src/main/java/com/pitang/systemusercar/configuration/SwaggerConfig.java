package com.pitang.systemusercar.configuration;

import com.google.common.base.Predicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)//
                .select()//
                .apis(customRequestHandlers())//
                .build()//
                .apiInfo(metadata())//
                .useDefaultResponseMessages(false)//
                .securitySchemes(Collections.singletonList(apiKey()))
                .securityContexts(Collections.singletonList(securityContext()))
                .tags(new Tag("api", "End Points da API"))//
                .genericModelSubstitutes(Optional.class);
    }

    private ApiInfo metadata() {
        return new ApiInfoBuilder()//
                .title("Desafio Pitang - API RESTful para Sistema de Usuários de Carros")//
                .description("Este é um exemplo de serviço de autenticação do desafio. Você pode descobrir mais sobre o projeto em [https://github.com/sidneyfilho/systemusercar](https://github.com/sidneyfilho/systemusercar). Uma vez que você tenha logado com sucesso e obtido o token `JWT`, você deve clicar no botão superior direito `Authorize` e introduzir o valor. Dessa forma, você poderá usar endpoints `me` ou `cars` por completos.")
                .version("1.0.0")//
                .build();
    }

    private ApiKey apiKey() {
        return new ApiKey("Authorization", "Authorization", "header");
    }

    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.any())
                .build();
    }

    private List<SecurityReference> defaultAuth() {
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        return Arrays.asList(new SecurityReference("Authorization", authorizationScopes));
    }

    private Predicate<RequestHandler> customRequestHandlers() {
        return new Predicate<RequestHandler>() {
            @Override
            public boolean apply(RequestHandler input) {
                Set<RequestMethod> methods = input.supportedMethods();
                return methods.contains(RequestMethod.GET)
                        || methods.contains(RequestMethod.POST)
                        || methods.contains(RequestMethod.PUT)
                        || methods.contains(RequestMethod.DELETE);
            }
        };
    }
}
