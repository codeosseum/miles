package com.codeosseum.miles.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class MappingModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(JsonParser.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Gson provideGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();

        return gsonBuilder.create();
    }
}
