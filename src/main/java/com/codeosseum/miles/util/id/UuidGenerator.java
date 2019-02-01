package com.codeosseum.miles.util.id;

import java.util.UUID;

import com.codeosseum.miles.util.id.IdGenerator;

public class UuidGenerator implements IdGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
