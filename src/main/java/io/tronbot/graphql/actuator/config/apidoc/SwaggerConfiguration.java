package io.tronbot.graphql.actuator.config.apidoc;

import static springfox.documentation.builders.PathSelectors.regex;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;

import io.tronbot.graphql.actuator.config.Constants;
import io.tronbot.graphql.actuator.config.JHipsterProperties;
import io.tronbot.graphql.actuator.security.jwt.JWTConfigurer;
import io.tronbot.graphql.actuator.service.AuthenticationService;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Springfox Swagger configuration.
 *
 * Warning! When having a lot of REST endpoints, Springfox can become a performance issue. In that
 * case, you can use a specific Spring profile for this class, so that only front-end developers
 * have access to the Swagger view.
 */
@Configuration
@EnableSwagger2
@Import(springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration.class)
@Profile(Constants.SPRING_PROFILE_SWAGGER)
public class SwaggerConfiguration {

    private final Logger log = LoggerFactory.getLogger(SwaggerConfiguration.class);

    public static final String DEFAULT_INCLUDE_PATTERN = "/api/.*";
    
    @Inject
    private AuthenticationService authenticationService;

    /**
     * Swagger Springfox configuration.
     *
     * @param jHipsterProperties the properties of the application
     * @return the Swagger Springfox configuration
     */
    @Bean
    public Docket swaggerSpringfoxDocket(JHipsterProperties jHipsterProperties) {
        log.debug("Starting Swagger");
        StopWatch watch = new StopWatch();
        watch.start();
        Contact contact = new Contact(
            jHipsterProperties.getSwagger().getContactName(),
            jHipsterProperties.getSwagger().getContactUrl(),
            jHipsterProperties.getSwagger().getContactEmail());

        ApiInfo apiInfo = new ApiInfo(
            jHipsterProperties.getSwagger().getTitle(),
            jHipsterProperties.getSwagger().getDescription(),
            jHipsterProperties.getSwagger().getVersion(),
            jHipsterProperties.getSwagger().getTermsOfServiceUrl(),
            contact,
            jHipsterProperties.getSwagger().getLicense(),
            jHipsterProperties.getSwagger().getLicenseUrl());
        
        List<Parameter> globalOperationParameters = new ArrayList<>();
        globalOperationParameters.add(new ParameterBuilder()
        		.parameterType("header")
    	        .description("JWT Token")
    	        .modelRef(new ModelRef("string"))
    	        .name(JWTConfigurer.AUTHORIZATION_HEADER)
    	        .defaultValue(JWTConfigurer.AUTHORIZATION_TOKEN_SCHEMA + authenticationService.authJWT())
    	        .required(false)
    	        .build());

        Docket docket = new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo)
            .forCodeGeneration(true)
            .genericModelSubstitutes(ResponseEntity.class)
            .ignoredParameterTypes(java.sql.Date.class)
            .directModelSubstitute(java.time.LocalDate.class, java.sql.Date.class)
            .directModelSubstitute(java.time.ZonedDateTime.class, Date.class)
            .directModelSubstitute(java.time.LocalDateTime.class, Date.class)
            .globalOperationParameters(globalOperationParameters)
            .select()
            .paths(regex(DEFAULT_INCLUDE_PATTERN))
            .build();
        
        
        
        watch.stop();
        log.debug("Started Swagger in {} ms", watch.getTotalTimeMillis());
        return docket;
    }
}
