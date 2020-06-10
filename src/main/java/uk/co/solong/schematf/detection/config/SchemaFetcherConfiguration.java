package uk.co.solong.schematf.detection.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.detection.SchemaFetcher;
import uk.co.solong.schematf.detection.SchemaDatabase;
import uk.co.solong.schematf.detection.TF2Template;
import uk.co.solong.schematf.twitter.TwitterPoster;

@Configuration
public class SchemaFetcherConfiguration {

    @Autowired
    private SchemaDatabase schemaPersistence;

    @Autowired
    private TF2Template tf2Template;

    @Autowired
    private TwitterPoster twitterPoster;

    @Bean
    public SchemaFetcher schemaFetcher() {
        return new SchemaFetcher(schemaPersistence, tf2Template, twitterPoster);
    }


}
