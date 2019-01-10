package com.codeosseum.miles.messaging.push;

import com.codeosseum.miles.messaging.Message;

public interface PushMessageToClientService {
    <T> void sendMessage(long recipientId, Message<T> message);
}
