package uk.co.solong.schematf.config;

import javax.inject.Inject;

import org.springframework.context.annotation.Bean;

import uk.co.solong.schematf.core.persistence.dropbox.DropboxDao;
import uk.co.solong.schematf.core.persistence.dropbox.DropboxSchemaDao;

public class DropboxSchemaDaoConfig {

   
    @Inject
    private DropboxDao dropboxDao;

    @Bean
    public DropboxSchemaDao dropboxSchemaDao() {
        DropboxSchemaDao schemaDao = new DropboxSchemaDao(dropboxDao);
        return schemaDao;
    }
}
