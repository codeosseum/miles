package com.codeosseum.miles.session.configuration;

import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.session.controller.SessionController;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;

import java.util.List;

import static java.util.Arrays.asList;

public class SessionModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MessageTransmitter.class, WebSocketDispatcher.class, SessionRegistry.class);
    }

    @Override
    protected void configureModule() {
        bind(SessionController.class).asEagerSingleton();
    }
}
