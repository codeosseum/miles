package com.codeosseum.miles.challenge.repository;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ProgressMonitor;
import org.eclipse.jgit.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;

public class GitChallengeRepositoryImpl implements ChallengeRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitChallengeRepositoryImpl.class);

    // TODO: Read from configuration.
    private static final String REPOSITORY_REMOTE_URL = "https://github.com/codeosseum/quaestiones.git";

    // TODO: Read from configuration.
    private static final String REPOSITORY_LOCAL_PATH = "challenges";

    @Override
    public List<String> findAllChallengesForMode(final String mode) {
        return Collections.emptyList();
    }

    void loadChallenges() throws CouldNotLoadChallengesException {
        // TODO: Make easier to test.
        try {
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
        } catch (Exception e) {
            throw new CouldNotLoadChallengesException(e);
        }
    }
}
