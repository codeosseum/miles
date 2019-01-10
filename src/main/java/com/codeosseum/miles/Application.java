package com.codeosseum.miles;

import com.codeosseum.miles.eventbus.configuration.EventBusModule;
import com.codeosseum.miles.mapping.MappingModule;
import com.codeosseum.miles.messaging.http.HttpBootstrapper;
import com.codeosseum.miles.messaging.http.configuration.HttpModule;
import com.codeosseum.miles.messaging.websocket.WebSocketBootstrapper;
import com.codeosseum.miles.messaging.websocket.configuration.WebSocketModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static spark.Spark.port;

public final class Application {
    private static final int PORT = 3000;

    public static void main(String[] args) {
        final Injector injector = createInjector(modules());

        injector.getInstance(Bootstrapper.class).bootstrap();
    }

    private static List<Module> modules() {
        return asList(
                new MappingModule(),
                new WebSocketModule(),
                new EventBusModule(),
                new HttpModule());
    }

    @Singleton
    private static final class Bootstrapper {
        private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

        private final WebSocketBootstrapper webSocketBootstrapper;

        private final HttpBootstrapper httpBootstrapper;

        @Inject
        public Bootstrapper(final WebSocketBootstrapper webSocketBootstrapper, final HttpBootstrapper httpBootstrapper) {
            this.webSocketBootstrapper = webSocketBootstrapper;
            this.httpBootstrapper = httpBootstrapper;
        }

        private void bootstrap() {
            port(PORT);

            webSocketBootstrapper.bootstrap();

            httpBootstrapper.bootstrap();
        }
    }
}
