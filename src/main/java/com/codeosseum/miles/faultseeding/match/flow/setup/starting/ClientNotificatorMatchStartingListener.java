package com.codeosseum.miles.faultseeding.match.flow.setup.starting;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.configuration.FaultSeedingConfig;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import lombok.Value;

public class ClientNotificatorMatchStartingListener implements SignalConsumer {
    private final PresentPlayerRegistry playerRegistry;

    private final PushMessageToClientService messageService;

    private final FaultSeedingConfig configuration;

    @Inject
    public ClientNotificatorMatchStartingListener(final PresentPlayerRegistry playerRegistry, final PushMessageToClientService messageService,
                                                  final EventDispatcher eventDispatcher, final FaultSeedingConfig configuration) {
        this.playerRegistry = playerRegistry;
        this.messageService = messageService;
        this.configuration = configuration;

        eventDispatcher.registerConsumer(MatchStartingSignal.class, this);
    }

    @Override
    public void accept() {
        final MatchStartingPayload payload = new MatchStartingPayload(configuration.getRuntimeSeconds());

        final Message<MatchStartingPayload> message = Message.message(MatchStartingPayload.FAULT_SEEDING_MATCH_STARTING, payload);

        playerRegistry.getAllPlayers().forEach(userId -> messageService.sendMessage(userId, message));
    }

    @Value
    private static final class MatchStartingPayload {
        private static final String FAULT_SEEDING_MATCH_STARTING = "fault-seeding-match-starting";

        private final int runtime;
    }
}
