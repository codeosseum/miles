package com.codeosseum.miles.player.configuration;

import com.codeosseum.miles.player.DefaultPlayerRegistryImpl;
import com.codeosseum.miles.player.PlayerRegistry;
import com.codeosseum.miles.util.inject.MilesModule;
import com.google.inject.Singleton;

public class PlayerModule extends MilesModule {
    @Override
    protected void configureModule() {
        bind(PlayerRegistry.class).to(DefaultPlayerRegistryImpl.class).in(Singleton.class);
    }
}
