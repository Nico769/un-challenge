# Exercise 3 Solution

For this exercise, a `PriorityBlockingQueue` is used to impose an ordering rule on the `FutureVaryingTask` tasks 
that are consumed. `FutureVaryingTask` is a wrapper of `VaryingTask` and implements the `Comparable` interface which 
is used 
by the queue for deciding which task goes first.

Since the Producer generates tasks faster than the Consumer processes them (1s vs 1.5s), a backlog develops, 
allowing the priority mechanism to demonstrate its effectiveness. The priority system uses numerical values where lower numbers indicate higher priority:

- HIGH = 0
- MEDIUM = 1
- LOW = 2

The Producer produces tasks according to a Gaussian distribution centered around LOW priority. This creates a 
realistic scenario where urgent tasks are rare.

As we can see from the output below, when a HIGH priority task (`Log 15`) is spawned, it gets consumed before earlier LOW priority tasks, proving the priority mechanism works correctly.

```
Spawning task: VaryingTask[priority=MEDIUM, payload='Log 0']
Spawning task: VaryingTask[priority=LOW, payload='Log 1']
Consumed: Log 0
Spawning task: VaryingTask[priority=LOW, payload='Log 2']
Consumed: Log 1
Spawning task: VaryingTask[priority=LOW, payload='Log 3']
Spawning task: VaryingTask[priority=LOW, payload='Log 4']
Consumed: Log 2
Spawning task: VaryingTask[priority=LOW, payload='Log 5']
Consumed: Log 4
Spawning task: VaryingTask[priority=LOW, payload='Log 6']
Spawning task: VaryingTask[priority=LOW, payload='Log 7']
Consumed: Log 5
Spawning task: VaryingTask[priority=LOW, payload='Log 8']
Consumed: Log 7
Spawning task: VaryingTask[priority=LOW, payload='Log 9']
Spawning task: VaryingTask[priority=LOW, payload='Log 10']
Consumed: Log 8
Spawning task: VaryingTask[priority=LOW, payload='Log 11']
Consumed: Log 10
Spawning task: VaryingTask[priority=LOW, payload='Log 12']
Spawning task: VaryingTask[priority=LOW, payload='Log 13']
Consumed: Log 11
Spawning task: VaryingTask[priority=LOW, payload='Log 14']
Consumed: Log 13
Spawning task: VaryingTask[priority=HIGH, payload='Log 15']
Spawning task: VaryingTask[priority=LOW, payload='Log 16']
Consumed: Log 15
Spawning task: VaryingTask[priority=LOW, payload='Log 17']
Consumed: Log 16
```

Please note that the current implementation can cause starvation of low-priority tasks when the system is overwhelmed with high-priority tasks (you can easily verify this by shifting 
the Gaussian distribution center toward the HIGH priority value). To solve this, we could use a `ThreadPoolExecutor` 
that
is backed up by the implemented `PriorityBlockingQueue`. In this way, we can tune the number of dedicated threads 
handling low priority tasks.
