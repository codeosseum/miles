package com.codeosseum.miles.messaging.push;

import com.codeosseum.miles.messaging.Message;

public interface PushMessageService {
    <T> void sendMessage(long recipientId, Message<T> message);
}
