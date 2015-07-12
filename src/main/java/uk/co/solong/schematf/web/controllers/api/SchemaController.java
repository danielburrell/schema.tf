package uk.co.solong.schematf.web.controllers.api;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import uk.co.solong.schematf.core.persistence.SchemaDao;
import uk.co.solong.schematf.core.strategy.ItemStrategy;
import uk.co.solong.schematf.core.strategy.QualityStrategy;
import uk.co.solong.schematf.core.strategy.Strategy;
import uk.co.solong.schematf.web.model.ErrorCodes;
import uk.co.solong.schematf.web.model.ErrorResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

@RestController
@RequestMapping("/api")
public class SchemaController {
    private final SchemaDao schemaDao;

    private JsonNode currentSchema;

    private CacheLoader<Strategy, JsonNode> loader;
    private LoadingCache<Strategy, JsonNode> cache;

    private static final Strategy ITEM_KEY = new ItemStrategy();
    private static final Strategy QUALITY_KEY = new QualityStrategy();

    @RequestMapping("getRawSchema")
    public @ResponseBody JsonNode getSchema() throws ExecutionException {
        JsonNode latestSchema = schemaDao.getLatestSchema();
        if (currentSchema != latestSchema) {
            currentSchema = latestSchema;
            cache.invalidateAll();
        }
        return latestSchema;
    }

    @RequestMapping("getAllItems")
    public @ResponseBody JsonNode getItems() throws ExecutionException {
        return cache.get(ITEM_KEY);
    }

    @RequestMapping("getAllQualities")
    public @ResponseBody JsonNode getQualities() throws ExecutionException {
        return cache.get(QUALITY_KEY);
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
        this.loader = new CacheLoader<Strategy, JsonNode>() {
            public JsonNode load(Strategy strategy) throws RuntimeException, JsonProcessingException, IOException {
                return strategy.execute(schemaDao.getLatestSchema());
            }
        };
        this.cache = CacheBuilder.newBuilder().build(loader);
    }
}
