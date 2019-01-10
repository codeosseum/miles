package com.codeosseum.miles.registration.registrar;

import lombok.Value;

@Value
public class RegistrationEvent {
    public static final String IDENTIFIER = "registration";

    private final String serverIdentifier;

    private final String uri;
}
