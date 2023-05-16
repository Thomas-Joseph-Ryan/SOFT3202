package au.edu.sydney.brawndo.erp.spfea.products;

import au.edu.sydney.brawndo.erp.ordering.Product;

import java.util.Arrays;
import java.util.Objects;

public class ProductImpl implements Product {

    private final String name;
    private final double[] manufacturingData;
    private final double cost;
    private ProductFlyWeight productFlyWeight;
    private String hashValue;

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

    @Override
    public int hashCode() {
        return Objects.hash(hashValue);
    }

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
