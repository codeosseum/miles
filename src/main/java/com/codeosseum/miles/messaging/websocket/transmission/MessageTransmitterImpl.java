package com.codeosseum.miles.messaging.websocket.transmission;

import java.io.IOException;

import com.codeosseum.miles.messaging.action.Action;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;

public class MessageTransmitterImpl implements MessageTransmitter {
    private final Gson gson;

    @Inject
    public MessageTransmitterImpl(Gson gson) {
        this.gson = gson;
    }

    @Override
    public void writeMessage(final Session session, final Action action, final Object payload) throws IOException {
        final OutboundMessage message = new OutboundMessage(action, payload);

        session.getRemote().sendStringByFuture(gson.toJson(message));
    }

    @Override
    public void writeException(final Session session, final Exception exception) throws IOException {
        final OutboundMessage message = new OutboundMessage(Action.ERROR, exception);

        session.getRemote().sendString(gson.toJson(message));
    }

    private static final class OutboundMessage {
        private final String action;

        private final Object payload;

        public OutboundMessage(Action action, Object payload) {
            this.action = action.name();
            this.payload = payload;
        }
    }
}

