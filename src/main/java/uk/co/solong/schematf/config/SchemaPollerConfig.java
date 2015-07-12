package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import uk.co.solong.schematf.core.SchemaPoller;
import uk.co.solong.schematf.core.notification.NotificationDao;
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.steam4j.tf2.TF2Template;

@Configuration
@EnableScheduling
public class SchemaPollerConfig {

    @Inject
    private TF2Template tf2Template;
    @Inject
    private SchemaDao schemaDao;
    @Inject
    private NotificationDao notificationDao;

    @Bean
    public SchemaPoller schemaPoller() {
        SchemaPoller schemaPoller = new SchemaPoller(tf2Template, schemaDao, notificationDao);
        return schemaPoller;
    }
}
