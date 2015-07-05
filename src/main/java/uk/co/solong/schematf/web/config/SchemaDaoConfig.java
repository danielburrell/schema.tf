package uk.co.solong.schematf.web.config;

import java.io.IOException;

import javax.inject.Inject;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import uk.co.solong.schematf.auth.Credentials;
import uk.co.solong.schematf.core.SchemaDao;

public class SchemaDaoConfig {

    @Value("${git.localdirectory}")
    private String directory;
    @Inject
    private Credentials credentials;

    @Bean
    public SchemaDao schemaDao() throws InvalidRemoteException, TransportException, GitAPIException, IOException {
        SchemaDao schemaDao = new SchemaDao(directory, credentials);
        return schemaDao;
    }
}
