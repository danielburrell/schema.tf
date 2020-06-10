package uk.co.solong.schematf.twitter.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

@Configuration
public class TwitterConfiguration {

    @Value("${twitter4j.oauth.consumerKey}")
    private String consumerKey;

    @Value("${twitter4j.oauth.consumerSecret}")
    private String consumerSecret;

    @Value("${twitter4j.oauth.accessToken}")
    private String accessToken;

    @Value("${twitter4j.oauth.accessTokenSecret}")
    private String accessTokenSecret;

    @Bean
    public Twitter twitter() {
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey(consumerKey)
                .setOAuthConsumerSecret(consumerSecret)
                .setOAuthAccessToken(accessToken)
                .setOAuthAccessTokenSecret(accessTokenSecret);
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        return twitter;
    }
}
