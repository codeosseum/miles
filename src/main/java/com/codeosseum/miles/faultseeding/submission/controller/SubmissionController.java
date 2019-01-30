package com.codeosseum.miles.faultseeding.submission.controller;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.google.gson.Gson;
import lombok.Value;
import org.eclipse.jetty.websocket.api.Session;

public class SubmissionController extends JsonWebSocketController {
    private static final String POST_FAULT_SEEDING_SUBMISSION = "post-fault-seeding-submission";

    private final SessionRegistry sessionRegistry;

    public SubmissionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry) {
        super(gson, messageTransmitter);
        this.sessionRegistry = sessionRegistry;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnMessageHandler(POST_FAULT_SEEDING_SUBMISSION, IncomingSubmission.class, this::handleIncomingSubmission);
    }

    private void handleIncomingSubmission(final Session session, final IncomingSubmission incomingSubmission) {

    }

    @Value
    private static final class IncomingSubmission {
        private final long timestamp;

        private final String code;
    }
}
