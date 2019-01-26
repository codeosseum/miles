package com.codeosseum.miles.challenge.repository;

import com.codeosseum.miles.StartupSignal;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChallengeLoaderListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeLoaderListener.class);

    private final EventDispatcher eventDispatcher;

    private final GitChallengeRepositoryImpl challengeRepository;

    @Inject
    public ChallengeLoaderListener(final EventDispatcher eventDispatcher, final GitChallengeRepositoryImpl challengeRepository) {
        this.eventDispatcher = eventDispatcher;
        this.challengeRepository = challengeRepository;

        eventDispatcher.registerConsumer(StartupSignal.class, this::onStartup);
    }

    private void onStartup() {
        try {
            challengeRepository.loadChallenges();
        } catch (Exception e) {
            // TODO: Better error handling
            LOGGER.error("Could not load git repository", e);
        }
    }
}
