package au.edu.sydney.soft3202.task1.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

/**
 * Container for items to be purchased
 */
public class ShoppingBasket {

    HashMap<String, Integer> items;
    HashMap<String, Double> values;

    String[] names = {"apple", "orange", "pear", "banana"};

    private static Map<String, ShoppingBasket> instances = new HashMap<>();

    /**
     * Creates a new, empty ShoppingBasket object
     */
    private ShoppingBasket() {
        this.items = new HashMap<>();
        this.values = new HashMap<>();

        for (String name: names) {
            this.items.put(name, 0);
        }

        this.values.put("apple", 2.5);
        this.values.put("orange", 1.25);
        this.values.put("pear", 3.00);
        this.values.put("banana", 4.95);
    }

    private ShoppingBasket(List<Item> items) {
        this.items = new HashMap<>();
        this.values = new HashMap<>();

        for (Item item : items) {
            this.items.put(item.item(), item.count());
            this.values.put(item.item(), item.cost());
        }
    }

    public static ShoppingBasket getInstance(String userID) {
        return instances.get(userID);
    }

    public static ShoppingBasket getInstance(String userID, List<Item> itemsFromDatabase) {
        if (!instances.containsKey(userID)) {
            if (itemsFromDatabase.size() == 0) {
                instances.put(userID, new ShoppingBasket());
            } else {
                instances.put(userID, new ShoppingBasket(itemsFromDatabase));
            }
        }

        return instances.get(userID);
    }

    public static void resetInstance() {
        instances.clear();
    }

    public List<Item> getItemsAsItemList() {
        List<Item> itemList = new ArrayList<>();
        for (Map.Entry<String, Integer> itemEntry : items.entrySet()) {
            String itemName = itemEntry.getKey();
            Integer count = itemEntry.getValue();
            Double cost = values.get(itemName);
            Item item = new Item(itemName, count, cost);
            itemList.add(item);
        }
        return itemList;
    }

    /**
     * Adds an item to the shopping basket.
     *
     * @param item  The item to be added. Must match one of ‘apple’, ‘orange’, ‘pear’, or ‘banana’, in any case.
     * @param count The count of the item to be added. Must be 1 or more. It
     * allows only Integer.INT_MAX number of items of a kind to be stored. If
     * items are added after INT_MAX, the parameter requirements will be breached.
     * @throws IllegalArgumentException If any parameter requirements are breached.
     */
    public void addItem(String item, int count) throws IllegalArgumentException {
        if (item == null) throw new IllegalArgumentException("Item is invalid");
        String stringItem = item.toLowerCase();

        if (!this.items.containsKey(stringItem)) throw new IllegalArgumentException("Item " + stringItem + " is not present.");
        if (count < 1) throw new IllegalArgumentException("Item " + item + " has invalid count.");

        Integer itemVal = this.items.get(stringItem);
        if (itemVal == Integer.MAX_VALUE) throw new IllegalArgumentException("Item " + item + " has reached maximum count.");

        this.items.put(stringItem, itemVal + count);
    }

    /**
     * Removes an item from the shopping basket, based on a case-insensitive but otherwise exact match.
     *
     * @param item  The item to be removed.
     * @param count The count of the item to be added. Must be 1 or more.
     * @return False if the item was not found in the basket, or if the count was higher than the amount of this item currently present, otherwise true.
     * @throws IllegalArgumentException If any parameter requirements are breached.
     */
    public boolean removeItem(String item, int count) throws IllegalArgumentException {
        if (item == null) throw new IllegalArgumentException("Item is invalid");
        String stringItem = item.toLowerCase();

        if (!this.items.containsKey(stringItem)) return false;
        if (count < 1) throw new IllegalArgumentException(count + " is invalid count.");

        Integer itemVal = this.items.get(stringItem);

        Integer newVal = itemVal - count;
        if (newVal < 0) return false;
        this.items.put(stringItem, newVal);

        return true;
    }

    /**
     * Updates count of item in basket
     * @param item item to be updated
     * @param count new count of item
     */
    public void updateItemCount(String item, Integer count) throws IllegalArgumentException {

//        if (item == null) throw new IllegalArgumentException("Item is invalid"); redundant because
//        string will only ever be provided through fixed input

        String stringItem = item.toLowerCase();

        if (!this.items.containsKey(stringItem)) {
            return;
        }
        if (count < 0) throw new IllegalArgumentException(count + " is invalid count.");

        this.items.replace(stringItem, count);
    }

    /**
     * Inserts a new item for this specific instance of the shopping basket.
     * If item name is already present, nothing will be changed.
     * Throws IllegalArgumentException if item cost is < 0
     * @param item New item name
     * @param cost New item cost
     */
    public void insertNewItem(String item, Double cost) {
        if (this.items.containsKey(item.toLowerCase())) {
            return;
        }
        if (cost < 0) {
            throw new IllegalArgumentException("cost " + cost + " must be greater then zero");
        }

        this.values.put(item, cost);
        this.items.put(item, 0);
    }

    public void deleteExistingItem(String itemName) {
        this.items.remove(itemName);
        this.values.remove(itemName);
    }

    /**
     * Gets the contents of the ShoppingBasket.
     *
     * @return A list of items and counts of each item in the basket. This list is a copy and any modifications will not modify the existing basket.
     */
    public List<Entry<String, Integer>> getItems() {
//        ArrayList<Entry<String, Integer>> originalItems = new ArrayList<Entry<String, Integer>>(this.items.entrySet());
//        ArrayList<Entry<String, Integer>> copyItems = new ArrayList<Entry<String, Integer>>();
//
//        int index = 0;
//
//        for(Entry<String,Integer> entry: originalItems){
//            copyItems.add(index, Map.entry(entry.getKey(), entry.getValue()));
//            index++;
//        }

        return this.items.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toList());
    }

    /**
     * Gets the contents of the ShoppingBasket.
     *
     * @return A list of items and counts of each item in the basket. This list is a copy and any modifications will not modify the existing basket.
     */
    public List<Entry<String, Double>> getItemsAndValues() {
        ArrayList<Entry<String, Double>> originalItems = new ArrayList<Entry<String, Double>>(this.values.entrySet());
        ArrayList<Entry<String, Double>> copyItems = new ArrayList<Entry<String, Double>>();

        int index = 0;

        for(Entry<String,Double> entry: originalItems){
            copyItems.add(index, Map.entry(entry.getKey(), entry.getValue()));
            index++;
        }

        return copyItems;
    }

    public void updateName(String oldName, String newName) {
        Integer count = this.items.get(oldName);
        Double cost = this.values.get(oldName);

        this.items.remove(oldName);
        this.values.remove(oldName);

        this.items.put(newName, count);
        this.values.put(newName, cost);
    }

    public void updateCost(String name, Double cost) {
        this.values.replace(name, cost);
    }

    /**
     * Gets the current dollar value of the ShoppingBasket based on the following values: Apples: $2.50, Oranges: $1.25, Pears: $3.00, Bananas: $4.95
     *
     * @return Null if the ShoppingBasket is empty, otherwise the total dollar value.
     */
    public Double getValue() {
        Double val = 0.0;

        for (String name: names) {
            val += this.values.get(name) * this.items.get(name);
        }

        if (val == 0.0) return null;
        return val;
    }

    /**
     * Empties the ShoppingBasket, removing all items.
     */
    public void clear() {
        for (String name: names) {
            this.items.put(name, 0);
        }
    }
}
