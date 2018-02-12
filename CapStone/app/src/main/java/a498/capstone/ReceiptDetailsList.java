package a498.capstone;

import android.support.v4.app.FragmentManager;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;

/**
 * Created by patrickgibson on 2018-01-28.
 */

public class ReceiptDetailsList extends ListActivity implements ReceiptDetailedEdit.ReceiptDetailedEditListener{

    ArrayList<DetailedData> data;
    Receipt_dbAdapter receipt_db;
    int id;


    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        receipt_db = new Receipt_dbAdapter(getApplicationContext());
        data = new ArrayList<>();
        ListView listView = getListView();
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        setData();
        DetailedAdapter detAdapter = new DetailedAdapter(this, data);
        listView.setAdapter(detAdapter);

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                DetailedData data = (DetailedData) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("foodType", data.getFoodType());
                bundle.putInt("quantity", data.getQuantity());
                bundle.putInt("id", data.getId());
                DialogFragment dialog = new ReceiptDetailedEdit();
                dialog.setArguments(bundle);
                String tag = "ReceiptDetailEdit";
                //dialog.show(getFragmentManager(), tag);
                return true;
            }
        });
    }

    public void onPosClick(DialogFragment dialog) {
        Bundle bundle = dialog.getArguments();
        int id = bundle.getInt("id");
        String foodType = bundle.getString("foodType");
        int quantity = bundle.getInt("quantity");
        receipt_db.editDetailReceipt("receipt"+id, id, foodType, quantity);
    }

    public void onNegClick(DialogFragment dialog) {

    }

    public void setData(){
        Cursor dataCursor = receipt_db.getDetailedData(id);
        while(dataCursor.moveToNext()){
            String foodType = dataCursor.getString(dataCursor.getColumnIndex("FoodType"));
            int quantity = dataCursor.getShort(dataCursor.getColumnIndex("Quantity"));
            int detID = dataCursor.getInt(dataCursor.getColumnIndex("_id"));
            String date = dataCursor.getString(dataCursor.getColumnIndex("ExpiryDate"));
            String purchaseDate = dataCursor.getString(dataCursor.getColumnIndex("PurchaseDate"));
            DetailedData detData = new DetailedData(foodType, quantity, detID, date, purchaseDate);
            data.add(detData);
        }
    }
}
