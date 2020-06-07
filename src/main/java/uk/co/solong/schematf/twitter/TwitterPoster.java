package uk.co.solong.schematf.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterPoster {

    private final Twitter twitter;
    private static final Logger logger = LoggerFactory.getLogger(TwitterPoster.class);

    public TwitterPoster(Twitter twitter) {
        this.twitter = twitter;
    }

    public void createTweet(String tweet) {
        try {
            twitter.updateStatus(tweet);
        } catch (TwitterException e) {
            logger.error("Could not post to twitter", e);
        }
    }
}
