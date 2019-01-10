package com.codeosseum.miles.communication.websocket.session;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jetty.websocket.api.Session;

public interface SessionRegistry {
    Set<Session> getActiveSessions();

    void addActiveSession(Session session);

    void removeActiveSession(Session session);

    void addAuthenticatedSession(Session session, long id);

    void removeAuthenticatedSession(Session session);

    Map<Long, Session> getAuthenticatedSessions();

    Optional<Long> getIdForSession(Session session);

    Optional<Session> getSessionForId(long id);
}

