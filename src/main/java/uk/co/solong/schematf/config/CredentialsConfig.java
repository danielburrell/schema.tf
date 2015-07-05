package uk.co.solong.schematf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.schematf.auth.Credentials;

@Configuration
public class CredentialsConfig {

    @Value("${remoteUrl}")
    private String remoteUrl;
    @Value("${git.api.token}")
    private String token;

    @Bean
    public Credentials credentials() {
        Credentials credentials = new Credentials(remoteUrl, token);
        return credentials;
    }
}
