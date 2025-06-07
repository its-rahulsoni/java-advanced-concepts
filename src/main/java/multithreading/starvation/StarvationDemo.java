package multithreading.starvation;

public class StarvationDemo {

    public static void main(String[] args) {
        Runnable highPriorityTask = () -> {
            while (true) {
                System.out.println("High Priority Thread running");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        };

        Runnable lowPriorityTask = () -> {
            while (true) {
                System.out.println("Low Priority Thread running");
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) {}
            }
        };

        Thread high1 = new Thread(highPriorityTask);
        Thread high2 = new Thread(highPriorityTask);
        Thread high3 = new Thread(highPriorityTask);

        Thread low = new Thread(lowPriorityTask);

        high1.setPriority(Thread.MAX_PRIORITY);
        high2.setPriority(Thread.MAX_PRIORITY);
        high3.setPriority(Thread.MAX_PRIORITY);

        low.setPriority(Thread.MIN_PRIORITY);

        high1.start();
        high2.start();
        high3.start();
        low.start();
    }
}
