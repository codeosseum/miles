package com.codeosseum.miles.util.configuration;

import com.codeosseum.miles.util.id.UuidGenerator;
import com.codeosseum.miles.util.id.IdGenerator;
import com.codeosseum.miles.util.inject.MilesModule;

public class UtilModule extends MilesModule {
    @Override
    protected void configureModule() {
        bindSingleton(IdGenerator.class, UuidGenerator.class);
    }
}
