package com.codeosseum.miles.faultseeding.registration;

import com.codeosseum.miles.communication.http.controller.JsonHttpController;
import com.google.gson.Gson;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Request;
import spark.Response;

import static spark.Spark.put;

public class MatchRegistrationController extends JsonHttpController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MatchRegistrationController.class);

    private static final String APPLICATION_JSON = "application/json";

    private static final String NEW_MATCH = "/match/faultseeding";

    @Inject
    public MatchRegistrationController(Gson gson) {
        super(gson);
    }

    @Override
    public void attach() {
        LOGGER.info("hai");
        put(NEW_MATCH, APPLICATION_JSON, this::registerNewMatch);
    }

    private Object registerNewMatch(final Request request, final Response response) {
        return null;
    }
}
