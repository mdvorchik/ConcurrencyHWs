package ru.sbt.edu.concurrency.locks;

import org.junit.Test;
import ru.sbt.edu.concurrency.counter.*;
import ru.sbt.edu.concurrency.locks.theory.LockOne;
import ru.sbt.edu.concurrency.locks.theory.LockTwo;
import ru.sbt.edu.concurrency.locks.theory.LockZero;
import ru.sbt.edu.concurrency.locks.theory.PetersonLock;
import ru.sbt.edu.concurrency.util.TwoThreadIds;

import static junit.framework.TestCase.assertEquals;

public class ILockTest {
    @Test
    public void testTheoryLock()  {
        ILock lock = new PetersonLock();
        Counter counter = new ILockCounter(lock);
        //try: 1, 2, 10, 100, 1000
        testCounter(counter, 100);
    }

    @Test
    public void testNaiveCounter()  {
        Counter counter = new MagicCounter();

        testCounter(counter, 10000);
    }

    private void testCounter(Counter counter, int iters) {
        Runnable increment = () -> {
            System.out.println(TwoThreadIds.me());
            for (int i = 0; i < iters; i++) {
                counter.increment();
            }
        };

        Thread t0 = new Thread(increment);
        Thread t1 = new Thread(increment);
        t0.start();
        t1.start();

        try {
            t0.join();
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long count = counter.getValue();
        System.out.println(count);
        assertEquals("Oops! Unexpected Behaviour!", iters * 2, count);
    }
}