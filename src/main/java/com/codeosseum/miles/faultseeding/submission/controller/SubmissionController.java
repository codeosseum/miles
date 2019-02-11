package com.codeosseum.miles.faultseeding.submission.controller;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.submission.Submission;
import com.codeosseum.miles.faultseeding.submission.SubmissionReceivedEvent;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.Value;
import org.eclipse.jetty.websocket.api.Session;

public class SubmissionController extends JsonWebSocketController {
    private static final String POST_FAULT_SEEDING_SUBMISSION = "post-fault-seeding-submission";

    private final SessionRegistry sessionRegistry;

    private final EventDispatcher eventDispatcher;

    @Inject
    public SubmissionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry, final EventDispatcher eventDispatcher) {
        super(gson, messageTransmitter);
        this.sessionRegistry = sessionRegistry;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnMessageHandler(POST_FAULT_SEEDING_SUBMISSION, IncomingSubmission.class, this::handleIncomingSubmission);
    }

    private void handleIncomingSubmission(final Session session, final IncomingSubmission incomingSubmission) {
        sessionRegistry.getIdForSession(session)
                .map(userId -> incomingSubmissionToSubmission(userId, incomingSubmission))
                .ifPresent(this::dispatchSubmission);
    }

    private Submission incomingSubmissionToSubmission(final String userId, final IncomingSubmission incomingSubmission) {
        return new Submission(incomingSubmission.id, incomingSubmission.taskId, userId, incomingSubmission.code);
    }

    private void dispatchSubmission(final Submission submission) {
        final SubmissionReceivedEvent event = new SubmissionReceivedEvent(submission);

        eventDispatcher.dispatchEvent(event);
    }

    @Value
    private static final class IncomingSubmission {
        private final String id;

        private final String taskId;

        private final String code;
    }
}
