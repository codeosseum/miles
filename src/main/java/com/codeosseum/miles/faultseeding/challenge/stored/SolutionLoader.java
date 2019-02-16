package com.codeosseum.miles.faultseeding.challenge.stored;

import com.codeosseum.miles.mapping.Json;
import com.google.common.io.MoreFiles;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import static com.codeosseum.miles.faultseeding.challenge.stored.Solution.ID_SEPARATOR;
import static java.util.Objects.requireNonNull;

class SolutionLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(SolutionLoader.class);

    private static final String MANIFEST_FILENAME = "manifest.json";
    private static final String ENTRYPOINT_FILENAME = "index.js";

    private final Path baseDirectory;

    private final Json json;

    static List<Solution> loadSolutionsFromDirectory(final Path path, final Json json) {
        final SolutionLoader loader = new SolutionLoader(requireNonNull(path), requireNonNull(json));

        return loader.load();
    }

    private SolutionLoader(final Path baseDirectory, final Json json) {
        this.baseDirectory = baseDirectory;
        this.json = json;
    }

    private List<Solution> load() {
        // TODO: Quite ugly, consider throwing instead.
        final List<Solution> result = new LinkedList<>();

        for (final Path pathToSolution : listSolutions(baseDirectory)) {
            try {
                result.add(loadSingleSolution(pathToSolution));
            } catch (IOException ioe) {
                LOGGER.warn("Could not load solution at {}", pathToSolution);
            }
        }

        return result;
    }

    private Solution loadSingleSolution(final Path solutionPath) throws IOException {
        final String id = convertSolutionPathToId(solutionPath);

        LOGGER.info("Loading solution with id {}", id);

        final int difficulty = loadManifest(solutionPath.resolve(MANIFEST_FILENAME)).getDifficulty();

        final String entrypointPath = solutionPath.resolve(ENTRYPOINT_FILENAME).toString();

        return Solution.builder()
                .id(id)
                .difficulty(difficulty)
                .entrypointPath(entrypointPath)
                .build();
    }

    private String convertSolutionPathToId(final Path solutionPath) {
        final int count = solutionPath.getNameCount();

        final String modeName = solutionPath.getName(count - 4).toString();
        final String challengeName = solutionPath.getName(count - 3).toString();
        final String solutionHash = solutionPath.getName(count - 1).toString();

        return modeName + ID_SEPARATOR + challengeName + ID_SEPARATOR + solutionHash;
    }

    private SolutionManifest loadManifest(final Path manifestPath) throws IOException {
        return json.fromJson(manifestPath, SolutionManifest.class);
    }

    private List<Path> listSolutions(final Path baseDirectory) {
        // TODO: Throw exception
        try {
            return new ArrayList<>(MoreFiles.listFiles(baseDirectory));
        } catch (final IOException ioe) {
            return Collections.emptyList();
        }
    }

    @Value
    private static final class SolutionManifest {
        private final int difficulty;
    }
}
