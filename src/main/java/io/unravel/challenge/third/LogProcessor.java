package io.unravel.challenge.third;

import io.unravel.challenge.third.task.FutureVaryingTask;
import io.unravel.challenge.third.task.VaryingTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.PriorityBlockingQueue;

public class LogProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(LogProcessor.class);
    private PriorityBlockingQueue<FutureVaryingTask> logQueue = new PriorityBlockingQueue<>();
    private Random random = new Random();

    public void produceLog(String log) {
        int nextRounded = (int) Math.round(random.nextGaussian() + VaryingTask.Priority.LOW.getValue());
        int randomPriorityLevel = Math.max(VaryingTask.Priority.HIGH.getValue(),
                Math.min(VaryingTask.Priority.LOW.getValue(), nextRounded));
        VaryingTask task = new VaryingTask(VaryingTask.Priority.from(randomPriorityLevel), log);
        LOG.info("Spawning task: " + task);
        logQueue.offer(new FutureVaryingTask(task));
    }

    public String consumeLog() throws InterruptedException {
        FutureVaryingTask head = logQueue.take();
        return head.getTask().getPayload();
    }
}
