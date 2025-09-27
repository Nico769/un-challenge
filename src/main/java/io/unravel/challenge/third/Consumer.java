package io.unravel.challenge.third;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class Consumer extends Thread {
    private static final Logger LOG = LoggerFactory.getLogger(Consumer.class);
    private LogProcessor processor;

    public Consumer(LogProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        try {
            while (true) {
                Thread.sleep(1500);
                String log = processor.consumeLog();
                LOG.info("Consumed: " + log);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
