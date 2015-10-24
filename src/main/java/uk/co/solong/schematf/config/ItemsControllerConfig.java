package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.web.controllers.api.ItemsController;

@Configuration
public class ItemsControllerConfig {

    @Inject
    private SchemaDao schemaDao;

    @Bean
    public ItemsController itemsController() {
        ItemsController itemsController = new ItemsController(schemaDao);
        return itemsController;
    }
}
