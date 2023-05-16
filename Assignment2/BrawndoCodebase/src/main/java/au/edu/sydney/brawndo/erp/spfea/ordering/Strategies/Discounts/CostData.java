package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.HashMap;
import java.util.Map;

public class CostData {
    private Map<Product, Integer> products;
    private double discountRate;
    private int discountThreshold;

    public CostData(Map<Product, Integer> products, double discountRate, int discountThreshold) {
        this.products = products;
        this.discountRate = discountRate;
        this.discountThreshold = discountThreshold;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public double getDiscountRate() {
        return discountRate;
    }

    public int getDiscountThreshold() {
        return discountThreshold;
    }
}
