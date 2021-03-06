package com.codeosseum.miles.challenge.configuration;

import com.codeosseum.miles.challenge.repository.ChallengeLoaderListener;
import com.codeosseum.miles.challenge.repository.ChallengeRepository;
import com.codeosseum.miles.challenge.repository.GitChallengeRepositoryImpl;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.mapping.Json;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class ChallengeModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class, Json.class);
    }

    @Override
    protected void configureModule() {
        bindSingleton(ChallengeRepository.class, GitChallengeRepositoryImpl.class);

        bindEagerSingleton(ChallengeLoaderListener.class);
    }
}
