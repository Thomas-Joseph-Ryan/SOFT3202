package org.example;

public class Proxy {
    private final Scheduler scheduler;

    public Proxy(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /*
    size is the size in kB
     */
    public void download(String url, Double size, Callback callback) {
        Double downloaded = 0.0;
        while (downloaded.compareTo(size) < 0) {
            Double increment = 10.0;
            if (downloaded + increment > size) {
                increment = size - downloaded;
            }
            scheduler.schedule(url, downloaded, downloaded + increment, callback);
            downloaded += increment;
        }
//        return dataList;
    }
}
