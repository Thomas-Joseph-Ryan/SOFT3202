package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

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
