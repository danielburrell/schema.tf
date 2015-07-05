package uk.co.solong.schematf.web.controllers.api;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.co.solong.schematf.core.SchemaDao;
import uk.co.solong.schematf.web.model.ErrorCodes;
import uk.co.solong.schematf.web.model.ErrorResult;

import com.fasterxml.jackson.databind.JsonNode;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private final SchemaDao schemaDao;

    @RequestMapping("getRawSchema")
    public @ResponseBody JsonNode getSchema() throws ExecutionException {
        return schemaDao.getLatestSchema();
    }

    @ResponseStatus(value = HttpStatus.SERVICE_UNAVAILABLE)
    @ExceptionHandler(ExecutionException.class)
    public @ResponseBody ErrorResult schemaUnavailable() {
        ErrorResult errorResult = new ErrorResult();
        errorResult.setId(ErrorCodes.SCHEMA_LOOKUP_FAILED.getId());
        errorResult.setReason("Schema lookup failed");
        return errorResult;
    }

    public SchemaController(SchemaDao schemaDao) {
        this.schemaDao = schemaDao;
    }
}
