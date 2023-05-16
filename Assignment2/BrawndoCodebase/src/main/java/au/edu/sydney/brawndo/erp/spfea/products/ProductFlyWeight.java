package au.edu.sydney.brawndo.erp.spfea.products;

public class ProductFlyWeight {

    private double[] recipeData;
    private double[] marketingData;
    private double[] safetyData;
    private double[] licensingData;

    public ProductFlyWeight(double[] recipeData, double[] marketingData, double[] safetyData, double[] licensingData) {
        this.licensingData = licensingData;
        this.recipeData = recipeData;
        this.marketingData = marketingData;
        this.safetyData = safetyData;
    }

    public double[] getLicensingData() {
        return licensingData;
    }

    public double[] getMarketingData() {
        return marketingData;
    }

    public double[] getRecipeData() {
        return recipeData;
    }

    public double[] getSafetyData() {
        return safetyData;
    }
}

