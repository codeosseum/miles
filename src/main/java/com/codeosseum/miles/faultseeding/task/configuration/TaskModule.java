package com.codeosseum.miles.faultseeding.task.configuration;

import java.util.List;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.challenge.stored.StoredChallengeRepository;
import com.codeosseum.miles.faultseeding.task.current.CurrentTaskService;
import com.codeosseum.miles.faultseeding.task.current.DefaultCurrentTaskServiceImpl;
import com.codeosseum.miles.faultseeding.task.repository.ChallengesLoadedListener;
import com.codeosseum.miles.faultseeding.task.repository.DefaultTaskRepositoryImpl;
import com.codeosseum.miles.faultseeding.task.repository.TaskRepository;
import com.codeosseum.miles.util.inject.MilesModule;

import static java.util.Arrays.asList;

public class TaskModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class, StoredChallengeRepository.class);
    }

    @Override
    protected void configureModule() {
        bindEagerSingleton(ChallengesLoadedListener.class);

        bindSingleton(TaskRepository.class, DefaultTaskRepositoryImpl.class);

        bindSingleton(CurrentTaskService.class, DefaultCurrentTaskServiceImpl.class);
    }
}
