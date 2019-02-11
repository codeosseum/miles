package com.codeosseum.miles.faultseeding.task.repository;

import java.util.Optional;

import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.util.math.Span;

public interface TaskRepository {
    Optional<Task> getTaskWithDifficulty(Span difficultySpan);
}
