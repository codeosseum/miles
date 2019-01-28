package com.codeosseum.miles.util.inject;

import com.codeosseum.miles.util.inject.attach.HttpControllerAttacher;
import com.codeosseum.miles.util.inject.attach.WebSocketControllerAttacher;
import com.codeosseum.miles.util.inject.initialization.PostConstructListener;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Singleton;
import com.google.inject.matcher.Matchers;

import java.util.Collections;
import java.util.List;

import static java.util.Objects.requireNonNull;

public class MilesModule extends AbstractModule {
    protected void configureModule() {

    }

    @Override
    protected void configure() {
        requires().forEach(this::requireBinding);

        installs().forEach(this::install);

        this.configureModule();

        bindListener(Matchers.any(), PostConstructListener.INSTANCE);

        bindListener(Matchers.any(), WebSocketControllerAttacher.INSTANCE);

        bindListener(Matchers.any(), HttpControllerAttacher.INSTANCE);
    }

    protected List<Class<?>> requires() {
        return Collections.emptyList();
    }

    protected List<Module> installs() { return Collections.emptyList(); }

    protected final <T> void bindSingleton(final Class<T> clazz) {
        bind(requireNonNull(clazz)).in(Singleton.class);
    }

    protected final <T, S extends T> void bindSingleton(final Class<T> clazz, final Class<S> subclazz) {
        bind(requireNonNull(subclazz)).in(Singleton.class);
        bind(requireNonNull(clazz)).to(subclazz);
    }

    protected final <T> void bindEagerSingleton(final Class<T> clazz) {
        bind(requireNonNull(clazz)).asEagerSingleton();
    }

    protected final <T, S extends T> void bindEagerSingleton(final Class<T> clazz, final Class<S> subclazz) {
        bind(requireNonNull(subclazz)).asEagerSingleton();
        bind(requireNonNull(clazz)).to(subclazz);
    }
}
