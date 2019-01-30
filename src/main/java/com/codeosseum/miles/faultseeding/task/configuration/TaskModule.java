package com.codeosseum.miles.faultseeding.task.configuration;

import java.util.List;

import com.codeosseum.miles.faultseeding.challenge.stored.StoredChallengeRepository;
import com.codeosseum.miles.faultseeding.task.repository.DefaultTaskRepositoryImpl;
import com.codeosseum.miles.faultseeding.task.repository.TaskRepository;
import com.codeosseum.miles.util.inject.MilesModule;

import static java.util.Arrays.asList;

public class TaskModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(StoredChallengeRepository.class);
    }

    @Override
    protected void configureModule() {
        bindEagerSingleton(TaskRepository.class, DefaultTaskRepositoryImpl.class);
    }
}
