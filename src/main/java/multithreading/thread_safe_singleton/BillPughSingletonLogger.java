package multithreading.thread_safe_singleton;

public class BillPughSingletonLogger {
    private BillPughSingletonLogger() {
        System.out.println("Logger Initialized...");
    }

    private static class Holder {
        private static final BillPughSingletonLogger INSTANCE = new BillPughSingletonLogger();
    }

    public static BillPughSingletonLogger getInstance() {
        return Holder.INSTANCE;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }

    /**
     * ✅ How it works:
     * 1. JVM loads Holder class only when getInstance() is called.
     *
     * 2. Class loading is thread-safe.
     *
     * ✅ Pros:
     * 1. Lazy initialization
     *
     * 2. High performance
     *
     * 3. No synchronization overhead
     */
    public static void main(String[] args) {
        BillPughSingletonLogger.getInstance().log("Starting application...");
        BillPughSingletonLogger.getInstance().log("Fetching user data...");
        BillPughSingletonLogger.getInstance().log("App shutdown.");
    }
}
