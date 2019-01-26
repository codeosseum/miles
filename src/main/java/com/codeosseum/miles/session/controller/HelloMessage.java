package com.codeosseum.miles.session.controller;

import lombok.Value;

@Value
public class HelloMessage {
    public static final String ACTION = "hello";

    private final String username;
}
