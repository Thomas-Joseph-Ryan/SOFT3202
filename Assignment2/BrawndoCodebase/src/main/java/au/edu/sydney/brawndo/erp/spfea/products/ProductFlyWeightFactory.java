package au.edu.sydney.brawndo.erp.spfea.products;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for checking if a product flyweight exists, and returns appropriate values
 */
public class ProductFlyWeightFactory {

    private Map<String, ProductFlyWeight> productFlyWeights = new HashMap<>();

    private static ProductFlyWeightFactory instance;


    private ProductFlyWeightFactory() {

    }

    /**
     * Singleton for the flyweight factory, so all products can access the same
     * flyweight objects in memory without needing to explicitly pass this factory
     * object to them
     * @return The ProductFlyWeightFactory object
     */
    public static synchronized ProductFlyWeightFactory getInstance() {
        if (instance == null) {
            instance = new ProductFlyWeightFactory();
        }
        return instance;
    }

    /**
     * Checks if an object exists with this specific data, if so then returns that object
     * otherwise creates a new object to store this data
     * @param recipeData Recipe data
     * @param marketingData Marketing data
     * @param safetyData Safety data
     * @param licensingData Licensing data
     * @return The flyWeight object containing this data
     */
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

    /**
     * Generates a key based on the data that will always be unique and consistent,
     * used for the key in the map of fly weights
     * @param recipeData
     * @param marketingData
     * @param safetyData
     * @param licensingData
     * @return Unique key
     */
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
