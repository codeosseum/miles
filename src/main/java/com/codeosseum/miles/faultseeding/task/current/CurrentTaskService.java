package com.codeosseum.miles.faultseeding.task.current;

import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.util.math.Span;

public interface CurrentTaskService {
    void setDifficultySpan(Span difficultySpan);

    void stepCurrentTask();

    Task getCurrentTask();
}
