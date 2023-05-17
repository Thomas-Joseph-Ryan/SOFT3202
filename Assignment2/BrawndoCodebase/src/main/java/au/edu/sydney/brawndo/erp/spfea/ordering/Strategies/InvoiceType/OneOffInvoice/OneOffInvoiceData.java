package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Map;

public record OneOffInvoiceData(double totalCost, Map<Product, Integer> products) {

}
