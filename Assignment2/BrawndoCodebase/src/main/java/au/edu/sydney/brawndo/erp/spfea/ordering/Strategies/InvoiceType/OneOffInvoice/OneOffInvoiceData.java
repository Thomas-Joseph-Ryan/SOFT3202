package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Map;

/**
 * Data structure for storing the necessary data for one-off order invoices
 * @param totalCost Total cost of the order
 * @param products The products in the order
 */
public record OneOffInvoiceData(double totalCost, Map<Product, Integer> products) {

}
