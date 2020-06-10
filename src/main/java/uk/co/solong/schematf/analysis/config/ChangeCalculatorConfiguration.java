package uk.co.solong.schematf.analysis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.analysis.ChangeCalculator;
import uk.co.solong.schematf.analysis.DiffManager;
import uk.co.solong.schematf.detection.SchemaDatabase;

@Configuration
public class ChangeCalculatorConfiguration {

    @Autowired
    private DiffManager diffManager;

    @Autowired
    private SchemaDatabase schemaDatabase;

    @Bean
    public ChangeCalculator changeCalculator() {
        return new ChangeCalculator(diffManager, schemaDatabase);
    }
}
