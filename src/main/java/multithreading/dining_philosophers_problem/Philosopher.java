package multithreading.dining_philosophers_problem;

// Philosopher thread
class Philosopher extends Thread {
    private final int id;
    private final DiningPhilosophersTable table;

    public Philosopher(int id, DiningPhilosophersTable table) {
        this.id = id;
        this.table = table;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Think
                System.out.println("Philosopher " + id + " is thinking");
                Thread.sleep((long) (Math.random() * 2000));

                // Try to eat
                table.philosopherStartEating(id);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}