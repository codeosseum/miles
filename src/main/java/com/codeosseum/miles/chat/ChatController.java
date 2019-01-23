package com.codeosseum.miles.chat;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.util.date.DateConversion;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.Value;
import org.eclipse.jetty.websocket.api.Session;

import java.time.LocalDateTime;

import static com.codeosseum.miles.util.date.DateConversion.epochMillisecondsToLocalDateTime;

public class ChatController extends JsonWebSocketController {
    private static final String INCOMING_CHAT_MESSAGE = "incoming-chat-message";

    private static final String GET_ROOMS_OF_USER = "get-rooms-of-user";

    private static final String GET_MESSAGES_SINCE = "get-messages-since";

    private final SessionRegistry sessionRegistry;

    private final ChatService chatService;

    @Inject
    public ChatController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry, final ChatService chatService) {
        super(gson, messageTransmitter);

        this.sessionRegistry = sessionRegistry;
        this.chatService = chatService;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnMessageHandler(INCOMING_CHAT_MESSAGE, IncomingChatMessage.class, this::onIncomingChatMessage);
    }

    private void onIncomingChatMessage(final Session session, final IncomingChatMessage payload) {
        sessionRegistry.getIdForSession(session)
                .map(sender -> incomingChatMessageToChatMessage(sender, payload))
                .ifPresent(chatService::sendMessage);
    }

    private ChatMessage incomingChatMessageToChatMessage(final String sender, final IncomingChatMessage incoming) {
        final LocalDateTime timestamp = epochMillisecondsToLocalDateTime(incoming.timestamp);

        return new ChatMessage(sender, timestamp, incoming.roomId, incoming.contents, incoming.type);
    }

    @Value
    private static final class IncomingChatMessage {
        private final long timestamp;

        private final String roomId;

        private final String contents;

        private final String type;
    }
}
