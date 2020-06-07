package uk.co.solong.schematf.schemadao;

import java.io.IOException;

public class CouldNotRetrieveLatestSchemaPublicationTimeException extends RuntimeException {
    public CouldNotRetrieveLatestSchemaPublicationTimeException(IOException e) {
        super();
        this.initCause(e);
    }
}
