package com.codeosseum.miles.faultseeding.match.registration;

import com.codeosseum.miles.communication.http.controller.JsonHttpController;
import com.codeosseum.miles.eventbus.dispatch.EventDispatcher;
import com.codeosseum.miles.faultseeding.match.configuration.MatchConfiguration;
import com.codeosseum.miles.match.MatchStatus;
import com.google.gson.Gson;
import com.google.inject.Inject;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static com.codeosseum.miles.match.MatchStatus.UNSET_MODE;
import static org.eclipse.jetty.http.HttpStatus.Code.CONFLICT;
import static org.eclipse.jetty.http.HttpStatus.Code.CREATED;
import static spark.Spark.put;

public class MatchRegistrationController extends JsonHttpController {
    private static final String EMPTY_RESPONSE = "";

    private static final Logger LOGGER = LoggerFactory.getLogger(MatchRegistrationController.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String NEW_MATCH = "/match/fault-seeding";

    private final MatchStatus matchStatus;

    private final EventDispatcher eventDispatcher;

    @Inject
    public MatchRegistrationController(final Gson gson, final MatchStatus matchStatus, final EventDispatcher eventDispatcher) {
        super(gson);

        this.matchStatus = matchStatus;
        this.eventDispatcher = eventDispatcher;
    }

    @Override
    public void attach() {
        put(NEW_MATCH, APPLICATION_JSON, this::registerNewMatch);

        LOGGER.info("Attached Fault Seeding MatchRegistrationController");
    }

    private Object registerNewMatch(final Request request, final Response response) {
        if (matchStatus.getCurrentMode().equals(UNSET_MODE)) {
            response.status(CREATED.getCode());

            dispatchMatch(gson.fromJson(request.body(), RegisterMatchRequest.class));

            LOGGER.info("Registered new FAULT SEEDING match");
        } else {
            LOGGER.warn("Received a match request while in-game.");

            response.status(CONFLICT.getCode());
        }

        return EMPTY_RESPONSE;
    }

    private void dispatchMatch(final RegisterMatchRequest request) {
        final FaultSeedingMatchRegisteredEvent event = FaultSeedingMatchRegisteredEvent.builder()
                .id(request.id)
                .matchConfiguration(request.matchConfiguration)
                .joinPassword(request.joinPassword)
                .build();

        eventDispatcher.dispatchEvent(event);
    }

    @Value
    private static final class RegisterMatchRequest {
        private final String id;

        private final MatchConfiguration matchConfiguration;

        private final String joinPassword;
    }
}
