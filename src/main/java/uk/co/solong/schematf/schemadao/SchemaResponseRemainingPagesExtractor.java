package uk.co.solong.schematf.schemadao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.io.InputStream;

public final class SchemaResponseRemainingPagesExtractor implements ResponseExtractor<JsonNode> {
    @Override
    public JsonNode extractData(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode() == HttpStatus.OK) {
            InputStream inputStream = null;
            try {
                inputStream = response.getBody();
                ObjectMapper m = new ObjectMapper();
                JsonNode jsonBackpack = m.readTree(inputStream);
                return jsonBackpack;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else {
            throw new RuntimeException("SteamAPI returned non-200 status code.{}" + response.getStatusCode());
        }

    }
}
