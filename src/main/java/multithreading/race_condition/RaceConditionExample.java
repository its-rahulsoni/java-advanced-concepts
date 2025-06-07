package multithreading.race_condition;

public class RaceConditionExample {
    private int counter = 0;

    public void increment() {
        counter++; // NOT atomic
    }

    public int getCounter() {
        return counter;
    }

    /**
     * Final Counter: 15836, 17492, or any random number < 20000
     *
     * Why Does This Happen?
     * counter++ is not atomic. It is actually 3 operations:
     *
     * 1. Read the value of counter
     *
     * 2. Add 1
     *
     * 3. Write the new value back
     *
     * If two threads execute this sequence simultaneously, they can interfere with each other’s result.
     */
    public static void main(String[] args) throws InterruptedException {
        RaceConditionExample example = new RaceConditionExample();

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                example.increment();
            }
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10000; i++) {
                example.increment();
            }
        });

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Final Counter: " + example.getCounter());
    }
}
