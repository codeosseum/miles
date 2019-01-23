package com.codeosseum.miles.communication.websocket.session;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;

public interface SessionRegistry {
    Set<Session> getActiveSessions();

    void addActiveSession(Session session);

    void removeActiveSession(Session session);

    void addAuthenticatedSession(Session session, String id);

    void removeAuthenticatedSession(Session session);

    Map<String, Session> getAuthenticatedSessions();

    Optional<String> getIdForSession(Session session);

    Optional<Session> getSessionForId(String id);
}

