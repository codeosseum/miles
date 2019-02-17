package com.codeosseum.miles.faultseeding.scoring;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignal;
import com.codeosseum.miles.faultseeding.submission.SubmissionEvaluatedEvent;
import com.codeosseum.miles.faultseeding.submission.SubmissionResult;
import com.codeosseum.miles.faultseeding.submission.evaluation.EvaluationResult;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.google.inject.Inject;

import static java.util.Collections.unmodifiableMap;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

public class DefaultScoringServiceImpl implements ScoringService {
    private final int SUCCESSFUL_SUBMISSION_SCORE = 10;

    private final RegisteredPlayerRegistry playerRegistry;

    private final Map<String, Integer> scoreMap;

    @Inject
    public DefaultScoringServiceImpl(final RegisteredPlayerRegistry playerRegistry, final EventDispatcher eventDispatcher) {
        this.playerRegistry = playerRegistry;

        eventDispatcher.registerConsumer(MatchCommencingSignal.class, this::onMatchCommencing);

        this.scoreMap = new ConcurrentHashMap<>();
    }

    @Override
    public Map<String, Integer> getScores() {
        return unmodifiableMap(scoreMap);
    }

    @Override
    public List<Position> getRanking() {
        return scoreMap.entrySet().stream()
                .map(entry -> new Position(entry.getKey(), entry.getValue()))
                .sorted(comparing(Position::getScore))
                .collect(toList());
    }

    @Override
    public void scoreSubmission(final SubmissionResult submissionResult) {
        if (isFaultFound(submissionResult)) {
            scoreMap.compute(submissionResult.getSubmission().getUserId(),
                    (userId, oldScore) -> oldScore + SUCCESSFUL_SUBMISSION_SCORE);
        }
    }

    void onMatchCommencing() {
        scoreMap.clear();

        playerRegistry.getAllPlayers()
                .forEach(username -> scoreMap.put(username, 0));
    }

    private boolean isFaultFound(final SubmissionResult submissionResult) {
        return isEvaluationSuccessful(submissionResult) && isDifferentOutputs(submissionResult);
    }

    private boolean isEvaluationSuccessful(final SubmissionResult result) {
        return result.getStatus().equals(SubmissionResult.Status.SUCCESSFUL_EVALUATION);
    }

    private boolean isDifferentOutputs(final SubmissionResult result) {
        return result.getEvaluationResult().getStatus().equals(EvaluationResult.Status.DIFFERENT_ACTUAL_AND_EXPECTED);
    }
}
