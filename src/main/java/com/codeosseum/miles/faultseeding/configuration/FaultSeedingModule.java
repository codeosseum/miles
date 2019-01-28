package com.codeosseum.miles.faultseeding.configuration;

import com.codeosseum.miles.faultseeding.challenge.configuation.ChallengeModule;
import com.codeosseum.miles.faultseeding.registration.MatchRegistrationController;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Module;

import java.util.List;

import static java.util.Arrays.asList;

public class FaultSeedingModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return super.requires();
    }

    @Override
    protected List<Module> installs() {
        return asList(new ChallengeModule());
    }

    @Override
    protected void configureModule() {
        bind(MatchRegistrationController.class).asEagerSingleton();
    }
}
