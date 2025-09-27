# Exercise 5 Solution

For the last exercise, I've implemented a simple endpoint `/benchmark` which simulates a simple DB workload with a 2:1 
read-to-write ratio (the read operation is a table `COUNT`, while the write operation stores a new `Issue`). 

Then, I've used Apache Bench to load test the app (running on a 8 core laptop) under several variations of the 
number of 
requests and
concurrent connections. While running the app, I've monitored the HikariCP connection pool metrics through 
VisualVM's MBeans tab. As we can see in the following screenshot, the Total connections peaked at 20, indicating that 
the provided `maximum-pool-size` of 100 is too high: this makes sense since, as suggested by [HikariCP docs]
(https://github.com/brettwooldridge/HikariCP/wiki/About-Pool-Sizing), the pool size should be tuned by starting from 
this formula:

```
connections = ((core_count * 2) + effective_spindle_count)
            = (8 cores * 2) + 1 = 17
```

![1](./1.png)

I've also reduced the `connection-timeout` property to 3000 since a shorter timeout avoids long waits. Assuming a 
more realistic workload (e.g. table joins, diversified read/write patterns, table indexing, etc.), I'd keep repeating 
the load tests, writing down the app/DB CPU metrics, the HikariCP metrics and the executed queries latencies until a 
good balance of these is reached.   
