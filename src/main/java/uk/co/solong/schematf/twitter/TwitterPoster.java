package uk.co.solong.schematf.twitter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import uk.co.solong.schematf.analysis.DifferenceReport;
import uk.co.solong.schematf.detection.SchemaDatabase;

import java.io.IOException;

public class TwitterPoster {

    private final SchemaDatabase schemaDatabase;
    private final Twitter twitter;
    private static final Logger logger = LoggerFactory.getLogger(TwitterPoster.class);

    public TwitterPoster(SchemaDatabase schemaDatabase, Twitter twitter) {
        this.schemaDatabase = schemaDatabase;
        this.twitter = twitter;
    }

    public Boolean createTweet(String pathToReport) {
        logger.info("Loading report {}", pathToReport);
        if (pathToReport != null && !pathToReport.isBlank()) {
            try {
                DifferenceReport report = schemaDatabase.loadReport(pathToReport);
                logger.info("Posting to twitter {}", report.getShortSummary());
                twitter.updateStatus(report.getShortSummary());
            } catch (TwitterException e) {
                logger.error("Could not post to twitter", e);
            } catch (IOException e) {
                logger.error("Could not read report file {}", pathToReport);
            }
        }
        return true;
    }
}
