package multithreading.starvation;

/**
 * This one is working fine for Starvation ....
 */
public class StarvationWithNotify {
    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        // Start many worker threads (some of which may get starved)
        for (int i = 1; i <= 10; i++) {
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        resource.useResource(Thread.currentThread().getName());
                        Thread.sleep(200); // Give others a chance
                    }
                } catch (InterruptedException ignored) {}
            }, "Worker-" + i);
            t.start();
        }

        // Background thread makes the resource available periodically
        new Thread(() -> {
            try {
                while (true) {
                    Thread.sleep(500);
                    resource.makeAvailable();
                }
            } catch (InterruptedException ignored) {}
        }).start();
    }
}

class SharedResource {
    private final Object lock = new Object();
    private boolean available = false;

    public void useResource(String name) throws InterruptedException {
        synchronized (lock) {
            while (!available) {
                System.out.println(name + " is waiting...");
                lock.wait(); // Goes into waiting state
            }

            // Resource is available — consume it
            available = false;
            System.out.println(name + " is using the resource.");
        }
    }

    public void makeAvailable() {
        synchronized (lock) {
            available = true;
            /**
             * With notify():
             *
             * Majority of the threads are getting access to the Lock ....
             */
            lock.notify(); // Notifies only one waiting thread ....

            /**
             * With notifyAll():
             *
             * 1. All threads wake up at the same time.
             *
             * 2. One wins the lock → consumes the resource → rest go back to wait.
             *
             * 3. But since one or two threads are consistently fast, they keep winning → others are starved repeatedly.
             *
             * This behavior depends on thread scheduling and CPU timing, which is non-deterministic and JVM-dependent.
             *
             * O/P: Only Thread 1 & 2 are able to get access to lock majority of the time ....
             */
           // lock.notifyAll(); // Notifies all the waiting thread ....

            System.out.println("Resource made available — notified one thread.");
        }
    }
}
