package com.codeosseum.miles.match.configuration;

import com.codeosseum.miles.match.DefaultMatchStatusImpl;
import com.codeosseum.miles.match.MatchStatus;
import com.codeosseum.miles.util.inject.MilesModule;

public class MatchModule extends MilesModule {
    @Override
    protected void configureModule() {
        bindSingleton(MatchStatus.class, DefaultMatchStatusImpl.class);
    }
}
