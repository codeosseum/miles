package com.codeosseum.miles.chat.controller;

import com.codeosseum.miles.chat.*;
import com.codeosseum.miles.chat.controller.incoming.IncomingChatMessage;
import com.codeosseum.miles.chat.controller.incoming.MessagesSinceRequest;
import com.codeosseum.miles.chat.controller.outgoing.MessagesSince;
import com.codeosseum.miles.chat.controller.outgoing.RoomsOfUser;
import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.codeosseum.miles.util.date.DateConversion.epochMillisecondsToLocalDateTime;

public class ChatController extends JsonWebSocketController {
    private static final String INCOMING_CHAT_MESSAGE = "incoming-chat-message";

    private static final String GET_ROOMS_OF_USER = "get-rooms-of-user";

    private static final String GET_MESSAGES_SINCE = "get-messages-since";

    private final SessionRegistry sessionRegistry;

    private final ChatService chatService;

    private final RoomService roomService;

    private final MessageService messageService;

    @Inject
    public ChatController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry, final ChatService chatService, final RoomService roomService, final MessageService messageService) {
        super(gson, messageTransmitter);

        this.sessionRegistry = sessionRegistry;
        this.chatService = chatService;
        this.roomService = roomService;
        this.messageService = messageService;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnMessageHandler(INCOMING_CHAT_MESSAGE, IncomingChatMessage.class, this::incomingChatMessage);
        dispatcher.attachOnMessageHandler(GET_MESSAGES_SINCE, MessagesSinceRequest.class, this::getMessagesSince);

        dispatcher.attachOnSignalHandler(GET_ROOMS_OF_USER, this::getRoomsOfUser);
    }

    private void incomingChatMessage(final Session session, final IncomingChatMessage payload) {
        sessionRegistry.getIdForSession(session)
                .map(sender -> incomingChatMessageToChatMessage(sender, payload))
                .ifPresent(chatService::sendMessage);
    }

    private void getRoomsOfUser(final Session session) throws IOException  {
        final Optional<RoomsOfUser> roomsOfUserOptional = sessionRegistry.getIdForSession(session)
                .map(roomService::getRoomsIncluding)
                .map(RoomsOfUser::new);

        if (roomsOfUserOptional.isPresent()) {
            messageTransmitter.writeMessage(session, GET_ROOMS_OF_USER, roomsOfUserOptional.get());
        }
    }

    private void getMessagesSince(final Session session, final MessagesSinceRequest request) throws IOException {
        final Optional<List<ChatMessage>> messagesOptional = sessionRegistry.getIdForSession(session)
                .map(user -> messagesSinceRequestToMessages(user, request));

        if (messagesOptional.isPresent()) {
            messageTransmitter.writeMessage(session, GET_MESSAGES_SINCE, new MessagesSince(messagesOptional.get()));
        }
    }

    private List<ChatMessage> messagesSinceRequestToMessages(final String user, final MessagesSinceRequest request) {
        final LocalDateTime since = epochMillisecondsToLocalDateTime(request.getTimestamp());

        return messageService.getMessageForRoomSince(request.getRoomId(), since);
    }

    private ChatMessage incomingChatMessageToChatMessage(final String sender, final IncomingChatMessage incoming) {
        final LocalDateTime timestamp = epochMillisecondsToLocalDateTime(incoming.getTimestamp());

        return new ChatMessage(sender, timestamp, incoming.getRoomId(), incoming.getContents(), incoming.getType());
    }
}
