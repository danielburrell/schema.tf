package uk.co.solong.schema.web.config;


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
import uk.co.solong.hatf2.core.config.MongoCollectionConfig;

@Configuration
@RootConfiguration
@Import({ PropertyPlaceholderConfig.class, RepositoryRestMvcAutoConfiguration.class, HttpMessageConvertersAutoConfiguration.class,
        SpringDataWebAutoConfiguration.class, WebMvcAutoConfiguration.class, EmbeddedServletContainerAutoConfiguration.class,
        DispatcherServletAutoConfiguration.class, ServerPropertiesAutoConfiguration.class, ApplicataionControllerConfig.class, SchemaProviderConfig.class,
        IndexControllerConfig.class, SearchControllerConfig.class, MongoCollectionConfig.class, SearchDaoConfig.class, SignedCookieManagerConfig.class,
        SessionControllerConfig.class, SessionManagerConfig.class })
public class Config {

}
