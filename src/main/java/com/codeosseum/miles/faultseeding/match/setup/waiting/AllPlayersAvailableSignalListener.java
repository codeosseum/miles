package com.codeosseum.miles.faultseeding.match.setup.waiting;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.match.Constants;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignal;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.player.event.AllPlayersAvailableSignal;
import com.google.inject.Inject;

public class AllPlayersAvailableSignalListener implements SignalConsumer {
    private final EventDispatcher eventDispatcher;

    private final MatchStatus matchStatus;

    @Inject
    public AllPlayersAvailableSignalListener(final EventDispatcher eventDispatcher, final MatchStatus matchStatus) {
        this.eventDispatcher = eventDispatcher;
        this.matchStatus = matchStatus;

        eventDispatcher.registerConsumer(AllPlayersAvailableSignal.class, this);
    }

    @Override
    public void accept() {
        if (currentMatchIsFaultSeeding() && waitingForPlayers()) {
            matchStatus.setCurrentStage(Constants.Stage.MATCH_COMMENCING);

            eventDispatcher.dispatchEvent(new MatchCommencingSignal());
        }
    }

    private boolean currentMatchIsFaultSeeding() {
        return matchStatus.getCurrentMode().equals(Constants.MODE);
    }

    private boolean waitingForPlayers() {
        return matchStatus.getCurrentStage().equals(Constants.Stage.WAITING_FOR_PLAYERS);
    }
}
