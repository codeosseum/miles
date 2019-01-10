package com.codeosseum.miles.communication.websocket;

import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.google.inject.Inject;

import static spark.Spark.webSocket;

public final class WebSocketBootstrapper {
    private static final String WEBSOCKET_PATH = "/ws";

    private final WebSocketDispatcher dispatcher;

    @Inject
    public WebSocketBootstrapper(final WebSocketDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void bootstrap() {
        webSocket(WEBSOCKET_PATH, dispatcher);
    }
}
