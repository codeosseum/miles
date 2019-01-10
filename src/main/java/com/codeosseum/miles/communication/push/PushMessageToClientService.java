package com.codeosseum.miles.communication.push;

import com.codeosseum.miles.communication.Message;

public interface PushMessageToClientService {
    <T> void sendMessage(long recipientId, Message<T> message);
}
