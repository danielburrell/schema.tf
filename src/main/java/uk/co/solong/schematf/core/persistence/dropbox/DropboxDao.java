package uk.co.solong.schematf.core.persistence.dropbox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DropboxDao {
    private static final Logger logger = LoggerFactory.getLogger(DropboxDao.class); 
    private final RestTemplate restTemplate;
    private final String token;
    
    public void uploadFile(JsonNode jsonNode, String fileName) {
        logger.debug("Uploading {}", fileName);
        Map<String,String> urlVariables = new HashMap<String,String>();
        urlVariables.put("overwrite", "true");
        urlVariables.put("autorename", "false");
        urlVariables.put("access_token", token);
        restTemplate.postForObject("https://api-content.dropbox.com/1/files_put/auto/"+fileName+"?overwrite={overwrite}&autorename={autorename}",jsonNode,JsonNode.class,urlVariables);
    }

    public DropboxDao(String token) {
        this.token = token;
        restTemplate = new RestTemplate();
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
        interceptors.add(new HeaderRequestInterceptor("Authorization", "Bearer "+token));
        restTemplate.setInterceptors(interceptors);
    }

    public JsonNode ls(String string) {
        Map<String,String> urlVariables = new HashMap<String,String>();
        urlVariables.put("access_token", token);
        JsonNode jsonNode = restTemplate.getForObject("https://api.dropbox.com/1/metadata/auto/",JsonNode.class,urlVariables);
        return jsonNode;
    }

    public JsonNode getFile(String path) {
        logger.debug("Downloading {}", path);
        Map<String,String> urlVariables = new HashMap<String,String>();
        urlVariables.put("access_token", token);
        String jsonNode = restTemplate.getForObject("https://api-content.dropbox.com/1/files/auto"+path,String.class,urlVariables);
        ObjectMapper m = new ObjectMapper();
        try {
            return m.readTree(jsonNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    
}
