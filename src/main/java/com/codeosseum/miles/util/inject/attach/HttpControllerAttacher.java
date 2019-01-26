package com.codeosseum.miles.util.inject.attach;

import com.codeosseum.miles.communication.http.controller.JsonHttpController;
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

public enum HttpControllerAttacher implements TypeListener {
    INSTANCE;

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpControllerAttacher.class);

    private final Set<JsonHttpController> controllers = new CopyOnWriteArraySet<>();

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        if (notJsonHttpController(type)) {
            return;
        }

        encounter.register((InjectionListener<I>) injectee -> {
            final JsonHttpController controller = (JsonHttpController) injectee;

            if (controllers.contains(controller)) {
                return;
            }

            controllers.add(controller);

            LOGGER.info("Registered instance of HTTP controller class: {}", controller.getClass());
        });
    }

    public void attachControllers() {
        controllers.forEach(JsonHttpController::attach);
    }

    private boolean notJsonHttpController(TypeLiteral<?> type) {
        return !JsonHttpController.class.isAssignableFrom(type.getRawType());
    }
}
