package com.codeosseum.miles;

import com.codeosseum.miles.chat.ChatBootstrapper;
import com.codeosseum.miles.chat.configuration.ChatModule;
import com.codeosseum.miles.eventbus.configuration.EventBusModule;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.heartbeat.configuration.HeartbeatModule;
import com.codeosseum.miles.mapping.MappingModule;
import com.codeosseum.miles.communication.http.HttpBootstrapper;
import com.codeosseum.miles.communication.http.configuration.HttpModule;
import com.codeosseum.miles.communication.websocket.WebSocketBootstrapper;
import com.codeosseum.miles.communication.websocket.configuration.WebSocketModule;
import com.codeosseum.miles.registration.configuration.RegistrationModule;
import com.codeosseum.miles.session.SessionBootstrapper;
import com.codeosseum.miles.session.configuration.SessionModule;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static com.google.inject.Guice.createInjector;
import static java.util.Arrays.asList;
import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;

public final class Application {
    // TODO: read from configuration
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
                new HttpModule(),
                new RegistrationModule(),
                new HeartbeatModule(),
                new ChatModule(),
                new SessionModule());
    }

    @Singleton
    private static final class Bootstrapper {
        private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

        private final WebSocketBootstrapper webSocketBootstrapper;

        private final HttpBootstrapper httpBootstrapper;

        private final ChatBootstrapper chatBootstrapper;

        private final EventDispatcher eventDispatcher;

        private final SessionBootstrapper sessionBootstrapper;

        @Inject
        public Bootstrapper(final WebSocketBootstrapper webSocketBootstrapper, final HttpBootstrapper httpBootstrapper, final ChatBootstrapper chatBootstrapper, final EventDispatcher eventDispatcher, final SessionBootstrapper sessionBootstrapper) {
            this.webSocketBootstrapper = webSocketBootstrapper;
            this.httpBootstrapper = httpBootstrapper;
            this.chatBootstrapper = chatBootstrapper;
            this.eventDispatcher = eventDispatcher;
            this.sessionBootstrapper = sessionBootstrapper;
        }

        private void bootstrap() {
            port(PORT);

            webSocketBootstrapper.bootstrap();

            chatBootstrapper.bootstrap();

            httpBootstrapper.bootstrap();

            sessionBootstrapper.bootstrap();

            get("/", (request, response) -> "Hello, World!");

            awaitInitialization();

            eventDispatcher.dispatchEvent(new StartupSignal());
        }
    }
}
