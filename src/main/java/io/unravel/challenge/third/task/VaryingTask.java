package io.unravel.challenge.third.task;

import java.util.StringJoiner;

public class VaryingTask implements Runnable {
    private Priority priority;
    private String payload;

    public VaryingTask(Priority priority, String payload) {
        this.priority = priority;
        this.payload = payload;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public Priority getPriority() {
        return priority;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", VaryingTask.class.getSimpleName() + "[", "]")
                .add("priority=" + priority)
                .add("payload='" + payload + "'")
                .toString();
    }

    public enum Priority {
        HIGH(0),
        MEDIUM(1),
        LOW(2);

        int value;

        Priority(int value) {
            this.value = value;
        }

        public static Priority from(int value) {
            return switch (value) {
                case 1 -> Priority.MEDIUM;
                case 0 -> Priority.HIGH;
                default -> Priority.LOW;
            };
        }

        public int getValue() {
            return value;
        }
    }
}
