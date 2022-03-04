package ru.sbt.edu.concurrency.counter;

public class MutexCounter implements Counter {
    private long value;

    @Override
    synchronized
    public void increment() {
        value++;
    }

    @Override
    synchronized
    public long getValue() {
        return value;
    }
}
