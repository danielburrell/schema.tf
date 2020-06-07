package uk.co.solong.schematf.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import some.other.place.*;
import uk.co.solong.schematf.configuration.*;
import uk.co.solong.schematf.schemadao.SchemaFetcher;

import java.util.function.Function;

@SpringBootApplication
@Import({AwsS3SecurityAccessConfiguration.class, SchemaFetcherConfiguration.class, SchemaPersistenceConfiguration.class, TF2TemplateConfiguration.class, TwitterConfiguration.class, TwitterPosterConfiguration.class})
public class SchemaTF {

    /*
     * You need this main method or explicit <start-class>example.FunjctionConfiguration</start-class>
     * in the POM to ensure boot plug-in makes the correct entry
     */
    public static void main(String[] args) {
        SpringApplication.run(SchemaTF.class, args);
    }

    @Autowired
    SchemaFetcher schemaFetcher;

    @Bean
    public Function<Long, Boolean> checkForNewSchema() {
        return schemaFetcher::checkNewSchema;
    }
}