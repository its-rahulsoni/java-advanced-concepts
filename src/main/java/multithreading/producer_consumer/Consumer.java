package multithreading.producer_consumer;

public class Consumer implements Runnable {
    private final SharedResource sharedResource;

    public Consumer(SharedResource sharedResource) {
        this.sharedResource = sharedResource;
    }

    @Override
    public void run() {
        try {
            sharedResource.consume();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

