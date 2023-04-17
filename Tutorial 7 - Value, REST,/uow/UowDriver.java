package au.edu.sydney.soft3202.tutorials.week8.uow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UowDriver {

    private SlowDatabase database;
    private Map<Integer, String> transactions = new HashMap<>();

    public UowDriver(SlowDatabase database) {
        this.database = database;
    }

    public void addTransaction(int id, String content) {
        if (transactions.containsKey(id)) {
            transactions.replace(id, content);
        } else {
            transactions.put(id, content);
        }
    }

    public void commit() {
        for (int id : transactions.keySet()) {
            database.saveDocumentContents(id, transactions.get(id));
        }
        transactions.clear();
    }

}
