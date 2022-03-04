package ru.sbt.edu.concurrency.counter;

import java.util.concurrent.Semaphore;

public class ConcurrentCounter implements Counter {
    private final Semaphore semaphore = new Semaphore(1);
    private long value;

    @Override
    public void increment() {
        try {
            semaphore.acquire();
            value++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    @Override
    public long getValue() {
        try {
            semaphore.acquire();
            return value;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            semaphore.release();
        }
    }
}
