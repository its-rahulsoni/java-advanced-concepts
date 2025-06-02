package multithreading.reader_write_problem;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// Shared resource
class SharedData {
    private int data = 0;  // Simulated shared resource
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    // Reader thread reads the data
    public void readData(String readerName) {
        lock.readLock().lock();  // Acquire read lock
        try {
            System.out.println(readerName + " is reading: " + data);
            Thread.sleep(1000); // Simulate time taken to read
            System.out.println(readerName + " finished reading");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.readLock().unlock();  // Release read lock
        }
    }

    // Writer thread writes/updates the data
    public void writeData(String writerName, int newValue) {
        lock.writeLock().lock();  // Acquire write lock
        try {
            System.out.println(writerName + " is writing: " + newValue);
            Thread.sleep(1500); // Simulate time taken to write
            this.data = newValue;
            System.out.println(writerName + " finished writing");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            lock.writeLock().unlock();  // Release write lock
        }
    }
}






