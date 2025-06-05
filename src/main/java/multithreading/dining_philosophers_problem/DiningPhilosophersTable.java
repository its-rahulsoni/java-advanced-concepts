package multithreading.dining_philosophers_problem;

import java.util.concurrent.Semaphore;

public class DiningPhilosophersTable {

    int totalPhilosophers = 5;

    // Represents forks: 1 fork between each pair of philosophers
    private final Semaphore[] forksSemaphore = new Semaphore[5];

    // Semaphore to limit the number of philosophers eating at once (at most 4 to prevent deadlock)
    private final Semaphore diningTableSemaphore = new Semaphore(4); // Max 4 philosophers at a time

    public DiningPhilosophersTable() {
        // Initialize each fork as a binary semaphore (1 available permit)
        for (int i = 0; i < totalPhilosophers; i++) {
            forksSemaphore[i] = new Semaphore(1);
        }
    }

    // Called by philosopher thread to eat
    public void philosopherStartEating(int philosopherId) throws InterruptedException {

        int leftFork = philosopherId;
        int rightFork = (philosopherId + (totalPhilosophers - 1)) % 5;

        // Start Eating at the Dining Table ....
        diningTableSemaphore.acquire();

        // Pick up left and right forks
        forksSemaphore[leftFork].acquire();
        forksSemaphore[rightFork].acquire();

        // Eat
        System.out.println("Philosopher " + philosopherId + " is eating");
        Thread.sleep(1000);

        // Put down forks
        forksSemaphore[leftFork].release();
        forksSemaphore[rightFork].release();

        System.out.println("Philosopher " + philosopherId + " is done eating");

        // Leave Eating at the Dining Table ....
        diningTableSemaphore.release();
    }

}
