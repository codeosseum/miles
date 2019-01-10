package com.codeosseum.miles.messaging.websocket.push;

import com.codeosseum.miles.messaging.Message;
import com.codeosseum.miles.messaging.push.PushMessageService;
import com.codeosseum.miles.messaging.websocket.session.SessionRegistry;
import com.codeosseum.miles.messaging.websocket.transmission.MessageTransmitter;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

public class WebSocketPushMessageServiceImpl implements PushMessageService {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketPushMessageServiceImpl.class);

    private final SessionRegistry sessionRegistry;

    private final MessageTransmitter messageTransmitter;

    @Inject
    public WebSocketPushMessageServiceImpl(final SessionRegistry sessionRegistry, final MessageTransmitter messageTransmitter) {
        this.sessionRegistry = sessionRegistry;
        this.messageTransmitter = messageTransmitter;
    }

    @Override
    public <T> void sendMessage(long recipientId, final Message<T> message) {
        Objects.requireNonNull(message);

        try {
            final Optional<Session> sessionOptional = sessionRegistry.getSessionForId(recipientId);

            if (!sessionOptional.isPresent()) {
                logger.warn("Recipient not found: {}", recipientId);
                return;
            }

            messageTransmitter.writeMessage(sessionOptional.get(), message.getAction(), message.getPayload());
        } catch (Exception e) {
            logger.warn(e.toString());
        }
    }
}
