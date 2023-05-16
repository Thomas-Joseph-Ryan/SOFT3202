package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Map;

public class InvoiceData {
    private double totalCost;
    private Map<Product, Integer> products;

    public InvoiceData(double totalCost, Map<Product, Integer> products) {
        this.totalCost = totalCost;
        this.products = products;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }
}
