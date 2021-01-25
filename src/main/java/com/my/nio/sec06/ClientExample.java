package com.my.nio.sec06;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientExample {
    public static void main(String[] args) throws IOException, InterruptedException {
        ExecutorService service = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors()
        );


        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello World " + Thread.currentThread().getName());
            }
        });

        for (int i = 0 ; i < 100; i++)
        {

            System.out.print(i + " : ");
            service.submit(th);
        }

        System.out.println("씨발련아");
        service.shutdown();
    }
}
