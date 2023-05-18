package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

public interface PricingStrategy {

    /**
     * Method to get the total cost of an order based
     * @param costData Cost data object sent by Order class
     * @return double representing the total value of the order.
     */
    double getTotalCost(CostData costData);
}
