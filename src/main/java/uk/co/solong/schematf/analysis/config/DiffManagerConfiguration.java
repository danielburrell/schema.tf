package uk.co.solong.schematf.analysis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.solong.schematf.analysis.DiffManager;
import uk.co.solong.schematf.analysis.modules.DiffModule;
import uk.co.solong.schematf.analysis.modules.NewItemReport;
import uk.co.solong.schematf.analysis.modules.SummaryReport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class DiffManagerConfiguration {

    @Bean
    public DiffManager diffManager() {
        List<DiffModule> diffModules = new ArrayList<>();
        DiffModule newItemReport = new NewItemReport();
        DiffModule summaryReport = new SummaryReport();
        diffModules.add(newItemReport);
        diffModules.add(summaryReport);
        return new DiffManager(diffModules);
    }
}
