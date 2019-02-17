package com.codeosseum.miles.faultseeding.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.challenge.configuation.ChallengeModule;
import com.codeosseum.miles.faultseeding.match.configuration.DefaultMatchConfigurationHolderImpl;
import com.codeosseum.miles.faultseeding.match.configuration.MatchConfigurationHolder;
import com.codeosseum.miles.faultseeding.match.flow.MatchFlowListener;
import com.codeosseum.miles.faultseeding.match.over.DefaultMatchTimerImpl;
import com.codeosseum.miles.faultseeding.match.over.MatchTimer;
import com.codeosseum.miles.faultseeding.match.over.MatchTimerListener;
import com.codeosseum.miles.faultseeding.match.registration.MatchIgniter;
import com.codeosseum.miles.faultseeding.match.registration.MatchRegistrationController;
import com.codeosseum.miles.faultseeding.match.setup.commencing.MatchCommencingSignalListener;
import com.codeosseum.miles.faultseeding.match.setup.starting.MatchStartingPublisherListener;
import com.codeosseum.miles.faultseeding.match.setup.waiting.AllPlayersAvailableSignalListener;
import com.codeosseum.miles.faultseeding.match.setup.waiting.PlayerJoinedListener;
import com.codeosseum.miles.faultseeding.scoring.DefaultScoringServiceImpl;
import com.codeosseum.miles.faultseeding.scoring.ScoringService;
import com.codeosseum.miles.faultseeding.submission.configuration.SubmissionModule;
import com.codeosseum.miles.faultseeding.task.configuration.TaskModule;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.util.List;
import java.util.Timer;

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

        bindEagerSingleton(MatchStartingPublisherListener.class);

        bindEagerSingleton(AllPlayersAvailableSignalListener.class);

        bindEagerSingleton(PlayerJoinedListener.class);

        bindEagerSingleton(ScoringService.class, DefaultScoringServiceImpl.class);

        bindEagerSingleton(MatchFlowListener.class);

        bindEagerSingleton(MatchTimerListener.class);

        bind(Timer.class)
                .annotatedWith(Names.named("match-timer-timer"))
                .toInstance(new Timer());

        bindSingleton(MatchTimer.class, DefaultMatchTimerImpl.class);
    }
}
