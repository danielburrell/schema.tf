package uk.co.solong.schematf.config;

import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebAutoConfiguration;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.EmbeddedServletContainerAutoConfiguration;
import org.springframework.boot.autoconfigure.web.HttpMessageConvertersAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerPropertiesAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import uk.co.solong.application.annotations.RootConfiguration;
import uk.co.solong.application.config.PropertyPlaceholderConfig;

@Configuration
@RootConfiguration
@Import({ PropertyPlaceholderConfig.class, RepositoryRestMvcAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class, WebMvcAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class, ServerPropertiesAutoConfiguration.class, CredentialsConfig.class, SchemaControllerConfig.class,
        SchemaDaoConfig.class, SchemaPollerConfig.class, TF2TemplateConfig.class, TwitterConfig.class, TwitterDaoConfig.class })
public class Config {

}
