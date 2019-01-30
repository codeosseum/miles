package com.codeosseum.miles.faultseeding.submission.configuration;

import java.util.List;

import com.codeosseum.miles.communication.websocket.transmission.MessageTransmitter;
import com.codeosseum.miles.faultseeding.submission.controller.SubmissionController;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.gson.Gson;

import static java.util.Arrays.asList;

public class SubmissionModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(Gson.class, MessageTransmitter.class);
    }

    @Override
    protected void configureModule() {
        bindEagerSingleton(SubmissionController.class);
    }
}
