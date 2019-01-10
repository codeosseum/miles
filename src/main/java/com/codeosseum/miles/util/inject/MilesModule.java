package com.codeosseum.miles.util.inject;

import com.codeosseum.miles.util.inject.initialization.PostConstructListener;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import java.util.Collections;
import java.util.List;

public class MilesModule extends AbstractModule {
    protected void configureModule() {

    }

    @Override
    protected void configure() {
        requires().forEach(this::requireBinding);

        this.configureModule();

        bindListener(Matchers.any(), PostConstructListener.INSTANCE);
    }

    protected List<Class<?>> requires() {
        return Collections.emptyList();
    }
}
