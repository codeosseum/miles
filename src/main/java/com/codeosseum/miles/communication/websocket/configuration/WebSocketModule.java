package com.codeosseum.miles.communication.websocket.configuration;

import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.communication.websocket.WebSocketBootstrapper;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.push.WebSocketPushMessageToClientServiceImpl;
import com.codeosseum.miles.communication.websocket.session.DefaultSessionRegistryImpl;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitterImpl;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class WebSocketModule extends AbstractModule {
    private static final List<Class<?>> REQUIRES = asList(Gson.class, JsonParser.class);

    @Override
    protected void configure() {
        REQUIRES.forEach(this::requireBinding);

        bind(MessageTransmitter.class).to(MessageTransmitterImpl.class).in(Singleton.class);

        bind(WebSocketDispatcher.class).in(Singleton.class);

        bind(SessionRegistry.class).to(DefaultSessionRegistryImpl.class).in(Singleton.class);

        bind(PushMessageToClientService.class).to(WebSocketPushMessageToClientServiceImpl.class).in(Singleton.class);

        bind(WebSocketBootstrapper.class).in(Singleton.class);
    }
}
