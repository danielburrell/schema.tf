package uk.co.solong.schematf.analysis.modules;

import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class SummaryReportTest {

    @Test
    public void testFormat() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("E d MMM HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        System.out.println(format);
    }
}