package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.web.controllers.api.AdminController;

@Configuration
public class AdminControllerConfig {

    @Inject
    private SchemaDao schemaDao;

    @Bean
    public AdminController adminController() {
        AdminController adminController = new AdminController(schemaDao);
        return adminController;
    }
}
