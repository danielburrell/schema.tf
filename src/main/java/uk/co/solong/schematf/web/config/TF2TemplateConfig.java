package uk.co.solong.schematf.web.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import uk.co.solong.steam4j.tf2.TF2Template;

@Configuration
public class TF2TemplateConfig {

    @Value("${steam.apikey}")
    private String apiKey;

    @Bean
    public TF2Template tf2Template() {
        TF2Template tf2Template = new TF2Template(apiKey);
        return tf2Template;
    }
}
