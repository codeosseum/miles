package com.codeosseum.miles.faultseeding.challenge.stored;

import com.codeosseum.miles.mapping.Json;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.codeosseum.miles.faultseeding.challenge.stored.SolutionLoader.loadSolutionsFromDirectory;
import static java.util.Objects.requireNonNull;

class ChallengeLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(ChallengeLoader.class);

    private static final String ID_SEPARATOR = ":";
    private static final String SOLUTION_PATH_PREFIX = "solutions";
    private static final String MANIFEST_FILENAME = "manifest.json";
    private static final Path DESCRIPTION_PATH = Paths.get("README.md");
    private static final Path EVALUATOR_ENTRYPOINT_PATH = Paths.get("evaluator", "index.js");

    private final Path baseDirectory;

    private final Json json;

    static Challenge loadChallengeFromDirectory(final Path path, final Json json) throws IOException {
        final ChallengeLoader loader = new ChallengeLoader(requireNonNull(path), requireNonNull(json));

        return loader.load();
    }

    private ChallengeLoader(final Path baseDirectory, final Json json) {
        this.baseDirectory = baseDirectory;
        this.json = json;
    }

    private Challenge load() throws IOException {
        final String id = convertChallengePathToId();

        LOGGER.info("Loading challenge {}", id);

        final List<Solution> solutions = loadSolutionsFromDirectory(baseDirectory.resolve(SOLUTION_PATH_PREFIX), json);

        LOGGER.info("Loaded {} solutions for challenge {}", solutions.size(), id);

        final ChallengeManifest manifest = loadManifest(baseDirectory.resolve(MANIFEST_FILENAME));

        return Challenge.builder()
                .id(id)
                .title(manifest.title)
                .difficulty(manifest.difficulty)
                .descriptionPath(baseDirectory.resolve(DESCRIPTION_PATH).toString())
                .evaluatorEntrypointPath(baseDirectory.resolve(EVALUATOR_ENTRYPOINT_PATH).toString())
                .solutions(solutions)
                .build();
    }

    private String convertChallengePathToId() {
        final int count = baseDirectory.getNameCount();

        final String modeName = baseDirectory.getName(count - 2).toString();
        final String challengeName = baseDirectory.getName(count - 1).toString();

        return modeName + ID_SEPARATOR + challengeName;
    }

    private ChallengeManifest loadManifest(final Path manifestPath) throws IOException {
        return json.fromJson(manifestPath, ChallengeManifest.class);
    }

    @Value
    private static final class ChallengeManifest {
        private final int difficulty;

        private final String title;
    }
}
