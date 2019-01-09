package com.codeosseum.miles.messaging.websocket.controller;

import com.codeosseum.miles.messaging.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.messaging.websocket.transmission.MessageTransmitter;
import com.google.gson.Gson;

public abstract class JsonWebSocketController {
    protected final Gson gson;

    protected final MessageTransmitter messageTransmitter;

    public JsonWebSocketController(Gson gson, MessageTransmitter messageTransmitter) {
        this.gson = gson;
        this.messageTransmitter = messageTransmitter;
    }

    public abstract void attach(WebSocketDispatcher dispatcher);
}
