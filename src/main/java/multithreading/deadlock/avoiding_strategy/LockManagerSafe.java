package multithreading.deadlock.avoiding_strategy;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class LockManagerSafe {
    static class LockManager {
        private final List<ReentrantLock> locks;

        public LockManager(ReentrantLock... locks) {
            this.locks = Arrays.asList(locks);
        }

        public void lockAll() {
            // Sort to acquire locks in an order ....
            locks.stream()
                    .sorted((a, b) -> Integer.compare(System.identityHashCode(a), System.identityHashCode(b)))
                    .forEach(lock -> lock.lock());

        }

        public void unlockAll() {
            locks.forEach(ReentrantLock::unlock);
        }
    }

    public static void main(String[] args) {
        ReentrantLock lock1 = new ReentrantLock();
        ReentrantLock lock2 = new ReentrantLock();

        LockManager manager = new LockManager(lock1, lock2);

        Runnable task = () -> {
            manager.lockAll();
            try {
                System.out.println(Thread.currentThread().getName() + ": Acquired locks via manager.");
            } finally {
                manager.unlockAll();
            }
        };

        new Thread(task, "Thread 1").start();
        new Thread(task, "Thread 2").start();
    }
}
