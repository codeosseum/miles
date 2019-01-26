package com.codeosseum.miles.util.inject.attach;

import com.codeosseum.miles.communication.websocket.controller.JsonWebSocketController;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public enum WebSocketControllerAttacher implements TypeListener {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketControllerAttacher.class);

    private final Set<JsonWebSocketController> controllers = new CopyOnWriteArraySet<>();

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        if (notJsonWebSocketController(type)) {
            return;
        }

        encounter.register((InjectionListener<I>) injectee -> {
            final JsonWebSocketController controller = (JsonWebSocketController) injectee;

            if (controllers.contains(controller)) {
                return;
            }

            controllers.add(controller);

            LOGGER.info("Registered instance of WS controller class: {}", controller.getClass());
        });
    }

    public void attachControllersTo(final WebSocketDispatcher dispatcher) {
        controllers.forEach(controller -> controller.attach(dispatcher));
    }

    private boolean notJsonWebSocketController(TypeLiteral<?> type) {
        return !JsonWebSocketController.class.isAssignableFrom(type.getRawType());
    }
}
