package a498.capstone;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class ReceiptTab extends Fragment implements ReceiptSummaryEdit.ReceiptSummaryEditListener{
    ArrayList<SummaryData> sumList;
    HashMap<Integer, ArrayList<DetailedData>> detailedList;
    Receipt_dbAdapter receipt_db;
    SummaryAdapter myAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.receipts_tab, container, false);

        ListView listView = rootView.findViewById(R.id.receiptListView);
        myAdapter = new SummaryAdapter(getContext(), sumList);
        listView.setAdapter(myAdapter);

        // When item is list is clicked, start new Activity displaying details
        listView.setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SummaryData rowData = (SummaryData) parent.getItemAtPosition(position);
                Intent i = new Intent(view.getContext(), ReceiptDetailsList.class);
                i.putExtra("id", rowData.getID());
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                SummaryData rowData = (SummaryData) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("id", rowData.getID());
                bundle.putString("Name", rowData.getName());
                bundle.putString("Date", rowData.getDate());

                DialogFragment dialog = new ReceiptSummaryEdit();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(ReceiptTab.this, 300);
                String tag = "ReceiptSummaryEdit";
                dialog.show(getFragmentManager(), tag);
                return true;
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        receipt_db = new Receipt_dbAdapter(getContext());
        setData();
    }

    /**
     * This method is called when Update button is pressed when editing name or date of receipt
     * @param dialog
     */
    public void onDialogPositiveClick(DialogFragment dialog) {
        Bundle bundle = dialog.getArguments();
        int id = bundle.getInt("id");
        String name = bundle.getString("Name");
        String date = bundle.getString("Date");
        receipt_db.editSummaryReceipt(id, name, date);
        setData();
        myAdapter.refreshData(sumList);

    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        int id = dialog.getArguments().getInt("id");
        receipt_db.deleteReceipt(id);
        setData();
        myAdapter.refreshData(sumList);
    }

    /**
     * Populates lists with data from database
     */
    public void setData(){
        Cursor summaryData = receipt_db.getSummaryData();
        //Populate ArrayList with summary data from database
        sumList = new ArrayList<SummaryData>();
        detailedList = new HashMap<Integer, ArrayList<DetailedData>>();
        while(summaryData.moveToNext()){
            int id = summaryData.getInt(summaryData.getColumnIndex("_id"));
            String name = summaryData.getString(summaryData.getColumnIndex("Name"));
            String date = summaryData.getString(summaryData.getColumnIndex("Date"));
            if(date.length() >= 10)
                date = date.substring(0, 10);  //Keep only date part of timestamp (cut off time part)
            SummaryData data = new SummaryData(id, name, date);
            sumList.add(data);
        }
    }

}
