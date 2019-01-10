package com.codeosseum.miles.communication.http.push;

import com.codeosseum.miles.communication.Message;
import com.codeosseum.miles.communication.push.PushMessageException;
import com.codeosseum.miles.communication.push.PushMessageToAresService;
import com.mashape.unirest.http.Unirest;

import java.util.Objects;

public class HttpPushMessageToAresServiceImpl implements PushMessageToAresService {
    private static final String ARES_HOST = "127.0.0.1:8080";

    @Override
    public <T> void sendMessage(final Message<T> message) {
        try {
            Unirest.post(ARES_HOST).body(Objects.requireNonNull(message)).asString();
        } catch (Exception e) {
            throw new PushMessageException(e);
        }
    }
}
