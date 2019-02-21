package com.codeosseum.miles.registration.registrar;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeosseum.miles.communication.Message.message;

public class DefaultServerRegistrarImpl implements ServerRegistrar {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerRegistrarImpl.class.getName());

    // TODO: read from configuration
    private static final String SERVER_IDENTIFIER = "server-01";

    // TODO: read from configuration
    private static final String SERVER_URI = "http://127.0.0.1:3000";

    private final PushMessageToAresService pushMessageService;

    @Inject
    public DefaultServerRegistrarImpl(final PushMessageToAresService pushMessageService) {
        this.pushMessageService = pushMessageService;
    }

    @Override
    public void register() {
        LOGGER.info("Registering server: {} - {}", SERVER_IDENTIFIER, SERVER_URI);

        pushMessageService.sendMessage(message(RegistrationEvent.IDENTIFIER, new RegistrationEvent(SERVER_IDENTIFIER, SERVER_URI)));
    }
}
