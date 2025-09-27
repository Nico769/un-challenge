package io.unravel.challenge.second;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class QueuedMemoryManager {
    private static final Map<String, byte[]> largeSessionData = new ConcurrentHashMap<>();
    private static final DelayQueue<SessionExpiry> expiryQueue = new DelayQueue<>();

    private static class SessionExpiry implements Delayed {
        private final String sessionId;
        private final long expiryTime;

        private SessionExpiry(String sessionId, long ttlMillis) {
            this.sessionId = sessionId;
            this.expiryTime = System.currentTimeMillis() + ttlMillis;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expiryTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public int compareTo(Delayed o) {
            return Long.compare(this.expiryTime, ((SessionExpiry) o).expiryTime);
        }

        public String getSessionId() {
            return sessionId;
        }
    }

    public static void addSessionData(String sessionId) {
        cleanupExpired();
        largeSessionData.put(sessionId, new byte[10 * 1024 * 1024]);
        expiryQueue.offer(new SessionExpiry(sessionId, TimeUnit.MINUTES.toMillis(1)));
    }

    public static void removeSessionData(String sessionId) {
        cleanupExpired();
        largeSessionData.remove(sessionId);
    }

    private static void cleanupExpired() {
        SessionExpiry expired;
        while((expired = expiryQueue.poll()) != null) {
            largeSessionData.remove(expired.getSessionId());
        }
    }
}