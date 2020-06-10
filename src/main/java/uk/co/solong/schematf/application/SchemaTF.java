package uk.co.solong.schematf.application;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import uk.co.solong.schematf.analysis.ChangeCalculator;
import uk.co.solong.schematf.analysis.DiffRequest;
import uk.co.solong.schematf.analysis.config.ChangeCalculatorConfiguration;
import uk.co.solong.schematf.analysis.config.DiffManagerConfiguration;
import uk.co.solong.schematf.detection.SchemaChangeResult;
import uk.co.solong.schematf.security.config.AwsS3SecurityAccessConfiguration;
import uk.co.solong.schematf.detection.config.SchemaFetcherConfiguration;
import uk.co.solong.schematf.detection.config.SchemaPersistenceConfiguration;
import uk.co.solong.schematf.detection.config.TF2TemplateConfiguration;
import uk.co.solong.schematf.twitter.config.TwitterConfiguration;
import uk.co.solong.schematf.twitter.config.TwitterPosterConfiguration;
import uk.co.solong.schematf.lastupdated.UpdateStatistics;
import uk.co.solong.schematf.lastupdated.UpdateStatisticsConfiguration;
import uk.co.solong.schematf.detection.SchemaFetcher;
import uk.co.solong.schematf.twitter.TwitterPoster;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@SpringBootApplication
@Import({AwsS3SecurityAccessConfiguration.class, SchemaFetcherConfiguration.class, SchemaPersistenceConfiguration.class,
        TF2TemplateConfiguration.class, TwitterConfiguration.class, TwitterPosterConfiguration.class,
        ChangeCalculatorConfiguration.class, DiffManagerConfiguration.class, UpdateStatisticsConfiguration.class
})
public class SchemaTF {

    private static final Logger logger = LoggerFactory.getLogger(SchemaTF.class);

    /*
     * You need this main method or explicit <start-class>example.FunjctionConfiguration</start-class>
     * in the POM to ensure boot plug-in makes the correct entry
     */
    public static void main(String[] args) {
        SpringApplication.run(SchemaTF.class, args);
    }

    @Autowired
    SchemaFetcher schemaFetcher;

    @Autowired
    ChangeCalculator changeCalculator;

    @Autowired
    TwitterPoster twitterPoster;

    @Autowired
    UpdateStatistics updateStatistics;

    @Bean
    public Function<DiffRequest, String> generateSchemaDiff() {
        return changeCalculator::generateChanges;
    }

    @Bean
    public Function<Boolean, SchemaChangeResult> checkForSchemaChanges() {
        return schemaFetcher::checkNewSchema;
    }

    @Bean
    public Function<JsonNode, JsonNode> updateLastChecked() {
        return updateStatistics::updateStatistics;

    }

    @Bean
    public Function<String, Boolean> notifyTwitter() {
        return twitterPoster::createTweet;
    }

    @Bean
    public Consumer<String> consumer() {
        return (x) -> {};
    }

    @Bean
    public Supplier<String> supplier() {
        return () -> "worked";
    }

    @Bean
    public Function<JsonNode, JsonNode> handleUnauthorized() {
        return updateStatistics::handleUnauthorized;
    }

    @Bean
    public Function<JsonNode, JsonNode> handleSteamIntermittent() {
        return updateStatistics::handleSteamIntermittent;
    }
}