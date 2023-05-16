package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

public class BulkDiscount implements DiscountingStrategy {
    @Override
    public double getTotalCost(CostData costData) {
        double cost = 0.0;
        for (Product product: costData.getProducts().keySet()) {
            int count = costData.getProducts().get(product);
            if (count >= costData.getDiscountThreshold()) {
                cost +=  count * product.getCost() * costData.getDiscountRate();
            } else {
                cost +=  count * product.getCost();
            }
        }
        return cost;
    }
}
