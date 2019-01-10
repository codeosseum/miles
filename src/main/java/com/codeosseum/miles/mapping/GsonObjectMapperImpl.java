package com.codeosseum.miles.mapping;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.mashape.unirest.http.ObjectMapper;

public class GsonObjectMapperImpl implements ObjectMapper {
    private final Gson gson;

    @Inject
    public GsonObjectMapperImpl(final Gson gson) {
        this.gson = gson;
    }

    @Override
    public <T> T readValue(final String s, final Class<T> aClass) {
        try {
            return gson.fromJson(s, aClass);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String writeValue(final Object o) {
        try {
            return gson.toJson(o);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
