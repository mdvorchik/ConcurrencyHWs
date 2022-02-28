package ru.sbt.edu.concurrency.counter;

import ru.sbt.edu.concurrency.util.ThreadID;

public class MagicCounter implements Counter {
    private final int[] level = new int[Runtime.getRuntime().availableProcessors()];
    private final int[] victim = new int[Runtime.getRuntime().availableProcessors()];
    private long value;

    public MagicCounter() {
        for (int i = 0; i < level.length; i++) {
            level[i] = 0;
            victim[i] = 0;
        }
    }

    //filter lock
    public void lock() {
        int me = ThreadID.get();
        for (int i = 1; i < level.length; i++) {
            level[me] = i;
            victim[i] = me;
            while (checkConflictExistence(i, me)) {
            }
        }
    }

    public void unlock() {
        int me = ThreadID.get();
        level[me] = 0;
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
        for (int k = 0; k < level.length; k++) {
            if (k != me && (level[k] >= i && victim[i] == me)) {
                return true;
            }
        }
        return false;
    }
}
