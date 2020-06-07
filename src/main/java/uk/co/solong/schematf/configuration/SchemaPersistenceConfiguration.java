package uk.co.solong.schematf.configuration;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.schemadao.SchemaPersistance;

@Configuration
public class SchemaPersistenceConfiguration {

    @Autowired
    AmazonS3 amazonS3;

    @Value("${metadata.bucket}")
    private String bucketName;

    @Bean
    public SchemaPersistance schemaPersistance() {
        SchemaPersistance schemaPersistance = new SchemaPersistance(amazonS3, bucketName);
        return schemaPersistance;
    }

}
