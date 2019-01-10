package com.codeosseum.miles.communication.websocket.transmission;

import java.io.IOException;

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
    public void writeMessage(final Session session, final String action, final Object payload) throws IOException {
        final OutboundMessage message = new OutboundMessage(action, payload);

        session.getRemote().sendStringByFuture(gson.toJson(message));
    }

    @Override
    public void writeException(final Session session, final Exception exception) throws IOException {
        final OutboundMessage message = new OutboundMessage("error", exception);

        session.getRemote().sendString(gson.toJson(message));
    }

    private static final class OutboundMessage {
        private final String action;

        private final Object payload;

        public OutboundMessage(final String action, final Object payload) {
            this.action = action;
            this.payload = payload;
        }
    }
}

