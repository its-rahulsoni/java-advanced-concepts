package multithreading.cyclic_barrier;

import java.util.concurrent.CyclicBarrier;

public class CyclicBarrierExample {

    private static final int THREAD_COUNT = 3;

    public static void main(String[] args) {

        /**
         * Barrier Initialization:
         *
         * 1.Creates a barrier for 3 threads.
         * 2.The Runnable is a barrier action executed once when all threads arrive.
         */
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () -> {
            System.out.println("All threads reached the barrier. Let's proceed to next phase.");
        });

        for (int i = 1; i <= THREAD_COUNT; i++) {
            Thread t = new Thread(new Task(barrier), "Thread-" + i);
            t.start();
        }
    }

    static class Task implements Runnable {
        private final CyclicBarrier barrier;

        Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + " is doing some work before the barrier.");
                Thread.sleep((long) (Math.random() * 3000));  // Simulate work

                System.out.println(Thread.currentThread().getName() + " is waiting at the barrier.");

                /**
                 *
                 * When all threads reach the barrier:
                 *
                 * 1. The barrier action runs once.
                 *
                 * 2. All threads are simultaneously released from the await() call.
                 *
                 * 3. Threads continue their work.
                 */
                barrier.await();  // The thread blocks here until all 3 threads call await() ....

                System.out.println(Thread.currentThread().getName() + " passed the barrier and is doing more work.");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
