package multithreading.reader_write_problem;

// Writer thread
class Writer implements Runnable {
    private final SharedData sharedData;
    private final String name;
    private final int newValue;

    public Writer(SharedData sharedData, String name, int newValue) {
        this.sharedData = sharedData;
        this.name = name;
        this.newValue = newValue;
    }

    @Override
    public void run() {
        sharedData.writeData(name, newValue);
    }
}
