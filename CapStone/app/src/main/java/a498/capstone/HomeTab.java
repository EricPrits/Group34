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
        myAdapter = new DetailedAdapter(getContext(), mainList);
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

    Receipt_dbAdapter receipt_db;
    DetailedAdapter myAdapter;

    public void loadList(){
        ArrayList<ArrayList<DetailedData>> allFoods = receipt_db.getAllReceipts();
        //For all the reciepts in the allFoods array list
        for(int i = 1; i <= allFoods.size(); i++){

            //For all the foods in each reciept
            for(int j = 1; i <=allFoods.get(i).size(); i++)
            {
                mainList.add(allFoods.get(i).get(j));
            }
    }


        //Everything from this point on can be used to populate the mainlist
        //SummaryData must be set to the new data set of foods
        //Cursor summaryData = receipt_db.getSummaryData();


        //detailedList = new HashMap<Integer, ArrayList<DetailedData>>();


//        //This will add everything in the reciept database
//        while(summaryData.moveToNext()){
//            int id = summaryData.getInt(summaryData.getColumnIndex("_id"));
//            String name = summaryData.getString(summaryData.getColumnIndex("Name"));
//            String date = summaryData.getString(summaryData.getColumnIndex("Date"));
//            if(date.length() >= 10) date = date.substring(0, 10);  //Keep only date part of timestamp (cut off time part)
//            SummaryData data = new SummaryData(id, name, date);
//            mainList.add(data);
//        }
    }
}
