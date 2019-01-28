package com.codeosseum.miles.faultseeding.challenge;

import com.codeosseum.miles.challenge.repository.ChallengesLoadedSignal;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.faultseeding.challenge.stored.StoredChallengeRepository;
import com.google.inject.Inject;

public class ChallengesLoadedListener implements SignalConsumer {
    private final StoredChallengeRepository storedChallengeRepository;

    @Inject
    public ChallengesLoadedListener(final EventDispatcher eventDispatcher, final StoredChallengeRepository storedChallengeRepository) {
        this.storedChallengeRepository = storedChallengeRepository;
        eventDispatcher.registerConsumer(ChallengesLoadedSignal.class, this);
    }

    @Override
    public void accept() {
        storedChallengeRepository.loadAllStoredChallenges();
    }
}
