package com.codeosseum.miles.challenge.repository;

import com.codeosseum.miles.mapping.Json;
import com.google.common.io.MoreFiles;
import com.google.inject.Inject;
import lombok.Value;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GitChallengeRepositoryImpl implements ChallengeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitChallengeRepositoryImpl.class);

    // TODO: Read from configuration.
    private static final String REPOSITORY_REMOTE_URL = "https://github.com/codeosseum/quaestiones.git";

    // TODO: Read from configuration.
    private static final String REPOSITORY_LOCAL_PATH = "challenges";

    private static final Path TRANSLATION_FILE_PATH = Paths.get(REPOSITORY_LOCAL_PATH, "src", "challenges", "modes.json");

    private Map<String, String> translationMap;

    private final Json json;

    @Inject
    public GitChallengeRepositoryImpl(final Json json) {
        this.json = json;
    }

    @Override
    public List<String> findAllChallengesForMode(final String mode) {
        // TODO: Throw exception (see loadChallengesForMode)
        return Optional.ofNullable(translationMap.get(mode))
                .map(this::loadChallengesForMode)
                .map(list -> {
                    LOGGER.info("Found {} challenges for mode {}", list.size(), mode);
                    LOGGER.debug("{}", list);

                    return list;
                })
                .orElseGet(Collections::emptyList);
    }

    void loadChallenges() throws CouldNotLoadChallengesException {
        try {
            cloneRepository();

            translationMap = loadTranslations();

            LOGGER.info("Loaded challenge translation map {}", translationMap);
        } catch (Exception e) {
            throw new CouldNotLoadChallengesException(e);
        }
    }

    private List<String> loadChallengesForMode(final String mode) {
        // TODO: Throw exception
        try {
            return MoreFiles.listFiles(Paths.get(REPOSITORY_LOCAL_PATH, "src", "challenges", mode))
                    .stream()
                    .map(Path::toString)
                    .collect(Collectors.toList());
        } catch (final IOException ioe) {
            return Collections.emptyList();
        }
    }

    private void cloneRepository() throws IOException, GitAPIException {
        // TODO: Make easier to test.
        final Path localPath = Paths.get(REPOSITORY_LOCAL_PATH);

        if (Files.exists(localPath)) {
            FileUtils.delete(localPath.toFile(), FileUtils.RECURSIVE);
        }

        LOGGER.info("Loading challenge git repository from remote: {}", REPOSITORY_REMOTE_URL);

        try (Git result = Git.cloneRepository()
                .setURI(REPOSITORY_REMOTE_URL)
                .setDirectory(new File(REPOSITORY_LOCAL_PATH))
                .call()) {
            LOGGER.info("Done cloning challenge repository.");
        }
    }

    private Map<String, String> loadTranslations() throws IOException{
        return json.fromJson(TRANSLATION_FILE_PATH, Modes.class).getModes();
    }

    @Value
    private static final class Modes {
        private final Map<String, String> modes;
    }
}
