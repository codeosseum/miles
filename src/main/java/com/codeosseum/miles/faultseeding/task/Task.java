package com.codeosseum.miles.faultseeding.task;

import com.codeosseum.miles.faultseeding.challenge.stored.Solution;
import lombok.Builder;
import lombok.Value;

import static com.codeosseum.miles.faultseeding.challenge.stored.Solution.CHALLENGE_NAME_INDEX;
import static com.codeosseum.miles.faultseeding.challenge.stored.Solution.HASH_INDEX;
import static com.codeosseum.miles.faultseeding.challenge.stored.Solution.ID_SEPARATOR;
import static com.codeosseum.miles.faultseeding.challenge.stored.Solution.MODE_INDEX;

@Value
@Builder
public class Task {
    private final String id;

    private final int difficulty;

    private final String title;

    private final String description;

    private final String evaluatorEntrypoint;

    private final String solutionEntrypoint;

    public String getModeId() {
        return id.split(ID_SEPARATOR)[MODE_INDEX];
    }

    public String getTaskName() {
        return id.split(ID_SEPARATOR)[CHALLENGE_NAME_INDEX];
    }

    public String getSolutionHash() {
        return id.split(ID_SEPARATOR)[HASH_INDEX];
    }
}
