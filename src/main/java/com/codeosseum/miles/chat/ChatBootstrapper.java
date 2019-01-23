package com.codeosseum.miles.chat;

import com.codeosseum.miles.chat.controller.ChatController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.google.inject.Inject;

public final class ChatBootstrapper {
    private final WebSocketDispatcher webSocketDispatcher;

    private final ChatController chatController;

    @Inject
    public ChatBootstrapper(WebSocketDispatcher webSocketDispatcher, ChatController chatController) {
        this.webSocketDispatcher = webSocketDispatcher;
        this.chatController = chatController;
    }

    public void bootstrap() {
        chatController.attach(webSocketDispatcher);
    }
}
