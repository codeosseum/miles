package com.codeosseum.miles.communication.websocket.session;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import org.eclipse.jetty.websocket.api.Session;

public class DefaultSessionRegistryImpl implements SessionRegistry {
    private final Set<Session> activeSessions;

    private final Map<String, Session> idToSessionMap;

    private final Map<Session, String> sessionToIdMap;

    public DefaultSessionRegistryImpl() {
        this.activeSessions = new CopyOnWriteArraySet<>();
        this.idToSessionMap = new ConcurrentHashMap<>();
        this.sessionToIdMap = new ConcurrentHashMap<>();
    }

    @Override
    public Set<Session> getActiveSessions() {
        return Collections.unmodifiableSet(activeSessions);
    }

    @Override
    public void addActiveSession(final Session session) {
        activeSessions.add(session);
    }

    @Override
    public void removeActiveSession(final Session session) {
        activeSessions.remove(session);
    }

    @Override
    public void addAuthenticatedSession(final Session session, final String id) {
        idToSessionMap.put(Objects.requireNonNull(id), Objects.requireNonNull(session));
        sessionToIdMap.put(session, id);
    }

    @Override
    public void removeAuthenticatedSession(final Session session) {
        Optional.ofNullable(sessionToIdMap.get(session))
                .ifPresent(id -> {
                    sessionToIdMap.remove(session);
                    idToSessionMap.remove(id);
                });
    }

    @Override
    public Map<String, Session> getAuthenticatedSessions() {
        return Collections.unmodifiableMap(idToSessionMap);
    }

    @Override
    public Optional<String> getIdForSession(final Session session) {
        return Optional.ofNullable(sessionToIdMap.get(session));
    }

    @Override
    public Optional<Session> getSessionForId(final String id) {
        return Optional.ofNullable(idToSessionMap.get(Objects.requireNonNull(id)));
    }
}

