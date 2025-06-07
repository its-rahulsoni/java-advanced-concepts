package multithreading.deadlock.avoiding_strategy;

public class AvoidNestedLocks {
    private static final Object lock1 = new Object();
    private static final Object lock2 = new Object();

    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {

            /**
             * Here:
             *
             * Thread 1 acquires and releases lock1 before acquiring lock2.
             *
             * There's no overlap — both locks are not held at the same time.
             *
             * This means no circular wait can happen.
             *
             * Thus, this avoids nested locking and prevents deadlock.
             */
            synchronized (lock1) {
                System.out.println("Thread 1: Working with lock1");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}

            System.out.println("Thread 1: Trying to acquire Lock2...");
            synchronized (lock2) {
                System.out.println("Thread 1: Working with lock2");
            }
        });

        Thread t2 = new Thread(() -> {

            /**
             * Here:
             *
             * Thread 1 acquires and releases lock1 before acquiring lock2.
             *
             * There's no overlap — both locks are not held at the same time.
             *
             * This means no circular wait can happen.
             *
             * Thus, this avoids nested locking and prevents deadlock.
             */
            synchronized (lock2) {
                System.out.println("Thread 2: Working with lock2");
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {}

            System.out.println("Thread 2: Trying to acquire Lock1...");
            synchronized (lock1) {
                System.out.println("Thread 2: Working with lock1");
            }
        });

        t1.start();
        t2.start();
    }
}
