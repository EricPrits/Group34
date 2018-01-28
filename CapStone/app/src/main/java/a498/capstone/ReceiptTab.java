package a498.capstone;

import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class ReceiptTab extends Fragment {
    ArrayList<SummaryData> sumList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.receipts_tab, container, false);

        ListView listView = rootView.findViewById(R.id.receiptListView);
        //ArrayAdapter<SummaryData> myAdapter = new ArrayAdapter<SummaryData>(getContext(), android.R.layout.simple_list_item_1, sumList);

        SummaryAdapter myAdapter = new SummaryAdapter(getContext(), sumList);

        listView.setAdapter(myAdapter);
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        if(getArguments() != null){
            sumList = getArguments().getParcelableArrayList("summary");
        }
    }

}
