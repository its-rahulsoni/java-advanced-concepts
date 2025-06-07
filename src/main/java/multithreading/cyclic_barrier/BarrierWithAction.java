package multithreading.cyclic_barrier;

import java.util.concurrent.CyclicBarrier;

public class BarrierWithAction {

    public static void main(String[] args) {

        Runnable barrierAction = () ->
                System.out.println("✅ All threads arrived at barrier — barrierAction is executing!");

        CyclicBarrier barrier = new CyclicBarrier(3, barrierAction);

        Runnable task = () -> {
            try {
                System.out.println(Thread.currentThread().getName() + " is ready.");
                Thread.sleep((long) (Math.random() * 1000));
                barrier.await();
                System.out.println(Thread.currentThread().getName() + " passed the barrier.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        new Thread(task, "Thread-1").start();
        new Thread(task, "Thread-2").start();
        new Thread(task, "Thread-3").start();
    }
}

