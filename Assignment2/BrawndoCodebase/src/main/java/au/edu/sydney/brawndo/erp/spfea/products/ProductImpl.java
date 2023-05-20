package au.edu.sydney.brawndo.erp.spfea.products;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Arrays;
import java.util.Objects;

/**
 * Implementation of the product
 */
public class ProductImpl implements Product {

    private final String name;
    private final double[] manufacturingData;
    private final double cost;
    private ProductFlyWeight productFlyWeight;
    private String hashValue;

    /**
     * Constructor for product object. name, cost and manufacturing data are extrinsic and recipe data, marketing data,
     * safety date and licensing data are intrinsic.
     *
     * This constructor tries to use the Flyweight Factory to see if there is an existing object with the same intrinsic
     * data to share the memory of to reduce RAM usage
     * @param name name of product
     * @param cost cost of product
     * @param manufacturingData Manufacturing data
     * @param recipeData Recipe data
     * @param marketingData Marketing data
     * @param safetyData Safety Data
     * @param licensingData Licensing Data
     */
    public ProductImpl(String name,
                       double cost,
                       double[] manufacturingData,
                       double[] recipeData,
                       double[] marketingData,
                       double[] safetyData,
                       double[] licensingData) {
        this.name = name;
        this.cost = cost;
        this.manufacturingData = manufacturingData;
        this.productFlyWeight = ProductFlyWeightFactory.getInstance().getProductFlyWeight(recipeData, marketingData, safetyData, licensingData);
        this.hashValue = generateHashValue(recipeData, marketingData, safetyData, licensingData, manufacturingData, name, cost);
    }

    @Override
    public String getProductName() {
        return name;
    }

    @Override
    public double getCost() {
        return cost;
    }

    @Override
    public double[] getManufacturingData() {
        return manufacturingData;
    }

    @Override
    public double[] getRecipeData() {
        return this.productFlyWeight.getRecipeData();
    }

    @Override
    public double[] getMarketingData() {
        return this.productFlyWeight.getMarketingData();
    }

    @Override
    public double[] getSafetyData() {
        return this.productFlyWeight.getSafetyData();
    }

    @Override
    public double[] getLicensingData() {
        return this.productFlyWeight.getLicensingData();
    }

    @Override
    public String toString() {

        return String.format("%s", name);
    }

    /**
     * Method to check whether an object is equal to this product
     * @param obj Object to check
     * @return True if the same, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ProductImpl other = (ProductImpl) obj;
        // compare the hash values
        if (!Objects.equals(this.hashValue, other.hashValue)) {
            return false;
        }

        // If hashes are equal, perform a full comparison to be sure because hashes do have a small chance of collision.
        return this.getCost() == other.getCost() &&
                this.getProductName().equals(other.getProductName()) &&
                Arrays.equals(this.getManufacturingData(), other.getManufacturingData()) &&
                Arrays.equals(this.getRecipeData(), other.getRecipeData()) &&
                Arrays.equals(this.getMarketingData(), other.getMarketingData()) &&
                Arrays.equals(this.getSafetyData(), other.getSafetyData()) &&
                Arrays.equals(this.getLicensingData(), other.getLicensingData());
    }

    /**
     * Modified hashCode() method as equals() method got changed.
     * @return The modified hashValue
     */
    @Override
    public int hashCode() {
        return Objects.hash(hashValue);
    }

    /**
     * Generates the hashValue for this product
     * @param recipeData
     * @param marketingData
     * @param safetyData
     * @param licensingData
     * @param manufacturingData
     * @param name
     * @param cost
     * @return A hash string making comparison easy.
     */
    private String generateHashValue(double[] recipeData,
                                     double[] marketingData,
                                     double[] safetyData,
                                     double[] licensingData,
                                     double[] manufacturingData,
                                     String name,
                                     Double cost) {
        int recipeHash = Arrays.hashCode(recipeData);
        int marketingHash = Arrays.hashCode(marketingData);
        int safetyHash = Arrays.hashCode(safetyData);
        int licensingHash = Arrays.hashCode(licensingData);
        int manufacturingHash = Arrays.hashCode(manufacturingData);
        int nameHash = Objects.hash(name);
        int costHash = Objects.hash(cost);

        return recipeHash + "_" + marketingHash + "_" + safetyHash + "_" + licensingHash + "_"
                + manufacturingHash + "_" + nameHash + "_" + costHash;
    }

}
