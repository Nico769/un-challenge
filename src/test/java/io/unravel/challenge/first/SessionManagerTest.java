package io.unravel.challenge.first;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

class SessionManagerTest {

    private static final String A_USER_ID = "1";
    private static final int JOB_DURATION_MILLIS = 100;
    private static final int SESSION_MANAGER_CLIENTS_NUM = 3;
    private static final String ANOTHER_USER_ID = "2";
    private static final String YET_ANOTHER_USER_ID = "3";
    SessionManager sessionManager = new SessionManager();

    public class SimulateUserLoggingInOp implements Callable<String> {
        private final String userId;

        public SimulateUserLoggingInOp(String userId) {
            this.userId = userId;
        }

        @Override
        public String call() {
            try {
                Thread.sleep(JOB_DURATION_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return sessionManager.login(userId);

        }
    }

    public class SimulateUserLoggingOutOp implements Callable<String> {
        private final String userId;

        public SimulateUserLoggingOutOp(String userId) {
            this.userId = userId;
        }

        @Override
        public String call() {
            try {
                Thread.sleep(JOB_DURATION_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return sessionManager.logout(userId);

        }
    }

    public class SimulateSessionDetailsOp implements Callable<String> {
        private final String userId;

        public SimulateSessionDetailsOp(String userId) {
            this.userId = userId;
        }

        @Override
        public String call() {
            try {
                Thread.sleep(JOB_DURATION_MILLIS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return sessionManager.getSessionDetails(userId);

        }
    }

    @Test
    void shouldObtainSameSessionId_WhenAUserLogsInFromMultipleClients() throws ExecutionException,
            InterruptedException {
        SimulateUserLoggingInOp aUserLogsIn = new SimulateUserLoggingInOp(A_USER_ID);
        ExecutorService pool = Executors.newFixedThreadPool(SESSION_MANAGER_CLIENTS_NUM);

        List<Future<String>> loggedClientsFutures = new ArrayList<>();
        for (int i = 0; i < SESSION_MANAGER_CLIENTS_NUM; i++) {
            Future<String> fut = pool.submit(aUserLogsIn);
            loggedClientsFutures.add(fut);
        }
        pool.shutdown();

        String firstLoggedClientSessionId = loggedClientsFutures.get(0).get();
        assertThat(loggedClientsFutures)
                .extracting(Future::get)
                .allMatch(sessionId -> sessionId.equals(firstLoggedClientSessionId));
    }

    @Test
    void shouldObtainDifferentSessionIds_WhenTwoUsersLogInFromMultipleClients() throws ExecutionException,
            InterruptedException {
        SimulateUserLoggingInOp firstUserLogsIn = new SimulateUserLoggingInOp(ANOTHER_USER_ID);
        SimulateUserLoggingInOp secondUserLogsIn = new SimulateUserLoggingInOp(YET_ANOTHER_USER_ID);
        ExecutorService pool = Executors.newFixedThreadPool(SESSION_MANAGER_CLIENTS_NUM);

        List<Future<String>> loggedClientsFirstUserFutures = new ArrayList<>();
        for (int i = 0; i < SESSION_MANAGER_CLIENTS_NUM; i++) {
            Future<String> fut = pool.submit(firstUserLogsIn);
            loggedClientsFirstUserFutures.add(fut);
        }

        List<Future<String>> loggedClientsSecondUserFutures = new ArrayList<>();
        for (int i = 0; i < SESSION_MANAGER_CLIENTS_NUM; i++) {
            Future<String> fut = pool.submit(secondUserLogsIn);
            loggedClientsSecondUserFutures.add(fut);
        }
        pool.shutdown();

        assertThat(loggedClientsFirstUserFutures.get(0).get()).isNotEqualTo(loggedClientsSecondUserFutures.get(0).get());
    }

    @Test
    void shouldDoNothing_WhenANonLoggedInUserTriesLoggingOut() throws InterruptedException {
        SimulateUserLoggingOutOp aUserLogsOut = new SimulateUserLoggingOutOp(A_USER_ID);
        ExecutorService pool = Executors.newFixedThreadPool(SESSION_MANAGER_CLIENTS_NUM);

        List<Future<String>> operationsFutures = pool.invokeAll(List.of(aUserLogsOut, aUserLogsOut));
        pool.shutdown();

        assertThat(operationsFutures)
                .extracting(Future::get)
                .allMatch(String::isBlank);
    }

    @Test
    void shouldObtainNoSessionDetails_WhenAUserHasNotLoggedInYet() throws InterruptedException {
        SimulateSessionDetailsOp aSessionDetailsOp = new SimulateSessionDetailsOp(A_USER_ID);
        ExecutorService pool = Executors.newFixedThreadPool(SESSION_MANAGER_CLIENTS_NUM);

        List<Future<String>> operationsFutures = pool.invokeAll(List.of(aSessionDetailsOp, aSessionDetailsOp));
        pool.shutdown();

        assertThat(operationsFutures)
                .extracting(Future::get)
                .allMatch(String::isBlank);
    }
}