package com.company;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by kruczjak on 4/8/15.
 */
class BoundedBuffer {
    public final int m;
    final Lock lock = new ReentrantLock();
    final Condition notFull  = lock.newCondition();
    final Condition notEmpty = lock.newCondition();
    private final Random rand = new Random();
    public final Map<Integer, Integer> mapK = new HashMap<Integer, Integer>();
    public final Map<Integer, Integer> mapP = new HashMap<Integer, Integer>();

    final Object[] items;
    public long dPut, dTake;
    int putptr, takeptr, count;

    BoundedBuffer(int m) {
        this.m = m;
        items = new Object[2*m];
    }

    public void put(Object [] x) throws InterruptedException {
        lock.lock();
        try {
            while (count + x.length > items.length)
                notFull.await();

            for(Object el : x) {
                items[putptr] = el;
                if (++putptr == items.length) putptr = 0;
                ++count;
            }
            dPut++;
            addToMap(mapP, x.length);
//            System.out.println("Put:" + dPut);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public Object take(int toConsume) throws InterruptedException {
        lock.lock();
        try {
            while (count - toConsume < 0)
                notEmpty.await();

            Object[] ret = new Object[toConsume];
            for (int i=0;i<toConsume;i++) {
                ret[i] = items[takeptr];
                if (++takeptr == items.length) takeptr = 0;
                --count;
            }
            dTake++;
            addToMap(mapK, toConsume);
//            System.out.println("Take:" + dTake);
            notFull.signal();
            return ret;
        } finally {
            lock.unlock();
        }
    }

    public void addToMap(Map<Integer, Integer> map, int portion) {
        if (map.containsKey(portion)) {
            int i = map.get(portion);
            map.put(portion, ++i);
        } else
            map.put(portion, 1);
    }
}