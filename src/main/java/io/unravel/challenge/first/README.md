# Exercise 1 Solution

The provided `SessionManager#login` method suffered from a race condition given by the incorrect usage of the
"check-then-act"
pattern (similarly to the `logout` method). In short, the original code checked whether the `sessions` map contained a
key
(i.e. the
`userId`)
and,
based on the check outcome, it'd create a new Session ID value and put it into the map.

Now, assuming there are two
concurrent threads `T_1` and `T_2` both executing the check for the same `userId` and that the map is initially
empty, they would create a new Session ID and put it into the map; even though these operations are atomic,
whichever thread runs last is going to overwrite the value put by the other.

The most appropriate solution to solve this issue is to use the atomic compound operation `putIfAbsent` of 
`ConcurrentHashMap`. In fact, this data structure is designed to allow concurrent reads/writes without being 
fully locked (i.e. it supports bucket-level locking), which substantially improves throughput for read-heavy 
workloads. Alternatively, assuming we are ok with slower read-only accesses, we could also have used a 
`SynchronizedMap` for solving the race condition.

See [SessionManagerTest](../../../../../../test/java/io/unravel/challenge/first/SessionManagerTest.java) for a 
couple of simple unit tests simulating a real use-case. 