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
public class ItemsController {
    private static final Logger logger = LoggerFactory.getLogger(ItemsController.class);
    private final SchemaDao schemaDao;

    public ItemsController(SchemaDao schemaDao) {
        this.schemaDao = schemaDao;
    }
    
    @RequestMapping("getAllItemsRaw")
    public @ResponseBody JsonNode getAllItemsRaw(HttpServletResponse response) throws ExecutionException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        return schemaDao.getFromDataCache(Keys.ITEM_RAW_KEY);
    }
    
    /**
     * Get all items in the schema with the cdn substituted.
     * 
     * @param callBack
     * @param response
     * @return
     * @throws ExecutionException
     */
    @RequestMapping("getAllItemsCdn")
    public @ResponseBody JsonNode getAllItemsCdn(HttpServletResponse response) throws ExecutionException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setContentType("text/javascript; charset=UTF-8");
        return schemaDao.getFromDataCache(Keys.ITEM_CDN_KEY);
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

}
