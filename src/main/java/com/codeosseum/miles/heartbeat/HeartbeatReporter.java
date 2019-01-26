package com.codeosseum.miles.heartbeat;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.registration.ServerRegisteredSignal;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.codeosseum.miles.communication.Message.message;

public class HeartbeatReporter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatReporter.class);

    // TODO: read from configuration
    private static final String SERVER_IDENTIFIER = "server-01";

    private final EventDispatcher eventDispatcher;

    private final PushMessageToAresService pushMessageService;

    private final AtomicBoolean shouldSendMessage;

    @Inject
    public HeartbeatReporter(final EventDispatcher eventDispatcher, final PushMessageToAresService pushMessageService) {
        this.eventDispatcher = eventDispatcher;
        this.pushMessageService = pushMessageService;

        eventDispatcher.registerConsumer(ServerRegisteredSignal.class, this::consumeServerRegisteredSignal);
        eventDispatcher.registerConsumer(HeartbeatSignal.class, this::consumeHeartbeatSignal);

        this.shouldSendMessage = new AtomicBoolean(false);
    }

    private void consumeServerRegisteredSignal() {
        shouldSendMessage.set(true);
    }

    private void consumeHeartbeatSignal() {
        if (shouldSendMessage.get()) {
            LOGGER.info("Sending heartbeat event to Ares");

            pushMessageService.sendMessage(message(HeartbeatEvent.IDENTIFIER, new HeartbeatEvent(SERVER_IDENTIFIER)));
        }
    }
}
