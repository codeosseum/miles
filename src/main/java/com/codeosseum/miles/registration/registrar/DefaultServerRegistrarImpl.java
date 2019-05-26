package com.codeosseum.miles.registration.registrar;

import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.codeosseum.miles.configuration.SelfConfig;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.codeosseum.miles.communication.Message.message;

public class DefaultServerRegistrarImpl implements ServerRegistrar {
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultServerRegistrarImpl.class.getName());

    private final PushMessageToAresService pushMessageService;

    private final SelfConfig configuration;

    @Inject
    public DefaultServerRegistrarImpl(final PushMessageToAresService pushMessageService, final SelfConfig configuration) {
        this.pushMessageService = pushMessageService;
        this.configuration = configuration;
    }

    @Override
    public void register() {
        final String uriWithPort = configuration.getUri() + ":" + configuration.getPort();

        LOGGER.info("Registering server: {} - {}", configuration.getIdentifier(), uriWithPort);

        pushMessageService.sendMessage(message(RegistrationEvent.IDENTIFIER, new RegistrationEvent(configuration.getIdentifier(), uriWithPort)));
    }
}
