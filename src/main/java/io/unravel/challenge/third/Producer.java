package io.unravel.challenge.third;

class Producer extends Thread {
    private LogProcessor processor;

    public Producer(LogProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            processor.produceLog("Log " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
