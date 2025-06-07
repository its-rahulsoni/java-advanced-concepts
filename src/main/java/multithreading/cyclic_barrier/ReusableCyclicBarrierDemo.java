package multithreading.cyclic_barrier;

import java.util.concurrent.CyclicBarrier;

public class ReusableCyclicBarrierDemo {

    static class Task extends Thread {
        private final CyclicBarrier barrier;

        public Task(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        public void run() {
            try {
                for (int i = 1; i <= 3; i++) {  // Do this 3 times
                    System.out.println(Thread.currentThread().getName() + " reached barrier for round " + i);
                    barrier.await();  // Wait for others
                    System.out.println(Thread.currentThread().getName() + " passed barrier for round " + i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        final int THREAD_COUNT = 3;
        CyclicBarrier barrier = new CyclicBarrier(THREAD_COUNT, () ->
                System.out.println("== All threads reached the barrier, moving to next round ==\n")
        );

        for (int i = 0; i < THREAD_COUNT; i++) {
            new Task(barrier).start();
        }
    }
}

