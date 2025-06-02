package multithreading.reader_write_problem;

// Reader thread
class Reader implements Runnable {
    private final SharedData sharedData;
    private final String name;

    public Reader(SharedData sharedData, String name) {
        this.sharedData = sharedData;
        this.name = name;
    }

    @Override
    public void run() {
        sharedData.readData(name);
    }
}