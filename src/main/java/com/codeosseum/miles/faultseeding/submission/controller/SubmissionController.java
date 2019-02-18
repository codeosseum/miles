package com.codeosseum.miles.faultseeding.submission.controller;

import java.util.Optional;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.communication.websocket.session.SessionRegistry;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.submission.Submission;
import com.codeosseum.miles.faultseeding.submission.SubmissionReceivedEvent;
import com.codeosseum.miles.match.MatchStatus;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.Value;
import org.eclipse.jetty.websocket.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubmissionController extends JsonWebSocketController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmissionController.class);

    private static final String POST_FAULT_SEEDING_SUBMISSION = "post-fault-seeding-submission";

    private static final String SUBMISSION_NOT_ACCEPTED = "fault-seeding-submission-not-accepted";

    private final SessionRegistry sessionRegistry;

    private final MatchStatus matchStatus;

    private final MessageTransmitter messageTransmitter;

    private final EventDispatcher eventDispatcher;

    @Inject
    public SubmissionController(final Gson gson, final MessageTransmitter messageTransmitter, final SessionRegistry sessionRegistry,
                                final MatchStatus matchStatus, final MessageTransmitter messageTransmitter1, final EventDispatcher eventDispatcher) {
        super(gson, messageTransmitter);
        this.sessionRegistry = sessionRegistry;
        this.matchStatus = matchStatus;
        this.messageTransmitter = messageTransmitter1;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void attach(final WebSocketDispatcher dispatcher) {
        dispatcher.attachOnMessageHandler(POST_FAULT_SEEDING_SUBMISSION, IncomingSubmission.class, this::handleIncomingSubmission);
    }

    private void handleIncomingSubmission(final Session session, final IncomingSubmission incomingSubmission) {
        final Optional<String> playerId = sessionRegistry.getIdForSession(session);

        if (playerId.isPresent() && matchInProgress()) {
            final Submission submission = incomingSubmissionToSubmission(playerId.get(), incomingSubmission);

            dispatchSubmission(submission);
        } else {
            sendNotAcceptedMessage(session);
        }
    }

    private void sendNotAcceptedMessage(final Session session) {
        try {
            messageTransmitter.writeMessage(session, SUBMISSION_NOT_ACCEPTED, null);
        } catch (final Exception e) {
            LOGGER.warn(e.toString());
        }
    }

    private Submission incomingSubmissionToSubmission(final String userId, final IncomingSubmission incomingSubmission) {
        return new Submission(incomingSubmission.id, incomingSubmission.taskId, userId, incomingSubmission.code);
    }

    private void dispatchSubmission(final Submission submission) {
        final SubmissionReceivedEvent event = new SubmissionReceivedEvent(submission);

        eventDispatcher.dispatchEvent(event);
    }

    private boolean matchInProgress() {
        return matchStatus.getCurrentStage().equals(Constants.Stage.IN_PROGRESS);
    }

    @Value
    private static final class IncomingSubmission {
        private final String id;

        private final String taskId;

        private final String code;
    }
}
