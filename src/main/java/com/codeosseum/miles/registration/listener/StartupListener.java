package com.codeosseum.miles.registration.listener;

import com.codeosseum.miles.StartupSignal;
import com.codeosseum.miles.eventbus.dispatch.EventConsumer;
import com.codeosseum.miles.registration.registrar.ServerRegistrar;
import com.google.inject.Inject;

public class StartupListener implements EventConsumer<StartupSignal> {
    private final ServerRegistrar serverRegistrar;

    @Inject
    public StartupListener(ServerRegistrar serverRegistrar) {
        this.serverRegistrar = serverRegistrar;
    }

    @Override
    public void accept(final StartupSignal startupSignal) {
        serverRegistrar.register();
    }
}
