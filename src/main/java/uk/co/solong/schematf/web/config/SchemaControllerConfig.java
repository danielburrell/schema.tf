package uk.co.solong.schematf.web.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.core.SchemaDao;
import uk.co.solong.schematf.web.controllers.api.SchemaController;

@Configuration
public class SchemaControllerConfig {

    @Inject
    private SchemaDao schemaDao;

    @Bean
    public SchemaController schemaController() {
        SchemaController schemaController = new SchemaController(schemaDao);
        return schemaController;
    }
}
