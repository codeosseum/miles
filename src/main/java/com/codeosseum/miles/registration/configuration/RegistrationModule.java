package com.codeosseum.miles.registration.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.registration.listener.StartupListener;
import com.codeosseum.miles.registration.registrar.DefaultServerRegistrarImpl;
import com.codeosseum.miles.registration.registrar.ServerRegistrar;
import com.codeosseum.miles.util.inject.initialization.PostConstructListener;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import java.util.List;

import static java.util.Arrays.asList;

public class RegistrationModule extends AbstractModule {
    private static final List<Class<?>> REQUIRES = asList(EventDispatcher.class);

    @Override
    protected void configure() {
        REQUIRES.forEach(this::requireBinding);

        bind(ServerRegistrar.class).to(DefaultServerRegistrarImpl.class).in(Singleton.class);

        bind(StartupListener.class).in(Singleton.class);

        bind(EventConsumerRegistrator.class).asEagerSingleton();

        bindListener(Matchers.any(), PostConstructListener.INSTANCE);
    }
}
