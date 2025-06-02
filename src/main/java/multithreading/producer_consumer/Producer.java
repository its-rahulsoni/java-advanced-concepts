package multithreading.producer_consumer;

public class Producer implements Runnable {
    private final SharedResource sharedResource;

    public Producer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        try {
            sharedResource.produce();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
