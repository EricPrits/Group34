package a498.capstone;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.Date;
import static java.lang.System.out;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class HomeTab extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tab, container, false);
        ListView listView = rootView.findViewById(R.id.mainListView);
        myAdapter = new HomeAdapter(getContext(), mainList);
        listView.setAdapter(myAdapter);
        return rootView;
    }
    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        receipt_db = new Receipt_dbAdapter(getContext());
        loadList();
    }

    //Creates an  array list of array list that include all of the food objects that has been scanned
    //And placed into the db
    ArrayList<ArrayList<DetailedData>> allFoods;
    ArrayList<DetailedData> mainList;

    ArrayList<DetailedData> duplicatesList;
    ArrayList<DetailedData> expiredList;
    ArrayList<DetailedData> alternativesList;



    //Creates an outline for date variables
    SimpleDateFormat curFormater = new SimpleDateFormat("yyyy-MM-ddd");

    //Creates a date variable that is the current date
    Date currentDate = Calendar.getInstance().getTime();
    Date expiryDate;
    String foodExpiry;

    Receipt_dbAdapter receipt_db;
    HomeAdapter myAdapter;

    public void loadList(){
        allFoods = receipt_db.getAllReceipts();
        mainList = new ArrayList<>();
        //For all the reciepts in the allFoods array list
        for(int i = 0; i < allFoods.size(); i++){

            //For all the foods in each reciept
            for(int j = 0; j < allFoods.get(i).size(); j++)
            {
                mainList.add(allFoods.get(i).get(j));
            }
        }

        duplicatesList = keepDuplicates(mainList);
        expiredList = keepExpired(mainList);
        alternativesList = keepAlternatives(mainList);
        mainList.clear();
        mainList.addAll(duplicatesList);
        mainList.addAll(alternativesList);
        mainList.addAll(expiredList);

    }

    /////////////////////////////////////////////////////////////////////////////////////
    //Algorithm Functions - Frequent Purchases
    /////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<DetailedData> keepDuplicates(ArrayList<DetailedData> list)
    {
        duplicatesList =  new ArrayList<DetailedData>();

        //For every food item in the original list
        for (int i = 0; i < mainList.size(); i++)
        {
            int counter = 0;
            //Iterate through the same list
            for (int j = i; j < mainList.size(); j++)
            {

                //check if theres a repeated food
                if (mainList.get(i).getFoodType().equals(mainList.get(j).getFoodType())) {
                    counter++;
                }

                //If a food is found twice on the list, add it to duplicate list
                if (counter >= 2){
                    duplicatesList.add(mainList.get(i));
                    break;
                }
            }
        }

        list = duplicatesList;
        return list;
    }

    public ArrayList<DetailedData>keepExpired(ArrayList<DetailedData> list)
    {
        expiredList =  new ArrayList<DetailedData>();

        for (int i = 0; i < mainList.size(); i++) {

            String foodExpiry = mainList.get(i).getExpiryDate();
            System.out.print(foodExpiry);




            //Format string from database into a date variable

            try{
                expiryDate = curFormater.parse(foodExpiry);
            }
            catch (java.text.ParseException e) {

            }


            //If the current food has expired, add it to the expiredList
          if (currentDate.compareTo(expiryDate) > 1)
          {
                expiredList.add(mainList.get(i));
           }
        }

        list = expiredList;
        return list;
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //Algorithm Functions - Returning Foods that are alternatives to users purchases
    /////////////////////////////////////////////////////////////////////////////////////
    public ArrayList<DetailedData> keepAlternatives(ArrayList<DetailedData> list)
    {

       RelatedFoods relatedFood = new RelatedFoods(getContext());




        alternativesList =  new ArrayList<DetailedData>();
        String foodCheck;

        //Iterate through mainList
        for (int i = 0; i < mainList.size(); i++) {


            //Set the name of the current food to a string
           foodCheck = mainList.get(i).getFoodType();

           //If there is no related food
           if (relatedFood.getNewFood(foodCheck).getFoodType().equals("NA")) {
               break;
            }

            else
            alternativesList.add(relatedFood.getNewFood(foodCheck));
        }

        list = alternativesList;
        return list;
    }


}


