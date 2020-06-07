package uk.co.solong.schematf.schemadao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

public final class SchemaResponseFirstPageExtractor implements ResponseExtractor<Optional<ResponseData>> {
    @Override
    public Optional<ResponseData> extractData(ClientHttpResponse response) throws IOException {

        if (response.getStatusCode() == HttpStatus.OK) {
            InputStream inputStream = null;
            try {
                inputStream = response.getBody();
                ObjectMapper m = new ObjectMapper();
                JsonNode jsonBackpack = m.readTree(inputStream);
                ResponseData resultData = new ResponseData();
                resultData.setData(jsonBackpack);
                resultData.setLastModified(response.getHeaders().getLastModified());
                return Optional.of(resultData);
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
        } else if (response.getStatusCode() == HttpStatus.NOT_MODIFIED) {
            return Optional.empty();
        } else {
            throw new RuntimeException("SteamAPI returned non-200 status code.{}" + response.getStatusCode());
        }

    }
}
