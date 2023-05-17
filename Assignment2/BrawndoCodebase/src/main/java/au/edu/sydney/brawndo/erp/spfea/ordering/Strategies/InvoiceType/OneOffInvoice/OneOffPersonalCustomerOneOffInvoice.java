package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceData;
import au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.OneOffInvoice.OneOffInvoiceStrategy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class OneOffPersonalCustomerOneOffInvoice implements OneOffInvoiceStrategy {

    @Override
    public String generateInvoiceData(OneOffInvoiceData oneOffInvoiceData) {
        StringBuilder sb = new StringBuilder();

        sb.append("Thank you for your Brawndo© order!\n");
        sb.append("Your order comes to: $");
        sb.append(String.format("%,.2f", oneOffInvoiceData.totalCost()));
        sb.append("\nPlease see below for details:\n");
        List<Product> keyList = new ArrayList<>(oneOffInvoiceData.products().keySet());
        keyList.sort(Comparator.comparing(Product::getProductName).thenComparing(Product::getCost));

        for (Product product: keyList) {
            sb.append("\tProduct name: ");
            sb.append(product.getProductName());
            sb.append("\tQty: ");
            sb.append(oneOffInvoiceData.products().get(product));
            sb.append("\tCost per unit: ");
            sb.append(String.format("$%,.2f", product.getCost()));
            sb.append("\tSubtotal: ");
            sb.append(String.format("$%,.2f\n", product.getCost() * oneOffInvoiceData.products().get(product)));
        }

        return sb.toString();
    }
}
