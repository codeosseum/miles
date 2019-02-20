package com.codeosseum.miles.match.setup.starting;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.flow.setup.starting.MatchStartingSignal;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;
import lombok.Value;

import static com.codeosseum.miles.communication.Message.message;

public class AresNotificatorMatchStartingListener {
    private final PushMessageToAresService messagingService;

    private final MatchStatus matchStatus;

    @Inject
    public AresNotificatorMatchStartingListener(final EventDispatcher eventDispatcher, final PushMessageToAresService messagingService, final MatchStatus matchStatus) {
        this.messagingService = messagingService;
        this.matchStatus = matchStatus;
        eventDispatcher.registerConsumer(MatchStartingSignal.class, this::onFaultSeedingMatchStarting);
    }

    void onFaultSeedingMatchStarting() {
        final String matchId = matchStatus.getId();

        final MatchStartingEvent event = new MatchStartingEvent(matchId);

        messagingService.sendMessage(message(MatchStartingEvent.IDENTIFIER, event));
    }

    @Value
    private static final class MatchStartingEvent {
        private static final String IDENTIFIER = "match-starting";

        private final String id;
    }
}
