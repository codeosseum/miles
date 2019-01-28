package com.codeosseum.miles.faultseeding.challenge.stored;

import com.codeosseum.miles.challenge.repository.ChallengeRepository;
import com.codeosseum.miles.mapping.Json;
import com.google.common.io.MoreFiles;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DefaultStoredChallengeRepositoryImpl implements StoredChallengeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultStoredChallengeRepositoryImpl.class);

    private static final String MODE = "TWO_PLAYER_FAULT_SEEDING";

    private final ChallengeRepository challengeRepository;

    private final Json json;

    @Inject
    public DefaultStoredChallengeRepositoryImpl(final ChallengeRepository challengeRepository, final Json json) {
        this.challengeRepository = challengeRepository;
        this.json = json;
    }

    @Override
    public List<Challenge> loadAllStoredChallenges() {
        // TODO: Quite ugly, consider throwing instead.
        final List<Challenge> result = new LinkedList<>();

        for (final String pathToChallenge : challengeRepository.findAllChallengesForMode(MODE)) {
            try {
                result.add(ChallengeLoader.loadChallengeFromDirectory(Paths.get(pathToChallenge), json));
            } catch (IOException ioe) {
                LOGGER.warn("Could not load challenge at {}", pathToChallenge);
            }
        }

        return result;
    }

    private List<Path> listSolutions(final Path baseDirectory) {
        // TODO: Throw exception
        try {
            return new ArrayList<>(MoreFiles.listFiles(baseDirectory));
        } catch (final IOException ioe) {
            return Collections.emptyList();
        }
    }
}
