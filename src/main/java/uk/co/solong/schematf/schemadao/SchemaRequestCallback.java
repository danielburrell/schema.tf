package uk.co.solong.schematf.schemadao;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;

public final class SchemaRequestCallback implements RequestCallback {
    private final long lastModified;

    public SchemaRequestCallback(long lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public void doWithRequest(ClientHttpRequest arg0) throws IOException {
        arg0.getHeaders().setIfModifiedSince(lastModified);
    }
}
