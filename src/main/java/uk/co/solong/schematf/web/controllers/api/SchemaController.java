package uk.co.solong.schematf.web.controllers.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletRequest;

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
import com.fasterxml.jackson.databind.node.TextNode;
import com.fasterxml.jackson.databind.util.JSONPObject;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private static final Logger logger = LoggerFactory.getLogger(SchemaController.class);
    private final SchemaDao schemaDao;
    private final HashCodeGenerator hashCodeGenerator = new HashCodeGenerator();

    @RequestMapping("getRawSchema")
    public @ResponseBody JsonNode getSchema() throws ExecutionException {
        JsonNode latestSchema = schemaDao.getLatestSchema();
        return latestSchema;
    }

    @RequestMapping("getAllItems")
    public @ResponseBody JSONPObject getItems(@RequestParam("c") String callBack) throws ExecutionException {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("data", schemaDao.getItems());
        return new JSONPObject(callBack, map);
    }

    @RequestMapping("getAllItemsJsonP")
    public @ResponseBody JsonNode getItems() throws ExecutionException {
        return schemaDao.getItems();
    }

    @RequestMapping("getAllQualities")
    public @ResponseBody JsonNode getQualities() throws ExecutionException {
        return schemaDao.getQualities();
    }

    @RequestMapping(value = "putSchema", method = RequestMethod.POST)
    public @ResponseBody JsonNode putSchema(@RequestParam long dateObserved, HttpServletRequest request) throws ExecutionException, IOException {
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
    }
}
