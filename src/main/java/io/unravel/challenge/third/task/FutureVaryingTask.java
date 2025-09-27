package io.unravel.challenge.third.task;

import java.util.concurrent.FutureTask;

public class FutureVaryingTask extends FutureTask<FutureVaryingTask> implements
        Comparable<FutureVaryingTask> {
    protected VaryingTask task;

    public FutureVaryingTask(VaryingTask task) {
        super(task, null);
        this.task = task;
    }

    @Override
    public int compareTo(FutureVaryingTask other) {
        return task.getPriority().getValue() - other.task.getPriority().getValue();
    }

    public VaryingTask getTask() {
        return task;
    }
}
