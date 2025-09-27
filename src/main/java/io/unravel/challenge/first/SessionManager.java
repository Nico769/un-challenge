package io.unravel.challenge.first;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private Map<String, String> sessions = new ConcurrentHashMap<>();

    public String login(String userId) {
        String existingSessionId = sessions.putIfAbsent(userId, UUID.randomUUID().toString());
        if (existingSessionId != null) {
            System.out.println("User " + userId + " already logged in with Session ID: " + existingSessionId);
            return existingSessionId;
        }
        String createdSessionId = sessions.get(userId);
        System.out.println("Login successful for user " + userId + ". Session ID: " + createdSessionId);
        return createdSessionId;
    }

    public String logout(String userId) {
        String cleanedId = sessions.remove(userId);
        if (cleanedId != null) {
            System.out.println("Logout successful.");
            return "Logout successful.";
        }
        System.out.println("User not logged in.");
        return "";
    }

    public String getSessionDetails(String userId) {
        String sessionId = sessions.get(userId);
        if (sessionId == null) {
            System.out.println("Session not found for user " + userId);
            return "";
        }
        System.out.println("Session ID for user " + userId + ": " + sessionId);
        return sessionId;
    }
}
