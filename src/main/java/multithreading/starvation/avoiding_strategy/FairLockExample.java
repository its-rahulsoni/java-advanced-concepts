package multithreading.starvation.avoiding_strategy;

import java.util.concurrent.locks.ReentrantLock;

public class FairLockExample {

    /**
     * ReentrantLock(true) ensures that threads get lock access in the order they requested it (FIFO) — preventing starvation.
     * Note: The "true" in Lock creation ....
     */
    private static final ReentrantLock lock = new ReentrantLock(true); // fair lock

    public static void main(String[] args) {
        Runnable task = () -> {
            while (true) {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + " acquired the lock");
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {
                } finally {
                    lock.unlock();
                }
            }
        };

        Thread t1 = new Thread(task, "Thread-A");
        Thread t2 = new Thread(task, "Thread-B");
        Thread t3 = new Thread(task, "Thread-C");

        t1.start();
        t2.start();
        t3.start();
    }
}
