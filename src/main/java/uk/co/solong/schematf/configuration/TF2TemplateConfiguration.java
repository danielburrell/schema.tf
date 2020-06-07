package uk.co.solong.schematf.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.schemadao.TF2Template;

@Configuration
public class TF2TemplateConfiguration {
    @Value("${steam.apiKey}")
    private String apiKey;

    @Bean
    public TF2Template tf2Template() {
        return new TF2Template(apiKey);
    }
}
