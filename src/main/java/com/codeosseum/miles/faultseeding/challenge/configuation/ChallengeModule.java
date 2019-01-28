package com.codeosseum.miles.faultseeding.challenge.configuation;

import com.codeosseum.miles.challenge.repository.ChallengeRepository;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.challenge.ChallengesLoadedListener;
import com.codeosseum.miles.faultseeding.challenge.stored.DefaultStoredChallengeRepositoryImpl;
import com.codeosseum.miles.faultseeding.challenge.stored.StoredChallengeRepository;
import com.codeosseum.miles.mapping.Json;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class ChallengeModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class, Json.class, ChallengeRepository.class);
    }

    @Override
    protected void configureModule() {
        bind(StoredChallengeRepository.class).to(DefaultStoredChallengeRepositoryImpl.class).in(Singleton.class);

        bind(ChallengesLoadedListener.class).asEagerSingleton();
    }
}
