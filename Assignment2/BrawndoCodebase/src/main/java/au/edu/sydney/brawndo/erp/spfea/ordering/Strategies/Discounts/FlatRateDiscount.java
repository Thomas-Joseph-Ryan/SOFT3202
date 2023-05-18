package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

/**
 * Pricing strategy for flat discounts, for detailed description of what
 * methods are doing, look at BulkDiscount class.
 */
public class FlatRateDiscount implements PricingStrategy {

    double discountRate;
    public FlatRateDiscount(double discountRate) {
        this.discountRate = discountRate;
    }
    @Override
    public double getTotalCost(CostData costData) {
        double cost = 0.0;
        for (Product product: costData.products().keySet()) {
            cost +=  costData.products().get(product) * product.getCost() * this.discountRate;
        }
        return cost;
    }
}
