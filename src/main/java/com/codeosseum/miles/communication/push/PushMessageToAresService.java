package com.codeosseum.miles.communication.push;

import com.codeosseum.miles.communication.Message;

public interface PushMessageToAresService {
    <T> void sendMessage(Message<T> message);
}
