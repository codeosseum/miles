package com.codeosseum.miles.messaging.http.configuration;

import com.codeosseum.miles.messaging.http.HttpBootstrapper;
import com.codeosseum.miles.messaging.http.push.HttpPushMessageToAresServiceImpl;
import com.codeosseum.miles.messaging.push.PushMessageToAresService;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.mashape.unirest.http.ObjectMapper;

import java.util.List;

import static java.util.Arrays.asList;

public class HttpModule extends AbstractModule {
    private static final List<Class<?>> REQUIRES = asList(ObjectMapper.class);

    @Override
    protected void configure() {
        REQUIRES.forEach(this::requireBinding);

        bind(PushMessageToAresService.class).to(HttpPushMessageToAresServiceImpl.class).in(Singleton.class);

        bind(HttpBootstrapper.class).in(Singleton.class);
    }
}
