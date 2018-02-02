package a498.capstone;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class ReceiptTab extends Fragment {
    ArrayList<SummaryData> sumList;
    HashMap<Integer, ArrayList<DetailedData>> detailedList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.receipts_tab, container, false);

        ListView listView = rootView.findViewById(R.id.receiptListView);
        SummaryAdapter myAdapter = new SummaryAdapter(getContext(), sumList);
        listView.setAdapter(myAdapter);

        // When item is list is clicked, start new Activity displaying details
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SummaryData rowData = (SummaryData) parent.getItemAtPosition(position);
                ArrayList<DetailedData> detData = detailedList.get(id);
                Intent i = new Intent(view.getContext(), ReceiptDetailsList.class);
                i.putParcelableArrayListExtra("data", detailedList.get(rowData.getID()));
                startActivity(i);
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        if(getArguments() != null){
            sumList = getArguments().getParcelableArrayList("summary");
            detailedList = (HashMap<Integer, ArrayList<DetailedData>>) getArguments().getSerializable("detailed");

        }
    }

}
