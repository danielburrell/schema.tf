package uk.co.solong.schematf.schemadao;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class TF2Template {
    private static final Logger logger = LoggerFactory.getLogger(TF2Template.class);
    private final String apiKey;

    private static final String apiGetSchema = "/IEconItems_440/GetSchemaItems/v1/?key={key}&language={language}";
    private static final String apiGetSchemaByPage = "/IEconItems_440/GetSchemaItems/v1/?key={key}&language={language}&start={start}";

    private static final String DEFAULT_BASE_URL = "http://api.steampowered.com";
    private final String language;
    private final RestTemplate restTemplate = new RestTemplate();

    public TF2Template(String apiKey) {
        this(apiKey, "en_US");
    }

    public TF2Template(String apiKey, String language) {
        this.apiKey = apiKey;
        this.language = language;
    }

    public Optional<ResponseData> getNewSchema(Long latestSchemaPublicationTime) {
        String uri = getSchemaUrl();
        Map<String, String> urlVariables = new HashMap<>(2);
        urlVariables.put("key", apiKey);
        urlVariables.put("language", language);
        RequestCallback rcb = new SchemaRequestCallback(latestSchemaPublicationTime);
        Optional<ResponseData> response = restTemplate.execute(uri, HttpMethod.GET, rcb, new SchemaResponseFirstPageExtractor(), urlVariables);
        return response;
    }

    public List<JsonNode> getMoreSchemaData(long start) {
        String uri = getSchemaByPageUrl();

        long currentStart = start;
        boolean continueProcessing = true;
        List<JsonNode> results = new ArrayList<>();
        while (continueProcessing) {
            logger.info("Fetching {}", currentStart);

            continueProcessing = false;
            Map<String, String> urlVariables = new HashMap<>(2);
            urlVariables.put("key", apiKey);
            urlVariables.put("language", language);
            urlVariables.put("start", Long.toString(currentStart));
            JsonNode response = restTemplate.execute(uri, HttpMethod.GET, null, new SchemaResponseRemainingPagesExtractor(), urlVariables);
            if (response != null && response.has("result")) {
                if (response.get("result").has("next")) {
                    currentStart = response.get("result").get("next").asLong();
                    continueProcessing = true;
                }
            }
            results.add(response);
        }
        logger.info("Finished Fetching");
        return results;
    }

    private String getSchemaUrl() {
        return DEFAULT_BASE_URL + apiGetSchema;
    }

    private String getSchemaByPageUrl() {
        return DEFAULT_BASE_URL + apiGetSchemaByPage;
    }


}
