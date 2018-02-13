package a498.capstone;

import android.database.Cursor;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

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

    mainList = keepDuplicates(mainList);
    }

    /////////////////////////////////////////////////////////////////////////////////////
    //Algorithm Functions
    /////////////////////////////////////////////////////////////////////////////////////
       public ArrayList<DetailedData> keepDuplicates(ArrayList<DetailedData> list)
       {
           duplicatesList =  new ArrayList<DetailedData>();

            //For every food item in the original list
           for (int i = 0; i < mainList.size(); i++)
            {
               int counter = 0;
               //Iterate through the same list
               for (int j = 0; j < mainList.size(); j++)
               {

                    //check if theres a repeated food
                    if (mainList.get(i).getFoodType() ==  mainList.get(j).getFoodType()) {
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

 }

