package com.redsoft.gda.stat;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class WorkSyncConstraints {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstFetchCondition = lock.newCondition();
    private final Condition addNewRecordCondition = lock.newCondition();

    public void lock() {
        lock.lock();
    }

    public void unlock() {
        lock.unlock();
    }

    public void awaitFirstFetchCondition() {
        try {
            firstFetchCondition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doneFirstFetchCondition() {
        firstFetchCondition.signal();
    }

    public void awaitAddNewRecordCondition() {
        try {
            addNewRecordCondition.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void doneAddNewRecordCondition() {
        addNewRecordCondition.signal();
    }
}
