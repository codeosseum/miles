package com.codeosseum.miles.chat.configuration;

import com.codeosseum.miles.chat.controller.ChatController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;

import java.util.List;

import static java.util.Arrays.asList;

public class ChatModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MessageTransmitter.class, SessionRegistry.class, WebSocketDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bind(ChatController.class).asEagerSingleton();
    }
}
