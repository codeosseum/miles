package com.codeosseum.miles.challenge.configuration;

import com.codeosseum.miles.challenge.repository.ChallengeLoaderListener;
import com.codeosseum.miles.challenge.repository.ChallengeRepository;
import com.codeosseum.miles.challenge.repository.GitChallengeRepositoryImpl;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class ChallengeModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bind(ChallengeRepository.class).to(GitChallengeRepositoryImpl.class).in(Singleton.class);

        bind(ChallengeLoaderListener.class).asEagerSingleton();
    }
}
