package a498.capstone;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to determine recommendations for similar foods. When getNewFood is called,
 * and provided the name of a food, it will return the name of a food with similar properties. If
 * the food provided is not in the database, it will return the food provided.
 *
 * Created by patrickgibson on 2018-02-10.
 */

public class RelatedFoods {
    public ArrayList<FoodTastes> foods;
    public HashMap<String,Integer> map;
    ArrayList<String> allFoods;


    public RelatedFoods(Context context){
        InputStream is =  context.getResources().openRawResource(R.raw.food_tastes);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        foods = new ArrayList<>();
        map = new HashMap<>();
        allFoods = new ArrayList<>();
        String line = "";
        try {
            while((line = reader.readLine()) != null){
               //Split data
                String[] values = line.split(",");
                FoodTastes foodValues = new FoodTastes(values);
                foods.add(foodValues);
                map.put(foodValues.getFood(), foods.indexOf(foodValues));
                allFoods.add(foodValues.getFood());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public DetailedData getNewFood(String food) {
        FoodTastes currentFood;
        try {
            currentFood = foods.get(map.get(food.toLowerCase()));
            String match = getMatch(currentFood);
            return new DetailedData(match);
        } catch (Exception e) {
            return new DetailedData(food);
        }
    }

    private String getMatch(FoodTastes currentFood) {
        ArrayList<FoodTastes> matches = new ArrayList<>();
        for(FoodTastes x : foods){
            if(x.getCategory() == currentFood.getCategory() && x.getFood() != currentFood.getFood())
                matches.add(x);
        }
        String match = currentFood.getFood();
        double minDistance = 100;
        for(FoodTastes x : matches){
            double currDistance;
            currDistance = Math.sqrt( Math.pow(currentFood.getBitterness() - x.getBitterness(), 2) +
                    Math.pow(currentFood.getFat() - x.getFat(), 2) +
                    Math.pow(currentFood.getSaltiness() - x.getSaltiness(), 2) +
                    Math.pow(currentFood.getSourness() - x.getSourness(), 2) +
                    Math.pow(currentFood.getUmami() - x.getUmami(), 2) +
                    Math.pow(currentFood.getSweetness() - x.getSweetness(), 2)
            );
            if (currDistance <  minDistance){
                match = x.getFood();
                minDistance = currDistance;
            }
        }
        return match;
    }

    public ArrayList<String> getAllFoods(){
        return allFoods;
    }

}
