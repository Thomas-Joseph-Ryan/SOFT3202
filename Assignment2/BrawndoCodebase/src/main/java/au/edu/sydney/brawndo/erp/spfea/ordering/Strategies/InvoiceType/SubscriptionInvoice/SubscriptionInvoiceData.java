package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Map;

public record SubscriptionInvoiceData(double totalCost, double recurringCost, Map<Product, Integer> products) {
}
