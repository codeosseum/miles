package com.codeosseum.miles.communication.websocket.push;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class WebSocketPushMessageToClientServiceImpl implements PushMessageToClientService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketPushMessageToClientServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    private final MessageTransmitter messageTransmitter;

    @Inject
    public WebSocketPushMessageToClientServiceImpl(final SessionRegistry sessionRegistry, final MessageTransmitter messageTransmitter) {
        this.sessionRegistry = sessionRegistry;
        this.messageTransmitter = messageTransmitter;
    }

    @Override
    public <T> void sendMessage(long recipientId, final Message<T> message) {
        Objects.requireNonNull(message);

        LOGGER.debug("Pushing WebSocket message (recipientID {}): {}", recipientId, message);

        try {
            final Optional<Session> sessionOptional = sessionRegistry.getSessionForId(recipientId);

            if (!sessionOptional.isPresent()) {
                LOGGER.warn("Recipient not found: {}", recipientId);
                return;
            }

            messageTransmitter.writeMessage(sessionOptional.get(), message.getAction(), message.getPayload());
        } catch (Exception e) {
            LOGGER.warn(e.toString());
        }
    }
}
