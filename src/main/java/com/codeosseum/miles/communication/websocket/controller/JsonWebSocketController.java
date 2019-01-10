package com.codeosseum.miles.communication.websocket.controller;

import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
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
