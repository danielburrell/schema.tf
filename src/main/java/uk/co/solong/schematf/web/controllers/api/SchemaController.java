package uk.co.solong.schematf.web.controllers.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.el.parser.BooleanNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.co.solong.schematf.core.HashCodeGenerator;
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.model.MetaData;
import uk.co.solong.schematf.web.model.ErrorCodes;
import uk.co.solong.schematf.web.model.ErrorResult;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private static final Logger logger = LoggerFactory.getLogger(SchemaController.class); 
    private final SchemaDao schemaDao;
    private final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();
    private Map<Integer, String> colorMap;
    
    @RequestMapping("getRawSchema")
    public @ResponseBody JsonNode getSchema() throws ExecutionException {
        JsonNode latestSchema = schemaDao.getLatestSchema();
        return latestSchema;
    }


    @RequestMapping("getAllItemsJsonP")
    public @ResponseBody JSONPObject getItems(@RequestParam("c") String callBack, HttpServletResponse response) throws ExecutionException {
        response.setContentType("text/javascript; charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        JsonNode items = schemaDao.getItems().deepCopy();
        for (JsonNode item : items) {
            ObjectNode n = (ObjectNode) item;
            n.put("image_url", n.get("image_url").asText().replaceFirst("http://media.steampowered.com/", "https://steamcdn-a.akamaihd.net/"));
        }
        map.put("data", items);
        return new JSONPObject(callBack, map);
    }

    @RequestMapping("getAllItems")
    public @ResponseBody JsonNode getItems() throws ExecutionException {
        return schemaDao.getItems();
    }

    @RequestMapping("getAllQualitiesJsonP")
    public @ResponseBody JSONPObject getAllQualitiesJsonP(@RequestParam("c") String callBack, HttpServletResponse response) throws ExecutionException {
        response.setContentType("text/javascript; charset=UTF-8");
        Map<String, Object> map = new HashMap<String, Object>();
        JsonNode qualities = schemaDao.getQualities().deepCopy();
        for (JsonNode quality : qualities) {
            ObjectNode n = (ObjectNode) quality;
            n.put("color", colorMap.get(n.get("id").asInt()));
        }
        map.put("data", qualities);
        return new JSONPObject(callBack, map);
    }
    
    @RequestMapping("getRawQualities")
    public @ResponseBody JsonNode getRawQualities() throws ExecutionException {
        return schemaDao.getQualities();
    }
    
    @RequestMapping(value="putSchema", method=RequestMethod.POST)
    public @ResponseBody JsonNode putSchema(@RequestParam long dateObserved, HttpServletRequest  request) throws ExecutionException, IOException {
        InputStream is = request.getInputStream();
        ObjectMapper m = new ObjectMapper();
        JsonNode schema = m.readTree(is);
        is.close();
        String hashCode = hashCodeGenerator.getHashCode(schema);
        if (!schemaDao.exists(hashCode)) {
            logger.info("New schema will be persisted {}", hashCode);
            // create meta data
            MetaData metadata = new MetaData();
            metadata.setObservedDate(dateObserved);
            metadata.setHashcode(hashCode);
            schemaDao.persist(schema, metadata);
            return new TextNode("OK");
        } else {
            logger.info("Duplicate schema will not be persisted {}", hashCode);
            return new TextNode("DUPE");
        }
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ExecutionException.class)
    public @ResponseBody ErrorResult schemaUnavailable() {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setId(ErrorCodes.SCHEMA_LOOKUP_FAILED.getId());
        errorResult.setReason("Schema lookup failed");
        return errorResult;
    }
    
    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(Throwable.class)
    public @ResponseBody ErrorResult generalFailure() {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setId(ErrorCodes.SCHEMA_LOOKUP_FAILED.getId());
        errorResult.setReason("Schema lookup failed");
        return errorResult;
    }

    public SchemaController(SchemaDao schemaDao) {
        this.schemaDao = schemaDao;
        this.colorMap = new HashMap<>();
        this.colorMap.put(0, "B2B2B2"); //normal
        this.colorMap.put(1, "4D7455"); //genuine
        this.colorMap.put(2, "000000"); //rarity2
        this.colorMap.put(3, "476291"); //vintage
        this.colorMap.put(4, "000000"); //rarity3
        this.colorMap.put(5, "8650AC"); //unusual
        this.colorMap.put(6, "FFD700"); //unique
        this.colorMap.put(7, "70B04A"); //community
        this.colorMap.put(8, "A50F79"); //valve
        this.colorMap.put(9, "70B04A"); //selfmade
        this.colorMap.put(10, "000000"); //customized
        this.colorMap.put(11, "CF6A32"); //strange
        this.colorMap.put(12, "000000"); //completed
        this.colorMap.put(13, "38F3AB"); //haunted
        this.colorMap.put(14, "AA0000"); //collectors
        this.colorMap.put(15, "FAFAFA"); //decorated
    }
}
