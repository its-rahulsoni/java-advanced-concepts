package multithreading.dining_philosophers_problem;

public class DiningPhilosophersProblem {

    public static void main(String[] args) {
        DiningPhilosophersTable table = new DiningPhilosophersTable();

        // Create and start 5 philosopher threads
        for (int i = 0; i < 5; i++) {
            new Philosopher(i, table).start();
        }
    }

}
