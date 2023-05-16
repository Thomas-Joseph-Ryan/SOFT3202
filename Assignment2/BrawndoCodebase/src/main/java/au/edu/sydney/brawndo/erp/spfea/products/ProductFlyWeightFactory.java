package au.edu.sydney.brawndo.erp.spfea.products;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ProductFlyWeightFactory {

    private Map<String, ProductFlyWeight> productFlyWeights = new HashMap<>();

    private static ProductFlyWeightFactory instance;

    private ProductFlyWeightFactory() {

    }

    public static synchronized ProductFlyWeightFactory getInstance() {
        if (instance == null) {
            instance = new ProductFlyWeightFactory();
        }
        return instance;
    }

    public ProductFlyWeight getProductFlyWeight(double[] recipeData,
                                                double[] marketingData,
                                                double[] safetyData,
                                                double[] licensingData) {
        String key = generateKey(recipeData, marketingData, safetyData, licensingData);

        if (!productFlyWeights.containsKey(key)) {
            productFlyWeights.put(key, new ProductFlyWeight(recipeData, marketingData, safetyData, licensingData));
        }

        return productFlyWeights.get(key);
    }

    private String generateKey(double[] recipeData,
                               double[] marketingData,
                               double[] safetyData,
                               double[] licensingData) {

        int recipeHash = Arrays.hashCode(recipeData);
        int marketingHash = Arrays.hashCode(marketingData);
        int safetyHash = Arrays.hashCode(safetyData);
        int licensingHash = Arrays.hashCode(licensingData);

        return recipeHash + "_" + marketingHash + "_" + safetyHash + "_" + licensingHash;
    }
}
