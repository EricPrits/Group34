package a498.capstone;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
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
    ArrayList<String> additionalFoods;


    public RelatedFoods(Context context){
        InputStream is =  context.getResources().openRawResource(R.raw.food_tastes);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );
        foods = new ArrayList<>();
        map = new HashMap<>();
        String line = "";
        try {
            while((line = reader.readLine()) != null){
               //Split data
                String[] values = line.split(",");
                FoodTastes foodValues = new FoodTastes(values);
                foods.add(foodValues);
                map.put(foodValues.getFood(), foods.indexOf(foodValues));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is2 =  context.getResources().openRawResource(R.raw.addtional_foods);
        BufferedReader reader2 = new BufferedReader(
                new InputStreamReader(is2, Charset.forName("UTF-8"))
        );

        additionalFoods = new ArrayList<String>();
        String line2 = "";
        try {
            while((line2 = reader.readLine()) != null){
                //Split data
                String values = line2;
                additionalFoods.add(values);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public ArrayList<String> getFoods(){
        ArrayList<String> foodNames = new ArrayList<String>();
        for(int i=0; i<foods.size();i++){
            foodNames.add(foods.get(i).getFood());
        }
        for(int i=0; i<additionalFoods.size();i++){
            foodNames.add(additionalFoods.get(i));
        }
        return foodNames;
    }

    public void setAdditionalFoods(ArrayList<String> input, Context context){

    }

    public String getNewFood(String food) {
        FoodTastes currentFood = new FoodTastes();
        try {
            currentFood = foods.get(map.get(food));
            String match = getMatch(currentFood);
            return match;
        } catch (Exception e) {
            return food;
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


}
