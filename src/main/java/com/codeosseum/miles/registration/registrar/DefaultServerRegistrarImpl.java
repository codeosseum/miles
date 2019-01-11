package com.codeosseum.miles.registration.registrar;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.google.inject.Inject;

import static com.codeosseum.miles.communication.Message.message;

public class DefaultServerRegistrarImpl implements ServerRegistrar {
    // TODO: read from configuration
    private static final String SERVER_IDENTIFIER = "server-01";

    // TODO: read from configuration
    private static final String SERVER_URI = "127.0.0.1:3000";

    private final PushMessageToAresService pushMessageService;

    @Inject
    public DefaultServerRegistrarImpl(final PushMessageToAresService pushMessageService) {
        this.pushMessageService = pushMessageService;
    }

    @Override
    public void register() {
        pushMessageService.sendMessage(message(RegistrationEvent.IDENTIFIER, new RegistrationEvent(SERVER_IDENTIFIER, SERVER_URI)));
    }
}
