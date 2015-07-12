package uk.co.solong.schematf.core.notification;

import uk.co.solong.schematf.core.analysis.SchemaAnalysis;

public interface NotificationDao {

    public void notifySchemaChange(SchemaAnalysis schemaAnalysis);

}
