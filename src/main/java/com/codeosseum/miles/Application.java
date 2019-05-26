package com.codeosseum.miles;

import com.codeosseum.miles.challenge.configuration.ChallengeModule;
import com.codeosseum.miles.chat.configuration.ChatModule;
import com.codeosseum.miles.communication.websocket.dispatcher.WebSocketDispatcher;
import com.codeosseum.miles.configuration.ConfigurationModule;
import com.codeosseum.miles.configuration.SelfConfig;
import com.codeosseum.miles.eventbus.configuration.EventBusModule;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.configuration.FaultSeedingModule;
import com.codeosseum.miles.heartbeat.configuration.HeartbeatModule;
import com.codeosseum.miles.mapping.MappingModule;
import com.codeosseum.miles.communication.http.HttpBootstrapper;
import com.codeosseum.miles.communication.http.configuration.HttpModule;
import com.codeosseum.miles.communication.websocket.WebSocketBootstrapper;
import com.codeosseum.miles.communication.websocket.configuration.WebSocketModule;
import com.codeosseum.miles.match.configuration.MatchModule;
import com.codeosseum.miles.player.configuration.PlayerModule;
import com.codeosseum.miles.registration.configuration.RegistrationModule;
import com.codeosseum.miles.session.configuration.SessionModule;
import com.codeosseum.miles.util.configuration.UtilModule;
import com.codeosseum.miles.util.inject.attach.HttpControllerAttacher;
import com.codeosseum.miles.util.inject.attach.WebSocketControllerAttacher;
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
import static spark.Spark.port;

public final class Application {
    public static void main(String[] args) {
        final Injector injector = createInjector(modules());

        injector.getInstance(Bootstrapper.class).bootstrap();
    }

    private static List<Module> modules() {
        return asList(
                new ConfigurationModule(),
                new MappingModule(),
                new WebSocketModule(),
                new EventBusModule(),
                new HttpModule(),
                new RegistrationModule(),
                new HeartbeatModule(),
                new ChatModule(),
                new SessionModule(),
                new PlayerModule(),
                new ChallengeModule(),
                new FaultSeedingModule(),
                new UtilModule(),
                new MatchModule());
    }

    @Singleton
    private static final class Bootstrapper {
        private static final Logger logger = LoggerFactory.getLogger(Bootstrapper.class);

        private final WebSocketBootstrapper webSocketBootstrapper;

        private final HttpBootstrapper httpBootstrapper;

        private final EventDispatcher eventDispatcher;

        private final WebSocketDispatcher webSocketDispatcher;

        private final SelfConfig configuration;

        @Inject
        public Bootstrapper(final WebSocketBootstrapper webSocketBootstrapper, final HttpBootstrapper httpBootstrapper, final EventDispatcher eventDispatcher,
                            final WebSocketDispatcher webSocketDispatcher, final SelfConfig configuration) {
            this.webSocketBootstrapper = webSocketBootstrapper;
            this.httpBootstrapper = httpBootstrapper;
            this.eventDispatcher = eventDispatcher;
            this.webSocketDispatcher = webSocketDispatcher;
            this.configuration = configuration;
        }

        private void bootstrap() {
            port(configuration.getPort());

            webSocketBootstrapper.bootstrap();

            WebSocketControllerAttacher.INSTANCE.attachControllersTo(webSocketDispatcher);

            httpBootstrapper.bootstrap();

            HttpControllerAttacher.INSTANCE.attachControllers();

            awaitInitialization();

            eventDispatcher.dispatchEvent(new StartupSignal());
        }
    }
}
