package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.HashMap;
import java.util.Map;

public class InvoiceData {
    private final double totalCost;
    private final Map<Product, Integer> products;

    private InvoiceData(Builder builder) {
        this.totalCost = builder.totalCost;
        this.products = builder.products;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public static class Builder {
        private double totalCost = 0.0;
        private Map<Product, Integer> products = new HashMap<>();

        public Builder totalCost(double totalCost) {
            this.totalCost = totalCost;
            return this;
        }

        public Builder products(Map<Product, Integer> products) {
            this.products = products;
            return this;
        }

        public InvoiceData build() {
            return new InvoiceData(this);
        }
    }
}
