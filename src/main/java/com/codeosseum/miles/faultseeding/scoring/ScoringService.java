package com.codeosseum.miles.faultseeding.scoring;

import java.util.List;
import java.util.Map;

import com.codeosseum.miles.faultseeding.submission.SubmissionResult;

public interface ScoringService {
    Map<String, Integer> getScores();

    List<Position> getRanking();

    void scoreSubmission(SubmissionResult submissionResult);
}
