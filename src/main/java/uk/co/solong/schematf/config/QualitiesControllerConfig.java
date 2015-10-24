package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.web.controllers.api.QualitiesController;

@Configuration
public class QualitiesControllerConfig {

    @Inject
    private SchemaDao schemaDao;

    @Bean
    public QualitiesController qualitiesController() {
        QualitiesController schemaController = new QualitiesController(schemaDao);
        return schemaController;
    }
}
