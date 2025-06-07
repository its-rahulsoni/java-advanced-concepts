package multithreading.deadlock;

public class DeadLockDemo {
    private static final Object Lock1 = new Object();
    private static final Object Lock2 = new Object();

    /**
     * Thread 1 acquires Lock1, then waits for Lock2.
     *
     * Thread 2 acquires Lock2, then waits for Lock1.
     *
     * Both threads are stuck forever.
     *
     * Reason - The locks are acquired in different orders by the threads, it may result in a deadlock.
     * This is an example of NESTED LOCKS ....
     */
    public static void main(String[] args) {

        /**
         * Here:
         *
         * Thread 1 holds Lock1, and tries to acquire Lock2.
         *
         * If Thread 2 does the reverse (holds Lock2 and waits for Lock1), both will wait forever → Deadlock.
         *
         * This is nested locking, and dangerous if both threads do it in different orders.
         */
        Thread t1 = new Thread(() -> {
            synchronized (Lock1) {
                System.out.println("Thread 1: Holding Lock1...");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}

                System.out.println("Thread 1: Waiting for Lock2...");
                synchronized (Lock2) {
                    System.out.println("Thread 1: Acquired Lock2!");
                }
            } // Both the locks are acquired
        });

        Thread t2 = new Thread(() -> {

            /**
             * Here:
             *
             * Thread 1 holds Lock1, and tries to acquire Lock2.
             *
             * If Thread 2 does the reverse (holds Lock2 and waits for Lock1), both will wait forever → Deadlock.
             *
             * This is nested locking, and dangerous if both threads do it in different orders.
             */
            synchronized (Lock2) {
                System.out.println("Thread 2: Holding Lock2...");

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ignored) {}

                System.out.println("Thread 2: Waiting for Lock1...");
                synchronized (Lock1) {
                    System.out.println("Thread 2: Acquired Lock1!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
