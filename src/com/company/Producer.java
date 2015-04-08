package com.company;

import java.util.Objects;
import java.util.Random;

/**
 * Created by kruczjak on 4/8/15.
 */
public class Producer extends Thread {
    private final BoundedBuffer boundedBuffer;
    private final Random rand = new Random();
    public Producer(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @Override
    public void run() {
        while (true) {
            int toPut = rand.nextInt(boundedBuffer.m);
            Object[] items =  new Object[toPut];
            try {
                boundedBuffer.put(items);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
