package uk.co.solong.schematf.analysis.modules;

import com.fasterxml.jackson.databind.JsonNode;
import uk.co.solong.schematf.analysis.DifferenceReport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SummaryReport implements DiffModule {
    @Override
    public void calculateDiff(List<JsonNode> current, List<JsonNode> previous, DifferenceReport difference) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E d MMM HH:mm");
        String shortSummary = difference.getNewItems().size() + " new items as of " + simpleDateFormat.format(new Date()) + "";
        //fixme include shortlink hex to changes schema.tf/s/A2
        //fixme shorten the tweet but not from the start, and don't truncate the url or datetime.
        difference.setShortSummary(shortSummary);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }
}
