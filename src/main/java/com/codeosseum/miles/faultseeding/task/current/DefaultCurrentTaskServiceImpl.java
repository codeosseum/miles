package com.codeosseum.miles.faultseeding.task.current;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignal;
import com.codeosseum.miles.faultseeding.task.Task;
import com.codeosseum.miles.faultseeding.task.repository.TaskRepository;
import com.codeosseum.miles.util.math.Span;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.requireNonNull;

public class DefaultCurrentTaskServiceImpl implements CurrentTaskService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCurrentTaskServiceImpl.class);

    private static final int STEP_TASK_ATTEMPTS = 10;

    private final TaskRepository taskRepository;

    private final List<Task> previousTaskList;

    private Span difficultySpan;

    private Task currentTask;

    private AtomicBoolean beingStepped;

    @Inject
    public DefaultCurrentTaskServiceImpl(final TaskRepository taskRepository, final EventDispatcher eventDispatcher) {
        this.taskRepository = taskRepository;

        this.currentTask = null;
        this.beingStepped = new AtomicBoolean();
        this.difficultySpan = new Span(0, 0);
        this.previousTaskList = new CopyOnWriteArrayList<>();

        eventDispatcher.registerConsumer(MatchCommencingSignal.class, this::reset);
    }

    @Override
    public void setDifficultySpan(final Span difficultySpan) {
        this.difficultySpan = requireNonNull(difficultySpan);
    }

    @Override
    public void stepCurrentTask() {
        guardBeingStepped();

        final Task nextTask = findNextTask();

        currentTask = nextTask;
        previousTaskList.add(nextTask);

        beingStepped.set(false);
    }

    @Override
    public Task getCurrentTask() {
        return currentTask;
    }

    private void reset() {
        this.previousTaskList.clear();
    }

    private void guardBeingStepped() {
        if (!beingStepped.compareAndSet(false, true)) {
            throw new IllegalStateException("Already being stepped!");
        }
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
