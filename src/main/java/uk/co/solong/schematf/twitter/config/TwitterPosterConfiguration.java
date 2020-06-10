package uk.co.solong.schematf.twitter.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import uk.co.solong.schematf.detection.SchemaDatabase;
import uk.co.solong.schematf.twitter.TwitterPoster;

@Configuration
public class TwitterPosterConfiguration {

    @Autowired
    private Twitter twitter;

    @Autowired
    private SchemaDatabase schemaDatabase;

    @Bean
    public TwitterPoster twitterPoster() {
        return new TwitterPoster(schemaDatabase, twitter);
    }
}
