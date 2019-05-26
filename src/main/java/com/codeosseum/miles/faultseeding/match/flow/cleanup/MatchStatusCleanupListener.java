package com.codeosseum.miles.faultseeding.match.flow.cleanup;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.codeosseum.miles.configuration.SelfConfig;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.scoring.FinalScore;
import com.codeosseum.miles.faultseeding.scoring.ScoringService;
import com.codeosseum.miles.match.MatchStatus;
import com.google.inject.Inject;
import lombok.Value;

import static com.codeosseum.miles.communication.Message.message;
import static com.codeosseum.miles.match.MatchStatus.UNSET_ID;
import static com.codeosseum.miles.match.MatchStatus.UNSET_JOIN_PASSOWRD;
import static com.codeosseum.miles.match.MatchStatus.UNSET_MODE;
import static com.codeosseum.miles.match.MatchStatus.UNSET_STAGE;

public class MatchStatusCleanupListener implements SignalConsumer {
    private final MatchStatus matchStatus;

    private final ScoringService scoringService;

    private final PushMessageToAresService messagingService;

    private final SelfConfig configuration;

    @Inject
    public MatchStatusCleanupListener(final MatchStatus matchStatus, final EventDispatcher eventDispatcher,
                                      final ScoringService scoringService, final PushMessageToAresService messagingService,
                                      final SelfConfig configuration) {
        this.matchStatus = matchStatus;
        this.scoringService = scoringService;
        this.messagingService = messagingService;
        this.configuration = configuration;

        eventDispatcher.registerConsumer(ScoreSentToClientsSignal.class, this);
    }

    @Override
    public void accept() {
        final String matchId = matchStatus.getId();

        resetMatchStatus();

        sendFinalScoreToAres(matchId);
    }

    private void resetMatchStatus() {
        matchStatus.setCurrentStage(UNSET_STAGE);
        matchStatus.setCurrentMode(UNSET_MODE);
        matchStatus.setId(UNSET_ID);
        matchStatus.setJoinPassword(UNSET_JOIN_PASSOWRD);
    }

    private void sendFinalScoreToAres(final String matchId) {
        messagingService.sendMessage(message(MatchOverPayload.IDENTIFIER, makeMatchOverPayload(matchId)));
    }

    private MatchOverPayload makeMatchOverPayload(final String matchId) {
        final FinalScore finalScore = scoringService.calculateFinalScore();

        return new MatchOverPayload(matchId, configuration.getIdentifier(), finalScore);
    }

    @Value
    private static final class MatchOverPayload {
        private static final String IDENTIFIER = "fault-seeding-match-over";

        private final String matchId;

        private final String serverId;

        private final FinalScore finalScore;
    }
}
