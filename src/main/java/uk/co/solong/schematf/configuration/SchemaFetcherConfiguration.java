package uk.co.solong.schematf.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.schemadao.SchemaFetcher;
import uk.co.solong.schematf.schemadao.SchemaPersistance;
import uk.co.solong.schematf.schemadao.TF2Template;
import uk.co.solong.schematf.twitter.TwitterPoster;

@Configuration
public class SchemaFetcherConfiguration {

    @Autowired
    private SchemaPersistance schemaPersistence;

    @Autowired
    private TF2Template tf2Template;

    @Autowired
    private TwitterPoster twitterPoster;

    @Bean
    public SchemaFetcher schemaFetcher() {
        return new SchemaFetcher(schemaPersistence, tf2Template, twitterPoster);
    }


}
