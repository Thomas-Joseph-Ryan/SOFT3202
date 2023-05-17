package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

public class BulkDiscount implements PricingStrategy {

    Integer discountThreshold;
    Double discountRate;

    public BulkDiscount(Integer discountThreshold, Double discountRate) {
        this.discountThreshold = discountThreshold;
        this.discountRate = discountRate;
    }

    @Override
    public double getTotalCost(CostData costData) {
        double cost = 0.0;
        for (Product product: costData.products().keySet()) {
            int count = costData.products().get(product);
            if (count >= this.discountThreshold) {
                cost +=  count * product.getCost() * discountRate;
            } else {
                cost +=  count * product.getCost();
            }
        }
        return cost;
    }
}
