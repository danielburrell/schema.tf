package uk.co.solong.schematf.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import uk.co.solong.schematf.twitter.TwitterPoster;

@Configuration
public class TwitterPosterConfiguration {

    @Autowired
    private Twitter twitter;

    @Bean
    public TwitterPoster twitterPoster() {
        return new TwitterPoster(twitter);
    }
}
