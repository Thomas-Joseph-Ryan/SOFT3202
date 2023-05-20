package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Map;


/**
 * Data structure for subscription invoice data
 * @param totalCost Total cost of the entire subscription
 * @param recurringCost Recurring cost each subscription payment
 * @param products The products in the subscription order
 */
public record SubscriptionInvoiceData(double totalCost, double recurringCost, Map<Product, Integer> products) {
}
