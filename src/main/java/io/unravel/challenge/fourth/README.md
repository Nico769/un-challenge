# Exercise 4 Solution

For this exercise, I won't include any code since the provided `DeadlockSimulator` is a toy example. Instead, I'll 
briefly address the example first and then the rest of the exercise.

In the toy example, the deadlock is occurring because the "basic rule" of a circular wait is broken: that is, locks 
are acquired without a global ordering in mind. To solve the circular wait, one should release the locks inverting 
the order in which they were acquired, namely:

```
lock1.acquire()
lock2.acquire()
lock2.release()
lock1.release()
```

Now, let's address the scenario outlined by the exercise which is way more involved than the example above. In 
short:

1. It's implied that there are multiple shared resources, not just two;
2. Deadlock occurs only under higher concurrency load, not consistently;
3. Third-party libraries are also using the same shared resources we do, implying that it's hard to do a 
  rewrite without breaking the libraries.

For the first point, assuming that there are multiple threads running on the same machine (in other words, we don't 
consider distributed locks), we might consider assigning an identifier to the shared resource and held a registry of 
locks. Then, we'd lock the resources in a consistent order based on their identifiers and store the resulting locks
in a registry (like a `ConcurrentHashMap`). Whenever two threads need to lock the same resource then one of them 
will wait until the resource is freed.

For the second point, we might argue that under heavy traffic it's more likely for resource contention to occur, 
especially if some thread are starved. A good starting point might be profiling the application with JFR (assuming we're ok 
with the performance overhead caused by the profiling itself) and analyze the dump in VisualVM. We should focus on 
the waiting time and the stack trace of the threads hitting the most contended locks. Once those are identified, we 
could reduce the threads waiting time by using a fair lock (i.e. `ReentrantLock`) or the lock striping technique
(i.e. instead of locking down the whole resource, we just lock a portion of it, like `ConcurrentHasMap` does).

For the last point, assuming we can't replace the third-party library with something else solving the same task and
having a better implementation in this regard, a possible solution could be to treat it like a "block box". That is, 
the main process could start and monitor a child process which performs the third-party library calls. If the child 
becomes stuck or is too slow, the main process will kill and restart it while doing the proper resource cleanup. 
