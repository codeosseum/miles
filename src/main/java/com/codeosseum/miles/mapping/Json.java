package com.codeosseum.miles.mapping;

import java.io.IOException;
import java.nio.file.Path;

public interface Json {
    <T> T fromJson(String json, Class<T> type);

    <T> T fromJson(Path path, Class<T> type) throws IOException;

    <T> String toJson(T obj);
}
