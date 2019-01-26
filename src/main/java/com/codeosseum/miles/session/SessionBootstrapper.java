package com.codeosseum.miles.session;

import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.session.controller.SessionController;
import com.google.inject.Inject;

public class SessionBootstrapper {
    private final WebSocketDispatcher webSocketDispatcher;

    private final SessionController sessionController;

    @Inject
    public SessionBootstrapper(final WebSocketDispatcher webSocketDispatcher, final SessionController sessionController) {
        this.webSocketDispatcher = webSocketDispatcher;
        this.sessionController = sessionController;
    }

    public void bootstrap() {
        sessionController.attach(webSocketDispatcher);
    }
}
