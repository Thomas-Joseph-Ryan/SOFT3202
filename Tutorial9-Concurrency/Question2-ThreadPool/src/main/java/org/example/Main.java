package org.example;

import java.util.concurrent.ExecutionException;

public class Main {
//    TODO QUESTION 2
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Servant servant = new Servant();
        Scheduler scheduler = new Scheduler(servant);
        Proxy proxy = new Proxy(scheduler);
        Callback callback = new Callback() {
            @Override
            public void onComplete(String result) {
                System.out.println(result);
            }
        };

        proxy.download("tomisawesome.com/download/memories", 42.15, callback);
        System.out.println("Request submitted, waiting for result...");
        scheduler.end();
    }
}