package com.codeosseum.miles.faultseeding.task.repository;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

import com.codeosseum.miles.faultseeding.challenge.stored.Challenge;
import com.codeosseum.miles.faultseeding.challenge.stored.Solution;
import com.codeosseum.miles.faultseeding.challenge.stored.StoredChallengeRepository;
import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.util.math.Span;
import com.google.inject.Inject;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class DefaultTaskRepositoryImpl implements TaskRepository {
    private final Logger LOGGER = LoggerFactory.getLogger(DefaultTaskRepositoryImpl.class);

    private final StoredChallengeRepository challengeRepository;

    private final Random random;

    private final List<TaskSeed> taskSeeds;

    @Inject
    public DefaultTaskRepositoryImpl(final StoredChallengeRepository challengeRepository, final Random random) {
        this.challengeRepository = challengeRepository;
        this.random = random;
        this.taskSeeds = new CopyOnWriteArrayList<>();
    }

    @Override
    public Optional<Task> getTaskWithDifficulty(final Span difficultySpan) {
        LOGGER.info("Searching for a random task in difficulty range: {}", difficultySpan);

        return findDifficultySpan(requireNonNull(difficultySpan))
                .flatMap(this::randomTaskInSpan);
    }

    public void reloadTasks() {
        LOGGER.info("Reloading tasks");

        final List<TaskSeed> newSeeds = getAllStoredTaskSeeds();

        LOGGER.info("{} task seeds created", newSeeds.size());

        taskSeeds.clear();

        taskSeeds.addAll(newSeeds);
    }

    private Optional<Span> findDifficultySpan(final Span span) {
        final Optional<Integer> minIndexOptional = findFirstWithDifficultyAtLeast(span.getLower());
        final Optional<Integer> maxIndexOptional = findFirstWithDifficultyAtMost(span.getUpper());

        if (minIndexOptional.isPresent() && maxIndexOptional.isPresent()) {
            final int min = minIndexOptional.get();
            final int max = maxIndexOptional.get();

            if (min > max) {
                return Optional.empty();
            }

            LOGGER.info("Number of candidate tasks: {}", max - min + 1);

            return Optional.of(new Span(min, max));
        } else {
            LOGGER.info("No task found in the requested difficulty span.");

            return Optional.empty();
        }
    }

    private Optional<Task> randomTaskInSpan(final Span difficultySpan) {
        final TaskSeed seed = randomTaskSeedBetween(difficultySpan.getLower(), difficultySpan.getUpper());

        try {
            return Optional.of(taskFromSeed(seed));
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    private TaskSeed randomTaskSeedBetween(final int minIndex, final int maxIndex) {
        return taskSeeds.get(randomIntegerBetween(minIndex, maxIndex));
    }

    private int randomIntegerBetween(final int min, final int max) {
        return random.nextInt(max + 1 - min) + min;
    }

    private Task taskFromSeed(final TaskSeed seed) throws IOException {
        LOGGER.info("Creating task from seed: {} - {}", seed.getChallenge().getId(), seed.getSolution().getId());

        final String description = readFileContents(seed.getChallenge().getDescriptionPath());
        final String evaluator = readFileContents(seed.getChallenge().getEvaluatorEntrypointPath());
        final String solution = readFileContents(seed.getSolution().getEntrypointPath());

        return Task.builder()
                .id(seed.getSolution().getId())
                .difficulty(seed.getDifficulty())
                .title(seed.getChallenge().getTitle())
                .description(description)
                .evaluatorEntrypoint(evaluator)
                .solutionEntrypoint(solution)
                .build();
    }

    private List<TaskSeed> getAllStoredTaskSeeds() {
        return challengeRepository.loadAllStoredChallenges().stream()
                .flatMap(this::seedsFromChallenge)
                .sorted(comparing(TaskSeed::getDifficulty))
                .collect(toList());
    }

    private Stream<TaskSeed> seedsFromChallenge(final Challenge challenge) {
        return challenge.getSolutions().stream()
                .map(solution -> new TaskSeed(challenge, solution, challenge.getDifficulty() + solution.getDifficulty()));
    }

    private Optional<Integer> findFirstWithDifficultyAtLeast(final int difficulty) {
        // TODO: This is O(n), use binary search instead.
        for (int i = 0; i < taskSeeds.size(); ++i) {
            if (taskSeeds.get(i).getDifficulty() >= difficulty) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    private Optional<Integer> findFirstWithDifficultyAtMost(final int difficulty) {
        // TODO: This is O(n), use binary search instead.
        for (int i = taskSeeds.size() - 1; i >= 0; --i) {
            if (taskSeeds.get(i).getDifficulty() <= difficulty) {
                return Optional.of(i);
            }
        }

        return Optional.empty();
    }

    private TaskSeed searchSeed(final int difficulty) {
        return new TaskSeed(null, null, difficulty);
    }

    private String readFileContents(final String path) throws IOException {
        LOGGER.debug("Read file {}", path);

        final byte[] rawContents = Files.readAllBytes(Paths.get(path));

        return new String(rawContents, Charset.defaultCharset());
    }

    @Value
    private static final class TaskSeed {
        private final Challenge challenge;

        private final Solution solution;

        private final int difficulty;
    }
}
