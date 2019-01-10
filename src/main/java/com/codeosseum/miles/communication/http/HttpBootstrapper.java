package com.codeosseum.miles.communication.http;

import com.google.inject.Inject;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;

public final class HttpBootstrapper {
    private final ObjectMapper objectMapper;

    @Inject
    public HttpBootstrapper(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void bootstrap() {
        Unirest.setObjectMapper(objectMapper);
    }
}
