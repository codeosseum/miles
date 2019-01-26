package com.codeosseum.miles.registration.listener;

import com.codeosseum.miles.StartupSignal;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.eventbus.dispatch.SignalConsumer;
import com.codeosseum.miles.registration.ServerRegisteredSignal;
import com.codeosseum.miles.registration.registrar.ServerRegistrar;
import com.google.inject.Inject;

public class StartupListener implements SignalConsumer {
    private final ServerRegistrar serverRegistrar;

    private final EventDispatcher eventDispatcher;

    @Inject
    public StartupListener(final ServerRegistrar serverRegistrar, final EventDispatcher eventDispatcher) {
        this.serverRegistrar = serverRegistrar;

        this.eventDispatcher = eventDispatcher;

        eventDispatcher.registerConsumer(StartupSignal.class, this);
    }

    @Override
    public void accept() {
        serverRegistrar.register();

        eventDispatcher.dispatchEvent(new ServerRegisteredSignal());
    }
}
