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
import com.google.inject.Inject;
import lombok.Value;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class DefaultTaskRepositoryImpl implements TaskRepository {
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
    public Optional<Task> getTaskWithDifficulty(final int minDifficulty, final int maxDifficulty) {
        return findDifficultySpan(minDifficulty, maxDifficulty)
                .flatMap(this::randomTaskInSpan);
    }

    public void reloadChallenges() {
        final List<TaskSeed> newSeeds = getAllStoredTaskSeeds();

        taskSeeds.clear();

        taskSeeds.addAll(newSeeds);
    }

    private Optional<Span> findDifficultySpan(final int minDifficulty, final int maxDifficulty) {
        final Optional<Integer> minIndexOptional = findFirstSeedIndexWithDifficulty(minDifficulty);
        final Optional<Integer> maxIndexOptional = findFirstSeedIndexWithDifficulty(maxDifficulty);

        if (minIndexOptional.isPresent() && maxIndexOptional.isPresent()) {
            final int min = minIndexOptional.get();
            final int max = maxIndexOptional.get();

            if (min > max) {
                return Optional.empty();
            }

            return Optional.of(new Span(min, max));
        } else {
            return Optional.empty();
        }
    }

    private Optional<Task> randomTaskInSpan(final Span difficultySpan) {
        final TaskSeed seed = randomTaskSeedBetween(difficultySpan.lower, difficultySpan.uppter);

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
        final String description = readFileContents(seed.getChallenge().getDescriptionPath());
        final String evaluator = readFileContents(seed.getChallenge().getEvaluatorEntrypointPath());
        final String solution = readFileContents(seed.getSolution().getEntrypointPath());

        return Task.builder()
                .challengeId(seed.getChallenge().getId())
                .solutionId(seed.getSolution().getId())
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

    private Optional<Integer> findFirstSeedIndexWithDifficulty(final int difficulty) {
        final int index = Collections.binarySearch(taskSeeds, searchSeed(difficulty), comparing(TaskSeed::getDifficulty));

        if (index < 0) {
            return Optional.empty();
        } else {
            return Optional.of(index);
        }
    }

    private TaskSeed searchSeed(final int difficulty) {
        return new TaskSeed(null, null, difficulty);
    }

    private String readFileContents(final String path) throws IOException {
        final byte[] rawContents = Files.readAllBytes(Paths.get(path));

        return new String(rawContents, Charset.defaultCharset());
    }

    @Value
    private static final class Span {
        private final int lower;

        private final int uppter;
    }

    @Value
    private static final class TaskSeed {
        private final Challenge challenge;

        private final Solution solution;

        private final int difficulty;
    }
}
