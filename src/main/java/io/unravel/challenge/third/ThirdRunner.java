package io.unravel.challenge.third;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThirdRunner implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ThirdRunner.class, args);
    }

    @Override
    public void run(String... args) {
        LogProcessor processor = new LogProcessor();
        Producer producer = new Producer(processor);
        Consumer consumer = new Consumer(processor);
        producer.start();
        consumer.start();
    }

}
