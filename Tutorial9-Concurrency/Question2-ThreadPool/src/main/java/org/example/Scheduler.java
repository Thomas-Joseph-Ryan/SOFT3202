package org.example;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Scheduler {
    private final ExecutorService executorService;
    private final Servant servant;

    public Scheduler(Servant servant) {
        this.servant = servant;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public void schedule(String url, Double dataStart, Double dataEnd, Callback callback) {
        executorService.submit(() -> {
                String res = servant.processData(url, dataStart, dataEnd);
                callback.onComplete(res);
            }
        );
    }

    public void end() {
        executorService.shutdown();
    }

}
