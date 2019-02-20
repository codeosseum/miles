package com.codeosseum.miles.faultseeding.match.flow.cleanup;

import java.util.List;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageToClientService;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.scoring.Position;
import com.codeosseum.miles.faultseeding.scoring.ScoringService;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.google.inject.Inject;
import lombok.Value;

public class FinalScoreSendingListener implements SignalConsumer {
    private final ScoringService scoringService;

    private final PresentPlayerRegistry playerRegistry;

    private final PushMessageToClientService messagingService;

    @Inject
    public FinalScoreSendingListener(final ScoringService scoringService, final PushMessageToClientService messagingService,
                                     final EventDispatcher eventDispatcher, final PresentPlayerRegistry playerRegistry) {
        this.scoringService = scoringService;
        this.messagingService = messagingService;
        this.playerRegistry = playerRegistry;

        eventDispatcher.registerConsumer(MatchOverSignal.class, this);
    }

    @Override
    public void accept() {
        final List<Position> ranking = scoringService.getRanking();

        final Message<MatchOverPayload> matchOverMessage = Message.message(MatchOverPayload.ACTION, makeMatchOverPayload(ranking));

        playerRegistry.getAllPlayers()
                .forEach(playerId -> messagingService.sendMessage(playerId, matchOverMessage));
    }

    private MatchOverPayload makeMatchOverPayload(final List<Position> ranking) {
        final List<String> winners = ranking.get(0).getPlayers();

        final String result = winners.size() == 1 ? MatchOverPayload.WIN : MatchOverPayload.DRAW;

        return new MatchOverPayload(winners, ranking, result);
    }

    @Value
    private static final class MatchOverPayload {
        private static final String ACTION = "fault-seeding-match-cleanup";

        private static final String WIN = "win";

        private static final String DRAW = "draw";

        private final List<String> winners;

        private final List<Position> ranking;

        private final String result;
    }
}
