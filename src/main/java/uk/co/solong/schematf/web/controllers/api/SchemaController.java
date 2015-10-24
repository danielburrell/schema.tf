package uk.co.solong.schematf.web.controllers.api;

import java.util.concurrent.ExecutionException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.co.solong.schematf.core.cachekeys.Keys;
import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.web.model.ErrorCodes;
import uk.co.solong.schematf.web.model.ErrorResult;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private static final Logger logger = LoggerFactory.getLogger(SchemaController.class);
    private final SchemaDao schemaDao;

    /**
     * Get the raw unaltered schema as provided by valve.
     * 
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("getRawSchema")
    public @ResponseBody JsonNode getSchema(HttpServletResponse response) throws ExecutionException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        JsonNode latestSchema = (JsonNode) schemaDao.getFromDataCache(Keys.SCHEMA_KEY);
        return latestSchema;
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
