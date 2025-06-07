package multithreading.starvation;

import java.util.concurrent.locks.ReentrantLock;

public class StarvationWithUnfairLock {

    private static final ReentrantLock lock = new ReentrantLock(); // unfair by default

    public static void main(String[] args) {
        // Create 1 hogging thread
        Thread greedyThread = new Thread(() -> {
            while (true) {
                lock.lock();
                try {
                    System.out.println("GreedyThread got the lock");
                    Thread.sleep(100); // holds the lock for a while
                } catch (InterruptedException ignored) {
                } finally {
                    lock.unlock();
                }

                // Short pause before re-acquiring
//                try {
//                   // Thread.sleep(10);
//                } catch (InterruptedException ignored) {}
            }
        });

        // Create 3 victim threads
        for (int i = 1; i <= 30; i++) {
            Thread t = new Thread(() -> {
                while (true) {
                    lock.lock();
                    try {
                        System.out.println(Thread.currentThread().getName() + " got the lock");
                        Thread.sleep(50); // short work
                    } catch (InterruptedException ignored) {
                    } finally {
                        lock.unlock();
                    }
                    try {
                        Thread.sleep(100); // give chance to others
                    } catch (InterruptedException ignored) {}
                }
            }, "VictimThread-" + i);

            t.start();
        }

        greedyThread.setName("GreedyThread");
        greedyThread.start();
    }
}
