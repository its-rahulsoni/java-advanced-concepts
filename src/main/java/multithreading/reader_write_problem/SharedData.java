package multithreading.reader_write_problem;

import java.util.concurrent.locks.ReentrantReadWriteLock;

// Shared resource
class SharedData {
    private int data = 0;  // Simulated shared resource

    /**
     * ReentrantReadWriteLock class of Java is an implementation of ReadWriteLock, that also supports ReentrantLock functionality.
     *
     * The ReadWriteLock is a pair of associated locks, one for read-only operations and one for writing. Whereas, the ReentrantLock is a re-entrant mutual exclusion Lock with the same behavior as the implicit monitor lock accessed using synchronized methods and statements, but with some more extended capabilities.
     *
     * ReadWriteLock in Java
     *
     * Even in a multi-threading application, multiple reads can occur simultaneously for a shared resource. It is only when multiple writes happen simultaneously or intermix of read and write that there is a chance of writing the wrong value or reading the wrong value.
     *
     * ReadWriteLock in Java uses the same idea in order to boost the performance by having separate pair of locks. A ReadWriteLock maintains a pair of associated locks-
     *
     * One for read-only operations; and
     * one for writing.
     * The read lock may be held simultaneously by multiple reader threads, so long as there are no writers. The write lock is exclusive.
     * @param readerName
     */
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






