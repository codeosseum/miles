package com.codeosseum.miles.faultseeding.challenge.stored;

import com.codeosseum.miles.challenge.repository.ChallengeRepository;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class DefaultStoredChallengeRepositoryImpl implements StoredChallengeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStoredChallengeRepositoryImpl.class);

    private static final String MODE = "TWO_PLAYER_FAULT_SEEDING";

    private final ChallengeRepository challengeRepository;

    @Inject
    public DefaultStoredChallengeRepositoryImpl(final ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    public List<Challenge> loadAllStoredChallenges() {
        return challengeRepository.findAllChallengesForMode(MODE).stream()
            .map(this::loadChallenge)
            .collect(Collectors.toList());
    }

    private Challenge loadChallenge(final String path) {
        LOGGER.info("Loading stored challenge: {}", path);

        return null;
    }
}
