package au.edu.sydney.soft3202.task1.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {

    private static Map<String, Cart> instances = new HashMap<>();

    private Map<String, Integer> items = new HashMap<>();

    private String cartOwnerId;

    private Cart(String id) {
        this.items = new HashMap<>();
        cartOwnerId = id;
        items.put("apple", 0);
        items.put("orange", 0);
        items.put("pear", 0);
        items.put("banana", 0);
    }

    public static Cart getCart(String id) {
        if (!instances.containsKey(id)) {
            instances.put(id, new Cart(id));
        }

        return instances.get(id);
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void updateItemCount(String item, Integer count) {
        items.replace(item, count);
    }

    public void insertNewItem(String item, Integer count) {
        items.put(item, 0);
    }
}
