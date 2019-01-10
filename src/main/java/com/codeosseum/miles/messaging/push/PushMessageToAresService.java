package com.codeosseum.miles.messaging.push;

import com.codeosseum.miles.messaging.Message;

public interface PushMessageToAresService {
    <T> void sendMessage(Message<T> message);
}
