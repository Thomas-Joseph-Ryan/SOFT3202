package org.example;

public class Servant {
    public String processData(String url, Double dataStart, Double dataEnd) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String string =  "Downloaded from url " + url + " from : " + dataStart + " to : " + dataEnd;
        return string;
    }
}
