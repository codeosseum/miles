package com.codeosseum.miles;

import com.codeosseum.miles.messaging.websocket.configuration.WebSocketModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;

import java.util.List;

import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static spark.Spark.port;

public final class Application {
    private static final int PORT = 8080;

    public static void main(String[] args) {
        final Injector injector = createInjector(modules());

        injector.getInstance(Bootstrapper.class).bootstrap();
    }

    private static List<Module> modules() {
        return asList(new WebSocketModule());
    }

    @Singleton
    public static final class Bootstrapper {
        public void bootstrap() {
            port(PORT);
        }
    }
}
