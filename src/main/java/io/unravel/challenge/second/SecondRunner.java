package io.unravel.challenge.second;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.UUID;


@SpringBootApplication
public class SecondRunner implements CommandLineRunner {
    private static final Logger LOG = LoggerFactory.getLogger(SecondRunner.class);

    public static void main(String[] args) {
        SpringApplication.run(SecondRunner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        // Uncomment the following for running the app with the original manager
//        benchmarkOriginalMemoryManager();
        benchmarkQueuedMemoryManager();
    }

    private static void benchmarkQueuedMemoryManager() throws InterruptedException {
        long i = 0;
        while (true) {
            LOG.info("At step " + i);
            QueuedMemoryManager.addSessionData(UUID.randomUUID().toString());
            Thread.sleep(500);
            i++;
        }
    }

    private static void benchmarkOriginalMemoryManager() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            OriginalMemoryManager.addSessionData(UUID.randomUUID().toString());
            Thread.sleep(1000);
            LOG.info("At step " + i);
        }
    }
}
