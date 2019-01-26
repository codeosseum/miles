package com.codeosseum.miles.mapping;

import com.google.gson.Gson;
import com.google.inject.Inject;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public class GsonJsonImpl implements Json {
    private final Gson gson;

    @Inject
    public GsonJsonImpl(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public <T> T fromJson(final String json, final Class<T> type) {
        return gson.fromJson(requireNonNull(json), requireNonNull(type));
    }

    @Override
    public <T> T fromJson(final Path path, final Class<T> type) throws IOException {
        try (final BufferedReader reader = Files.newBufferedReader(requireNonNull(path))) {
            return gson.fromJson(reader, requireNonNull(type));
        }
    }

    @Override
    public <T> String toJson(final T obj) {
        return gson.toJson(obj);
    }
}
