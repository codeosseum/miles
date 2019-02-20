package com.codeosseum.miles.faultseeding.match.flow.cleanup;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.scoring.FinalScore;
import com.codeosseum.miles.faultseeding.scoring.ScoringService;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import lombok.Value;

public class ClientNotificatorFinalScoreSendingListener implements SignalConsumer {
    private final ScoringService scoringService;

    private final PresentPlayerRegistry playerRegistry;

    private final PushMessageToClientService messagingService;

    @Inject
    public ClientNotificatorFinalScoreSendingListener(final ScoringService scoringService, final PushMessageToClientService messagingService,
                                                      final EventDispatcher eventDispatcher, final PresentPlayerRegistry playerRegistry) {
        this.scoringService = scoringService;
        this.messagingService = messagingService;
        this.playerRegistry = playerRegistry;

        eventDispatcher.registerConsumer(MatchOverSignal.class, this);
    }

    @Override
    public void accept() {
        final Message<MatchOverPayload> matchOverMessage = Message.message(MatchOverPayload.ACTION, makeMatchOverPayload());

        playerRegistry.getAllPlayers()
                .forEach(playerId -> messagingService.sendMessage(playerId, matchOverMessage));
    }

    private MatchOverPayload makeMatchOverPayload() {
        final FinalScore finalScore = scoringService.calculateFinalScore();

        return new MatchOverPayload(finalScore);
    }

    @Value
    private static final class MatchOverPayload {
        private static final String ACTION = "fault-seeding-match-over";

        private final FinalScore finalScore;
    }
}
