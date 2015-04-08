package com.company;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class Main {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("M:");
//        int m = scanner.nextInt();
//        System.out.print("P:");
//        int p = scanner.nextInt();
//        System.out.print("K:");
//        int k = scanner.nextInt();

        try {
            start(1000,10,10);
            start(10000,100,100);
            start(100000,1000,1000);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void start(int m, int p, int k) throws FileNotFoundException {
        BoundedBuffer boundedBuffer = new BoundedBuffer(m);
        Consumer [] consumers = new Consumer[k];
        Producer [] producers = new Producer[p];

        for (int i=0; i<k; i++)
            consumers[i] = new Consumer(boundedBuffer);
        for (int i=0; i<p; i++)
            producers[i] = new Producer(boundedBuffer);

        for(Consumer c : consumers)
            c.start();
        for(Producer pr : producers)
            pr.start();

        try {
            Thread.sleep(1000 * 60 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(Consumer c : consumers)
            c.interrupt();
        for(Producer pr : producers)
            pr.interrupt();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("M:" + m + "P:" + p + "K:" + k + "\nProducent:" + boundedBuffer.dPut + "\nConsumer:" + boundedBuffer.dTake);
        drawHistogram(boundedBuffer.mapK, m, "k");
        drawHistogram(boundedBuffer.mapP, m, "p");
    }

    private static void drawHistogram(Map<Integer,Integer> data, int i, String type) throws FileNotFoundException {
        PrintWriter printWriter = new PrintWriter(i + type + ".txt");
        SortedSet<Integer> keys = new TreeSet<Integer>(data.keySet());
        for(Integer key : keys){
            printWriter.println(key + " " + data.get(key));
        }
        printWriter.close();
    }
}
