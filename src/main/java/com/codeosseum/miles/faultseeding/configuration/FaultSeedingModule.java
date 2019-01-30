package com.codeosseum.miles.faultseeding.configuration;

import com.codeosseum.miles.faultseeding.challenge.configuation.ChallengeModule;
import com.codeosseum.miles.faultseeding.registration.MatchRegistrationController;
import com.codeosseum.miles.faultseeding.submission.configuration.SubmissionModule;
import com.codeosseum.miles.faultseeding.task.configuration.TaskModule;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;
import com.google.inject.Module;

import java.util.List;

import static java.util.Arrays.asList;

public class FaultSeedingModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class);
    }

    @Override
    protected List<Module> installs() {
        return asList(
                new ChallengeModule(),
                new SubmissionModule(),
                new TaskModule());
    }

    @Override
    protected void configureModule() {
        bind(MatchRegistrationController.class).asEagerSingleton();
    }
}
