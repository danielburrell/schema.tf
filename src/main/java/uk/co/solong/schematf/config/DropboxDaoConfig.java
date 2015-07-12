package uk.co.solong.schematf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.core.persistence.dropbox.DropboxDao;

@Configuration
public class DropboxDaoConfig {

    @Value("${dropbox.token}")
    private String token;
    
    @Bean
    public DropboxDao dropboxDao() {
        DropboxDao dropboxDao = new DropboxDao(token);
        return dropboxDao;
    }
}
