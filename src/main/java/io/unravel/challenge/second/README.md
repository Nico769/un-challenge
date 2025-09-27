# Exercise 2 Solution

The memory leak here is caused by `MemoryManager` class, which stores strong references to large memory chunks (10MB 
each) in a static `HashMap`. Under high load, the heap size will steadily increase and the application will 
eventually exhaust it, causing a `OutOfMemoryError` exception. The following VisualVM screenshot demonstrates this issue through the characteristic upward trend in heap usage:

![1](./1.png)

A possible solution to the memory leak is to use a `DelayQueue` of `SessionExpiry`. In this way, we can leverage the 
`Delayed` interface for comparing the `expiryTime` of `SessionExpiry` objects: whenever a session is expired 
then the head of the queue can be "taken" and, therefore, the associated data can be freed. As we can see from the 
following screenshot, after an initial ramp-up period, memory stabilizes because expired sessions are automatically 
cleaned up every minute, preventing unbounded growth:

```java
new SessionExpiry(sessionId, TimeUnit.MINUTES.toMillis(1));
```

![2](./2.png)

A nice benefit of this approach is that the cleanup occurs transparently during normal operations. However, if no 
add or remove session data operations occur, the 
cleanup is not triggered. Nonetheless, this can be addressed by using a `ScheduledThreadPoolExecutor` which, 
upon session expiration, would have used dedicated background threads for periodic cleanup.

Regarding the last point of the exercise, that is "reducing memory pressure", we could opt for compressing the 
session data or lazy loading it from a SQL database, ideally via `spring-boot-session-jdbc`.