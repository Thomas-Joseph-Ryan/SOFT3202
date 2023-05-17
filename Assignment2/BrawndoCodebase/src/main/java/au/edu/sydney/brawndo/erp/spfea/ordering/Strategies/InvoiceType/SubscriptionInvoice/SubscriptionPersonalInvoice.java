package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.InvoiceType.SubscriptionInvoice;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class SubscriptionPersonalInvoice implements SubscriptionInvoiceStrategy {

    @Override
    public String generateInvoiceData(SubscriptionInvoiceData subscriptionInvoiceData) {
        Map<Product, Integer> products = subscriptionInvoiceData.products();

        StringBuilder sb = new StringBuilder();

        sb.append("Thank you for your Brawndo© order!\n");
        sb.append("Your order comes to: $");
        sb.append(String.format("%,.2f", subscriptionInvoiceData.recurringCost()));
        sb.append(" each week, with a total overall cost of: $");
        sb.append(String.format("%,.2f", subscriptionInvoiceData.totalCost()));
        sb.append("\nPlease see below for details:\n");
        List<Product> keyList = new ArrayList<>(products.keySet());
        keyList.sort(Comparator.comparing(Product::getProductName).thenComparing(Product::getCost));

        for (Product product: keyList) {
            sb.append("\tProduct name: ");
            sb.append(product.getProductName());
            sb.append("\tQty: ");
            sb.append(products.get(product));
            sb.append("\tCost per unit: ");
            sb.append(String.format("$%,.2f", product.getCost()));
            sb.append("\tSubtotal: ");
            sb.append(String.format("$%,.2f\n", product.getCost() * products.get(product)));
        }

        return sb.toString();
    }
}
