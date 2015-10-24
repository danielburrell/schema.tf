package uk.co.solong.schematf.core.cachekeys;

import uk.co.solong.schematf.core.persistence.dropbox.SchemaStrategy;
import uk.co.solong.schematf.core.strategy.DerivativeDataLoader;
import uk.co.solong.schematf.core.strategy.ItemRawStrategy;
import uk.co.solong.schematf.core.strategy.ItemSimpleStrategy;
import uk.co.solong.schematf.core.strategy.QualityRawLoader;
import uk.co.solong.schematf.core.strategy.QualitySimpleLoader;

import com.fasterxml.jackson.databind.JsonNode;

public class Keys {
    //root schema key
    public static final DerivativeDataLoader<JsonNode> SCHEMA_KEY = new SchemaStrategy();
    
    //root item key
    public static final DerivativeDataLoader<JsonNode> ITEM_RAW_KEY = new ItemRawStrategy();
    public static final DerivativeDataLoader<JsonNode> ITEM_CDN_KEY = new ItemSimpleStrategy();
    
    //quality keys
    public static final DerivativeDataLoader<JsonNode> QUALITY_RAW_KEY = new QualityRawLoader();
    public static final DerivativeDataLoader<JsonNode> QUALITY_SIMPLE_KEY = new QualitySimpleLoader();
}
