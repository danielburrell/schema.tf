package uk.co.solong.schematf.detection;

import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.RequestCallback;

import java.io.IOException;
import java.util.Optional;

public final class SchemaRequestCallback implements RequestCallback {
    private final Optional<Long> lastModified;

    public SchemaRequestCallback(Optional<Long> lastModified) {
        this.lastModified = lastModified;
    }

    @Override
    public void doWithRequest(ClientHttpRequest arg0) throws IOException {
        lastModified.ifPresent(arg0.getHeaders()::setIfModifiedSince);
    }
}
