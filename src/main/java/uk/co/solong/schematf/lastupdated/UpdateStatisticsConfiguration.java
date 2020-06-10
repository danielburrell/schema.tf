package uk.co.solong.schematf.lastupdated;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.detection.SchemaDatabase;

@Configuration
public class UpdateStatisticsConfiguration {

    @Autowired
    private SchemaDatabase schemaDatabase;

    @Bean
    public UpdateStatistics lastUpdated() {
        return new UpdateStatistics(schemaDatabase);
    }
}
