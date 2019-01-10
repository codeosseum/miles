package com.codeosseum.miles.messaging.websocket.dispatcher;

import java.io.IOException;
import java.util.*;

import com.codeosseum.miles.messaging.MalformedMessageException;
import com.codeosseum.miles.messaging.action.UnsupportedActionException;
import com.codeosseum.miles.messaging.websocket.handler.OnCloseHandler;
import com.codeosseum.miles.messaging.websocket.handler.OnConnectHandler;
import com.codeosseum.miles.messaging.websocket.handler.OnMessageHandler;
import com.codeosseum.miles.messaging.websocket.transmission.MessageTransmitter;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

@WebSocket
public class WebSocketDispatcher {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketDispatcher.class);

    private static final String ACTION_FIELD = "action";

    private static final String PAYLOAD_FIELD = "payload";

    private final Set<OnConnectHandler> onConnectHandlers;

    private final Set<OnCloseHandler> onCloseHandlers;

    private final Map<String, TypedMessageHandler<?>> messageHandlerMap;

    private final Gson gson;

    private final JsonParser jsonParser;

    private final MessageTransmitter messageTransmitter;

    @Inject
    public WebSocketDispatcher(final Gson gson, final JsonParser jsonParser, final MessageTransmitter messageTransmitter) {
        this.onConnectHandlers = new HashSet<>();
        this.onCloseHandlers = new HashSet<>();
        this.messageHandlerMap = new HashMap<>();

        this.gson = gson;
        this.jsonParser = jsonParser;

        this.messageTransmitter = messageTransmitter;
    }

    public void attachOnConnectHandler(final OnConnectHandler handler) {
        this.onConnectHandlers.add(requireNonNull(handler));
    }

    public void attachOnCloseHandler(final OnCloseHandler handler) {
        this.onCloseHandlers.add(requireNonNull(handler));
    }

    public <T> void attachOnMessageHandler(final String action, final Class<T> payloadType, final OnMessageHandler<T> handler) {
        this.messageHandlerMap.put(requireNonNull(action), new TypedMessageHandler<>(requireNonNull(payloadType), requireNonNull(handler)));
    }

    @OnWebSocketConnect
    public void onConnect(final Session session) {
        onConnectHandlers.forEach(h -> h.handle(session));
    }

    @OnWebSocketClose
    public void onClose(final Session session, final int statusCode, final String reason) {
        onCloseHandlers.forEach(h -> h.handle(session, statusCode, reason));
    }

    @OnWebSocketMessage
    public void onMessage(final Session session, final String message) {
        try {
            final RawMessage rawMessage = parseWebSocketString(message);

            dispatchMessage(session, rawMessage);
        } catch (IOException e) {
            logger.warn(e.toString());
        } catch (Exception e) {
            logger.warn(e.toString());
            sendException(session, e);
        }
    }

    @SuppressWarnings("unchecked")
    private void dispatchMessage(final Session session, final RawMessage message)
            throws IOException, MalformedMessageException, UnsupportedActionException {
        final TypedMessageHandler typedHandler = getHandlerForAction(message.action);

        final Object payload = jsonPayloadToObject(message.payload, typedHandler.payloadType);

        typedHandler.handler.handle(session, typedHandler.payloadType.cast(payload));
    }

    private TypedMessageHandler<?> getHandlerForAction(final String action) throws UnsupportedActionException {
        if (!messageHandlerMap.containsKey(action)) {
            throw new UnsupportedActionException(action);
        }

        return messageHandlerMap.get(action);
    }

    private <T> T jsonPayloadToObject(final String payload, final Class<T> outputType) throws MalformedMessageException {
        try {
            return gson.fromJson(payload, outputType);
        } catch (Exception e) {
            throw new MalformedMessageException(e);
        }
    }

    private void sendException(final Session session, final Exception exception) {
        try {
            messageTransmitter.writeException(session, exception);
        } catch(Exception e) {
            logger.warn(e.toString());
        }
    }

    private RawMessage parseWebSocketString(final String webSocketMessage) throws MalformedMessageException {
        try {
            final JsonObject jsonObject = jsonParser.parse(webSocketMessage).getAsJsonObject();

            final String action = jsonObject.get(ACTION_FIELD).getAsString();
            final String payload = jsonObject.get(PAYLOAD_FIELD).toString();

            return new RawMessage(action, payload);
        } catch (Exception e) {
            throw new MalformedMessageException(e);
        }
    }

    private static final class RawMessage {
        private final String action;

        private final String payload;

        private RawMessage(final String action, final String payload) {
            this.action = action;
            this.payload = payload;
        }
    }

    private static final class TypedMessageHandler<T> {
        private final Class<T> payloadType;

        private final OnMessageHandler<T> handler;

        public TypedMessageHandler(final Class<T> payloadType, final OnMessageHandler<T> handler) {
            this.payloadType = payloadType;
            this.handler = handler;
        }
    }
}

