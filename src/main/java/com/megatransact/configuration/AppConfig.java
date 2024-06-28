package com.megatransact.configuration;

import ch.qos.logback.classic.Logger;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.core.env.Environment;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@RequiredArgsConstructor
public class AppConfig {
  private final Environment env;
  Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());

  @PostConstruct
  public void init() {
    initProperties();
  }

  public void initProperties() {
    try {
      // TODO: Perform manual assignment to be able to use properties as a singleton pattern
      AppProperties.appSwaggerServerUrl = env.getRequiredProperty("app.swagger-server-url", String.class);
    } catch (IllegalStateException e) {
      logger.error("Unable to initialize property from environment variables. Check your environment and/or profiles definition.");
      logger.error(e.getMessage());
    } catch (ConversionFailedException e) {
      logger.error("Unable to convert property to target type from environment variables. Check your environment and/or profiles definition.");
      logger.error(e.getMessage());
    }
  }

  @Bean
  public OpenAPI openApiConfig() {
    final String title = "MegaTransact API";
    final String description = "MegaTransact API description";
    final String version = "1.0.0";
    final String serverUrl = AppProperties.appSwaggerServerUrl;
    final String securitySchemeName = "bearerAuth";

    return new OpenAPI()
      .addSecurityItem(
        new SecurityRequirement()
          .addList(securitySchemeName)
      )
      .components(
        new Components()
          .addSecuritySchemes(securitySchemeName, new SecurityScheme()
            .name(securitySchemeName)
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
          )
      )
      .addServersItem(
        new Server()
          .url(serverUrl)
      )
      .info(
        new Info()
          .title(title)
          .description(description)
          .version(version)
      );
  }

}
