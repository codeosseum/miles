package com.codeosseum.miles.player.configuration;

import java.util.List;

import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.player.DefaultPresentPlayerRegistryImpl;
import com.codeosseum.miles.player.DefaultRegisteredPlayerRegistryImpl;
import com.codeosseum.miles.player.PresentPlayerRegistry;
import com.codeosseum.miles.player.RegisteredPlayerRegistry;
import com.codeosseum.miles.player.event.PlayerLeftListener;
import com.codeosseum.miles.util.inject.MilesModule;

import static java.util.Arrays.asList;

public class PlayerModule extends MilesModule {
    @Override
    protected List<Class<?>> requires() {
        return asList(EventDispatcher.class);
    }

    @Override
    protected void configureModule() {
        bindSingleton(RegisteredPlayerRegistry.class, DefaultRegisteredPlayerRegistryImpl.class);

        bindSingleton(PresentPlayerRegistry.class, DefaultPresentPlayerRegistryImpl.class);

        bindEagerSingleton(PlayerLeftListener.class);
    }
}
