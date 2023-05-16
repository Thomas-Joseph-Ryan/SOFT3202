package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PersonalCustomerInvoice implements InvoiceStrategy {

    @Override
    public String generateInvoiceData(InvoiceData invoiceData) {
        StringBuilder sb = new StringBuilder();

        sb.append("Thank you for your Brawndo© order!\n");
        sb.append("Your order comes to: $");
        sb.append(String.format("%,.2f", invoiceData.getTotalCost()));
        sb.append("\nPlease see below for details:\n");
        List<Product> keyList = new ArrayList<>(invoiceData.getProducts().keySet());
        keyList.sort(Comparator.comparing(Product::getProductName).thenComparing(Product::getCost));

        for (Product product: keyList) {
            sb.append("\tProduct name: ");
            sb.append(product.getProductName());
            sb.append("\tQty: ");
            sb.append(invoiceData.getProducts().get(product));
            sb.append("\tCost per unit: ");
            sb.append(String.format("$%,.2f", product.getCost()));
            sb.append("\tSubtotal: ");
            sb.append(String.format("$%,.2f\n", product.getCost() * invoiceData.getProducts().get(product)));
        }

        return sb.toString();
    }
}
