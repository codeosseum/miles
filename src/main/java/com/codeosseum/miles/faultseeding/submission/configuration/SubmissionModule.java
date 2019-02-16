package com.codeosseum.miles.faultseeding.submission.configuration;

import java.util.List;

import com.codeosseum.miles.code.execution.listener.ListenerCodeExecutorFactory;
import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.faultseeding.submission.controller.SubmissionController;
import com.codeosseum.miles.faultseeding.submission.evaluation.DefaultSubmissionEvaluatorFactoryImpl;
import com.codeosseum.miles.faultseeding.submission.evaluation.SubmissionEvaluationService;
import com.codeosseum.miles.faultseeding.submission.evaluation.SubmissionEvaluatorFactory;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;
import com.google.inject.name.Names;

import static java.util.Arrays.asList;

public class SubmissionModule extends MilesModule {
    private static final String FACTORY_IDENTIFIER = "fault-seeding-submission-evaluator-factory";

    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MessageTransmitter.class);
    }

    @Override
    protected void configureModule() {
        bindEagerSingleton(SubmissionController.class);

        bindSingleton(SubmissionEvaluatorFactory.class, DefaultSubmissionEvaluatorFactoryImpl.class);

        bind(ListenerCodeExecutorFactory.class)
                .annotatedWith(Names.named(FACTORY_IDENTIFIER))
                .toInstance(new ListenerCodeExecutionFactoryProvider().get());

        bindEagerSingleton(SubmissionEvaluationService.class);
    }


}
