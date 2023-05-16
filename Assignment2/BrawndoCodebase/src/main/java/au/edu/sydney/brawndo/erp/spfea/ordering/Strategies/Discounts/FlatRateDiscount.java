package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

public class FlatRateDiscount implements DiscountingStrategy {
    @Override
    public double getTotalCost(CostData costData) {
        double cost = 0.0;
        for (Product product: costData.getProducts().keySet()) {
            cost +=  costData.getProducts().get(product) * product.getCost() * costData.getDiscountRate();
        }
        return cost;
    }
}
