package com.codeosseum.miles.faultseeding.task.current;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.faultseeding.task.repository.TaskRepository;
import com.codeosseum.miles.util.math.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class DefaultCurrentTaskServiceImpl implements CurrentTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCurrentTaskServiceImpl.class);

    private static final int STEP_TASK_ATTEMPTS = 10;

    private final TaskRepository taskRepository;

    private final List<Task> previousTaskList;

    private Span difficultySpan;

    private AtomicReference<Task> currentTask;

    public DefaultCurrentTaskServiceImpl(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

        this.currentTask = new AtomicReference<>();
        this.difficultySpan = new Span(0, 0);
        this.previousTaskList = new ArrayList<>();
    }

    @Override
    public void setDifficultySpan(final Span difficultySpan) {
        this.difficultySpan = requireNonNull(difficultySpan);
    }

    @Override
    public void stepCurrentTask() {
        final Task nextTask = findNextTask();

        currentTask.set(nextTask);
        previousTaskList.add(nextTask);
    }

    @Override
    public Task getCurrentTask() {
        return currentTask.get();
    }

    public void reset() {
        this.previousTaskList.clear();
    }

    private Task findNextTask() {
        for (int attempts = 0; attempts < STEP_TASK_ATTEMPTS; ++attempts) {
            final Optional<Task> taskOptional = taskRepository.getTaskWithDifficulty(difficultySpan);

            if (!taskOptional.isPresent()) {
                break;
            } else if (!previousTaskList.contains(taskOptional.get())) {
                return taskOptional.get();
            }
        }

        throw new IllegalStateException("Could not find the next task");
    }
}
