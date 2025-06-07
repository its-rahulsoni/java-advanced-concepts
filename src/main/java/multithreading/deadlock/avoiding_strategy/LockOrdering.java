package multithreading.deadlock.avoiding_strategy;

public class LockOrdering {
    private static final Object Lock1 = new Object();
    private static final Object Lock2 = new Object();

    public static void main(String[] args) {
        Runnable task = () -> {
            System.out.println(Thread.currentThread().getName() + ": has started task ....");
            synchronized (Lock1) {
                System.out.println(Thread.currentThread().getName() + ": Holding Lock1 ....");
                try { Thread.sleep(5000); } catch (InterruptedException ignored) {}

                synchronized (Lock2) {
                    System.out.println(Thread.currentThread().getName() + ": Acquired Lock2");
                }
            }
        };

        Thread t1 = new Thread(task, "Thread 1");
        Thread t2 = new Thread(task, "Thread 2");

        t1.start();
        t2.start();
    }
}
