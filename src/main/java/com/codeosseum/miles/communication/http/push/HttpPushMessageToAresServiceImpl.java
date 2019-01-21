package com.codeosseum.miles.communication.http.push;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageException;
import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.mashape.unirest.http.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class HttpPushMessageToAresServiceImpl implements PushMessageToAresService {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpPushMessageToAresServiceImpl.class.getName());

    private static final String EVENT_IDENTIFIER_HEADER = "X-Event-Identifier";

    // TODO: read from configuration
    private static final String ARES_HOST = "http://127.0.0.1:8000/api/event";

    @Override
    public <T> void sendMessage(final Message<T> message) {
        Objects.requireNonNull(message);

        LOGGER.debug("Pushing message to Ares: {}", message);

        try {
            Unirest.post(ARES_HOST)
                .header(EVENT_IDENTIFIER_HEADER, message.getAction())
                .body(message.getPayload())
                .asString();
        } catch (Exception e) {
            throw new PushMessageException(e);
        }
    }
}
