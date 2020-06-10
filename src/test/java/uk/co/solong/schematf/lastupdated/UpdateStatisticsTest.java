package uk.co.solong.schematf.lastupdated;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UpdateStatisticsTest {

    @Test
    public void blankLongNodeBehaviour() {
        JsonNode l = new ObjectMapper().missingNode();
        Long p = l.asLong();
        assertEquals(0L, p);
    }

    @Test
    public void blankTextNodeBehaviour() {
        JsonNode l = new ObjectMapper().missingNode();
        String time = l.asText();
        assertEquals("", time);
    }

    @Test
    public void canParseEventTime() {
        String time = "2020-06-10T18:58:01Z";
        OffsetDateTime o = OffsetDateTime.parse(time);
        Long l = o.toInstant().toEpochMilli();
        assertEquals(1591815481000L, l);
    }
}