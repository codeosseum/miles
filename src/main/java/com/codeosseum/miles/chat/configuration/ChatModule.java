package com.codeosseum.miles.chat.configuration;

import com.codeosseum.miles.chat.ChatService;
import com.codeosseum.miles.chat.DefaultChatServiceImpl;
import com.codeosseum.miles.chat.controller.ChatController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class ChatModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MessageTransmitter.class, SessionRegistry.class, WebSocketDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bind(ChatService.class).to(DefaultChatServiceImpl.class).in(Singleton.class);

        bind(ChatController.class).asEagerSingleton();
    }
}
