package uk.co.solong.schematf.web.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import uk.co.solong.schematf.core.SchemaDao;
import uk.co.solong.schematf.core.SchemaPoller;
import uk.co.solong.steam4j.tf2.TF2Template;

@Configuration
@EnableScheduling
public class SchemaPollerConfig {

    @Inject
    private TF2Template tf2Template;
    @Inject
    private SchemaDao schemaDao;

    @Bean
    public SchemaPoller schemaPoller() {
        SchemaPoller schemaPoller = new SchemaPoller(tf2Template, schemaDao);
        return schemaPoller;
    }
}
