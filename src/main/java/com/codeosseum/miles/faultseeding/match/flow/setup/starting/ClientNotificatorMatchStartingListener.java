package com.codeosseum.miles.faultseeding.match.flow.setup.starting;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;

public class ClientNotificatorMatchStartingListener implements SignalConsumer {
    private static final String FAULT_SEEDING_MATCH_STARTING = "fault-seeding-match-starting";

    private final PresentPlayerRegistry playerRegistry;

    private final PushMessageToClientService messageService;

    @Inject
    public ClientNotificatorMatchStartingListener(final PresentPlayerRegistry playerRegistry, final PushMessageToClientService messageService, final EventDispatcher eventDispatcher) {
        this.playerRegistry = playerRegistry;
        this.messageService = messageService;

        eventDispatcher.registerConsumer(MatchStartingSignal.class, this);
    }

    @Override
    public void accept() {
        final Message<Void> message = Message.emptyMessage(FAULT_SEEDING_MATCH_STARTING);

        playerRegistry.getAllPlayers().forEach(userId -> messageService.sendMessage(userId, message));
    }
}
