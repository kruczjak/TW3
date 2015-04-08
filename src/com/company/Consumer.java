package com.company;

import java.util.Random;

/**
 * Created by kruczjak on 4/8/15.
 */
public class Consumer extends Thread {
    private final BoundedBuffer boundedBuffer;
    private final Random random = new Random();


    public Consumer(BoundedBuffer boundedBuffer) {
        this.boundedBuffer = boundedBuffer;
    }

    @Override
    public void run() {
        while(true) {
            int toConsume = random.nextInt(boundedBuffer.m);
            try {
                boundedBuffer.take(toConsume);
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}
