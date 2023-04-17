package au.edu.sydney.soft3202.tutorials.week8.uow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlowDatabase {
    private Map<Integer, String> documentContents;

    public SlowDatabase() {
        this.documentContents = new HashMap<>();
    }

    public void saveDocumentContents(int key, String documentContents) {
        System.out.println("Saving document contents to database");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
        this.documentContents.put(key, documentContents);
    }

    public String getDocumentContents(int key) {
        System.out.println("Getting document contents from database");
        try {
            Thread.sleep(1);
        } catch (InterruptedException ignored) {}
        return this.documentContents.get(key);
    }

    public List<Integer> getDocumentKeys() {
        System.out.println("Getting all document keys from database");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {}
        return new ArrayList<>(documentContents.keySet());
    }
}
