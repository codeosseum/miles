package com.codeosseum.miles.faultseeding.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.challenge.configuation.ChallengeModule;
import com.codeosseum.miles.faultseeding.match.configuration.DefaultMatchConfigurationHolderImpl;
import com.codeosseum.miles.faultseeding.match.configuration.MatchConfigurationHolder;
import com.codeosseum.miles.faultseeding.match.registration.MatchIgniter;
import com.codeosseum.miles.faultseeding.match.registration.MatchRegistrationController;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignalListener;
import com.codeosseum.miles.faultseeding.submission.configuration.SubmissionModule;
import com.codeosseum.miles.faultseeding.task.configuration.TaskModule;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;
import com.google.inject.Module;

import java.util.List;

import static java.util.Arrays.asList;

public class FaultSeedingModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MatchStatus.class, EventDispatcher.class);
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
        bindEagerSingleton(MatchRegistrationController.class);

        bindSingleton(MatchConfigurationHolder.class, DefaultMatchConfigurationHolderImpl.class);

        bindEagerSingleton(MatchIgniter.class);

        bindEagerSingleton(MatchCommencingSignalListener.class);
    }
}
