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

    ArrayList<SummaryData> mainList;
    HashMap<Integer, ArrayList<DetailedData>> detailedList;
    Receipt_dbAdapter receipt_db;
    SummaryAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tab, container, false);

        ListView listView = rootView.findViewById(R.id.mainListView);
        myAdapter = new SummaryAdapter(getContext(), mainList);
        listView.setAdapter(myAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        receipt_db = new Receipt_dbAdapter(getContext());
        loadList();
    }


    public void loadList(){
        Cursor summaryData = receipt_db.getSummaryData();
        //Populate ArrayList with summary data from database
        mainList = new ArrayList<SummaryData>();
        detailedList = new HashMap<Integer, ArrayList<DetailedData>>();
        while(summaryData.moveToNext()){
            int id = summaryData.getInt(summaryData.getColumnIndex("_id"));
            String name = summaryData.getString(summaryData.getColumnIndex("Name"));
            String date = summaryData.getString(summaryData.getColumnIndex("Date"));
            if(date.length() >= 10)
                date = date.substring(0, 10);  //Keep only date part of timestamp (cut off time part)
            SummaryData data = new SummaryData(id, name, date);
            mainList.add(data);
        }
    }
}
