package com.codeosseum.miles.faultseeding.configuration;

import com.codeosseum.miles.faultseeding.registration.MatchRegistrationController;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

import java.util.List;

public class FaultSeedingModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return super.requires();
    }

    @Override
    protected void configureModule() {
        bind(MatchRegistrationController.class).asEagerSingleton();
    }
}
