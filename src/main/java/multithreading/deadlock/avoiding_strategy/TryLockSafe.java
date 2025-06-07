package multithreading.deadlock.avoiding_strategy;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

public class TryLockSafe {
    private static final Lock Lock1 = new ReentrantLock();
    private static final Lock Lock2 = new ReentrantLock();

    public static void main(String[] args) {
        Runnable task = () -> {
            while (true) {
                try {
                    if (Lock1.tryLock(1000, TimeUnit.MILLISECONDS)) {
                        try {
                            if (Lock2.tryLock(1000, TimeUnit.MILLISECONDS)) {
                                try {
                                    System.out.println(Thread.currentThread().getName() + ": Acquired both locks.");
                                    break;
                                } finally {
                                    Lock2.unlock();
                                }
                            }
                        } finally {
                            Lock1.unlock();
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(Thread.currentThread().getName() + ": Couldn't acquire both locks. Retrying...");
                try { Thread.sleep(100); } catch (InterruptedException ignored) {}
            }
        };

        Thread t1 = new Thread(task, "Thread 1");
        Thread t2 = new Thread(task, "Thread 2");

        t1.start();
        t2.start();
    }
}
