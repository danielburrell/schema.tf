package uk.co.solong.schematf.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Twitter;

public class TwitterDao implements NotificationDao {

    private final Twitter twitter;
    private static final Logger logger = LoggerFactory.getLogger(TwitterDao.class);

    @Override
    public void notifySchemaChange(SchemaAnalysis schemaAnalysis) {
        try {
            twitter.timelineOperations().updateStatus("Schema changed:\n"+schemaAnalysis.toString());
        } catch (Throwable e) {
            logger.error("Could not update twitter status.", e);
        }
    }

    public TwitterDao(Twitter twitter) {
        this.twitter = twitter;
    }

}
