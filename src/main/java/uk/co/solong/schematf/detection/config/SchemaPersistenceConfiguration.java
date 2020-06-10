package uk.co.solong.schematf.detection.config;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.detection.SchemaDatabase;

@Configuration
public class SchemaPersistenceConfiguration {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${metadata.bucket}")
    private String bucketName;

    @Bean
    public SchemaDatabase schemaPersistance() {
        SchemaDatabase schemaDatabase = new SchemaDatabase(amazonS3, bucketName);
        return schemaDatabase;
    }

}
