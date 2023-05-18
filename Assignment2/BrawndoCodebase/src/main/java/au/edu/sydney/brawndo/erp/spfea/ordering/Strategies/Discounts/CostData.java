package au.edu.sydney.brawndo.erp.spfea.ordering.Strategies.Discounts;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.HashMap;
import java.util.Map;

/**
 * Record to providing a clean way to send the required data to the pricing methods.
 * @param products Products currently in the order basket
 */
public record CostData(Map<Product, Integer> products) {
}
