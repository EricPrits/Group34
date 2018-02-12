package a498.capstone;

/**
 * Used to house the characteristics of a food.
 * Created by patrickgibson on 2018-02-10.
 */

class FoodTastes {
    private String food;
    private int category;       //Food group this food belongs too eg. Fruits, Dairy.
    private double sweetness;
    private double saltiness;
    private double sourness;
    private double bitterness;
    private double umami;
    private double fat;
    private int expiryDate;

    public FoodTastes(){
    }

    public FoodTastes(String[] values) {
        food = values[0];
        category = Integer.parseInt(values[1]);
        sweetness = Double.parseDouble(values[2]);
        saltiness = Double.parseDouble(values[3]);
        sourness = Double.parseDouble(values[4]);
        bitterness = Double.parseDouble(values[5]);
        umami = Double.parseDouble(values[6]);
        fat = Double.parseDouble(values[7]);
        expiryDate = Integer.parseInt(values[8]);
    }

    public String getFood(){
        return food;
    }

    public int getCategory() {
        return category;
    }

    public double getSweetness() {
        return sweetness;
    }

    public double getSaltiness() {
        return saltiness;
    }

    public double getSourness() {
        return sourness;
    }

    public double getBitterness() {
        return bitterness;
    }

    public double getUmami() {
        return umami;
    }

    public double getFat() {
        return fat;
    }
    public int getExpiryDate(){
        return expiryDate;
    }
}
