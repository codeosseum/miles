package com.codeosseum.miles.faultseeding.task.repository;

import java.util.Optional;

import com.codeosseum.miles.faultseeding.task.Task;

public interface TaskRepository {
    Optional<Task> getTaskWithDifficulty(int minDifficulty, int maxDifficulty);
}
