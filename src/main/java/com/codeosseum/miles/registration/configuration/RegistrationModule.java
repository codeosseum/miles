package com.codeosseum.miles.registration.configuration;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.registration.listener.StartupListener;
import com.codeosseum.miles.registration.registrar.DefaultServerRegistrarImpl;
import com.codeosseum.miles.registration.registrar.ServerRegistrar;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

import java.util.List;

import static java.util.Arrays.asList;

public class RegistrationModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bind(ServerRegistrar.class).to(DefaultServerRegistrarImpl.class).in(Singleton.class);

        bind(StartupListener.class).in(Singleton.class);
        
        bind(EventConsumerRegistrator.class).asEagerSingleton();
    }
}
