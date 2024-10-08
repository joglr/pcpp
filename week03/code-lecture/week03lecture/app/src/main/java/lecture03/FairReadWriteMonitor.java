// For week 2
// raup@itu.dk * 01/09/2021
package lecture03;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class FairReadWriteMonitor {
    private volatile int readsAcquires = 0;
    private volatile int readsReleases = 0;
    private volatile boolean writer    = false;
    private final Lock lock            = new ReentrantLock();
    private final Condition condition  = lock.newCondition();

    //////////////////////////
    // Read lock operations //
    //////////////////////////

    public void readLock() {
        lock.lock();
        try {
            while(writer)
                condition.await();
            readsAcquires++;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void readUnlock() {
        lock.lock();
        try {
            readsReleases++;
            if(readsAcquires==readsReleases)
                condition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }


    ///////////////////////////
    // Write lock operations //
    ///////////////////////////

    public void writeLock() {
        lock.lock();
        try {
            while(writer)
                condition.await();
            writer=true;
            while(readsAcquires != readsReleases)
                condition.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void writeUnlock() {
        lock.lock();
        try {
            writer=false;
            condition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

}
