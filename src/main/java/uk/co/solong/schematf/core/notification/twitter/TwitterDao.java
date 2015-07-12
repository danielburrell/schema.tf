package uk.co.solong.schematf.core.notification.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Twitter;

import uk.co.solong.schematf.core.analysis.SchemaAnalysis;
import uk.co.solong.schematf.core.notification.NotificationDao;

public class TwitterDao implements NotificationDao {

    private final Twitter twitter;
    private static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

    @Override
    public void notifySchemaChange(SchemaAnalysis schemaAnalysis) {
        try {
            String rawTweet = schemaAnalysis.toString();
            String tweet = rawTweet.substring(0, Math.min(140, rawTweet.length()));
            twitter.timelineOperations().updateStatus(tweet);
        } catch (Throwable e) {
            logger.error("Could not update twitter status.", e);
        }
    }

    public TwitterDao(Twitter twitter) {
        this.twitter = twitter;
    }

}
