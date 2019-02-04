package com.example.quartzdemo.scheduler.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.*;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.*;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.lang.reflect.WildcardType;
import java.time.LocalDate;
import java.util.List;

@EnableSwagger2
@Configuration
public class SwaggerConfig {

  @Autowired
  private TypeResolver typeResolver;

  @Bean
  public Docket petApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.example.quartzdemo"))
        .paths(PathSelectors.any())
        .build()
        .pathMapping("/")
        .directModelSubstitute(LocalDate.class, String.class)
        .genericModelSubstitutes(ResponseEntity.class)
        .alternateTypeRules(
            AlternateTypeRules.newRule(typeResolver.resolve(DeferredResult.class,
                typeResolver.resolve(ResponseEntity.class, WildcardType.class)),
                typeResolver.resolve(WildcardType.class)))
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET,
            Lists.newArrayList(new ResponseMessageBuilder()
                .code(500)
                .message("Several: System Error")
                .responseModel(new ModelRef("string"))
                .build()))
        .securitySchemes(Lists.newArrayList(apiKey()))
        .securityContexts(Lists.newArrayList(securityContext()))
        .enableUrlTemplating(true)
        /*.globalOperationParameters(
            Lists.newArrayList(new ParameterBuilder()
                .name("someGlobalParameter")
                .description("Description of someGlobalParameter")
                .modelRef(new ModelRef("string"))
                .parameterType("query")
                .required(true)
                .build()))*/
        //.tags(new Tag("Quartz Scheduler", "All apis relating to quartz"))
        //.additionalModels(typeResolver.resolve(AdditionalModel.class))
        .apiInfo(metadata());
  }

  private ApiInfo metadata() {
    return new ApiInfoBuilder()
        .title("Quartz Demo")
        .description("This Api should provide quartz operations to control scheduled jobs")
        .version("0.0.1")
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("mykey", "api_key", "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.regex("/anyPath.*"))
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope
        = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Lists.newArrayList(
        new SecurityReference("mykey", authorizationScopes));
  }

  @Bean
  public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder()
        .clientId("quartz-demo-client-id")
        .clientSecret("quartz-demo-client-secret")
        .realm("quartz-demo-realm")
        .appName("quartz-demo")
        .scopeSeparator(",")
        .additionalQueryStringParams(null)
        .useBasicAuthenticationWithAccessCodeGrant(false)
        .build();
  }

  @Bean
  public UiConfiguration uiConfig() {
    return UiConfigurationBuilder.builder()
        .deepLinking(true)
        .displayOperationId(false)
        .defaultModelsExpandDepth(1)
        .defaultModelExpandDepth(1)
        .defaultModelRendering(ModelRendering.EXAMPLE)
        .displayRequestDuration(false)
        .docExpansion(DocExpansion.LIST)
        .filter(false)
        .maxDisplayedTags(null)
        .operationsSorter(OperationsSorter.ALPHA)
        .showExtensions(false)
        .tagsSorter(TagsSorter.ALPHA)
        .supportedSubmitMethods(UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS)
        .validatorUrl(null)
        .build();
  }

}
