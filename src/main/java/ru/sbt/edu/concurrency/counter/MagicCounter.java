package ru.sbt.edu.concurrency.counter;

import ru.sbt.edu.concurrency.util.ThreadID;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class MagicCounter implements Counter {
    private final List<Integer> level = new CopyOnWriteArrayList<>();
    private final List<Integer> victim = new CopyOnWriteArrayList<>();
    private long value;

    public MagicCounter() {
        for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
            level.add(0);
            victim.add(0);
        }
    }

    //filter lock
    public void lock() {
        int me = ThreadID.get();
        for (int i = 1; i < level.size(); i++) {
            level.set(me, i);
            victim.set(i, me);
            while (checkConflictExistence(i, me)) {
            }
        }
    }

    public void unlock() {
        int me = ThreadID.get();
        level.set(me, 0);
    }

    @Override
    public void increment() {
        lock();
        try {
            value++;
        } finally {
            unlock();
        }
    }

    @Override
    public long getValue() {
        return value;
    }

    private boolean checkConflictExistence(int i, int me) {
        for (int k = 0; k < level.size(); k++) {
            if (k != me && (level.get(k) >= i && victim.get(i) == me)) {
                return true;
            }
        }
        return false;
    }
}
