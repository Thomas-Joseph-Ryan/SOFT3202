package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

public class BulkDiscount implements PricingStrategy {

    Integer discountThreshold;
    Double discountRate;

    /**
     * Necessary information for calculating discount provided through Facade
     * createOrder() method.
     * @param discountThreshold Item count needed for discount to apply
     * @param discountRate Rate of the discount
     */
    public BulkDiscount(Integer discountThreshold, Double discountRate) {
        this.discountThreshold = discountThreshold;
        this.discountRate = discountRate;
    }

    /**
     * Calculate total cost for BulkDiscount
     * @param costData Cost data object sent by Order class
     * @return Value of cart with bulk discount applied
     */
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
