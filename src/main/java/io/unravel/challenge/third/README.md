# Exercise 3 Solution

For this exercise, a `PriorityBlockingQueue` is used to impose an ordering rule on the `FutureVaryingTask` tasks to 
consume. `FutureVaryingTask` is a wrapper of `VaryingTask` and implements the `Comparable` interface which is used 
by the queue for deciding which task goes first. The Producer takes 1 second to produce a task while the consumer 1.
5 seconds to consume one. The Producer produces tasks according to a Gaussian distribution: this means that most of 
the tasks will spawn with a LOW priority while MEDIUM or HIGH tasks will be spawned less frequently.

As we can see from the output below, the tasks with lower priorities are consumed first 

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

Please note that the current implementation will starve low priority tasks (you can easily verify this by shifting 
the Gaussian distribution center around the priority HIGH). To solve this, we could use a `ThreadPoolExecutor` that
is backed up by the implemented `PriorityBlockingQueue`. In this way, dedicated threads will handle low priority 
tasks too.
