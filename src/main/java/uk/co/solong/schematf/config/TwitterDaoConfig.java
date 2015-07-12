package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.twitter.api.Twitter;

import uk.co.solong.schematf.core.notification.twitter.TwitterDao;

@Configuration
public class TwitterDaoConfig {

    @Inject
    private Twitter twitter;
    
    @Bean
    public TwitterDao twitterDao() {
        TwitterDao twitterDao = new TwitterDao(twitter);
        return twitterDao;
    }
}
