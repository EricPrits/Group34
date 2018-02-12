package a498.capstone;

import android.graphics.Paint;
import android.support.v4.app.FragmentManager;
import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by patrickgibson on 2018-01-28.
 */

public class ReceiptDetailsList extends AppCompatActivity implements ReceiptDetailedEdit.ReceiptDetailedEditListener{

    ArrayList<DetailedData> data;
    Receipt_dbAdapter receipt_db;
    int id;
    DetailedAdapter detAdapter;


    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.detail_tab);
        Bundle bundle = getIntent().getExtras();
        id = bundle.getInt("id");
        receipt_db = new Receipt_dbAdapter(getApplicationContext());
        setData();
        ListView listView = findViewById(R.id.detaillist);
        listView.setItemsCanFocus(true);
        setData();
        detAdapter = new DetailedAdapter(this, data);
        listView.setAdapter(detAdapter);
        TextView Food = findViewById(R.id.textView4);
        Food.setPaintFlags(Food.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        TextView Quant = findViewById(R.id.textView3);
        Quant.setPaintFlags(Food.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long stuff){
                DetailedData data = (DetailedData) parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("foodType", data.getFoodType());
                bundle.putInt("quantity", data.getQuantity());
                bundle.putInt("id", data.getId());
                //bundle.putInt("table", id);
                ReceiptDetailedEdit dialog = new ReceiptDetailedEdit();
                dialog.setArguments(bundle);
                dialog.setmListener(ReceiptDetailsList.this);
                String tag = "ReceiptDetailEdit";
                dialog.show(getSupportFragmentManager(), tag);
                return true;
            }
        });
    }

    public void onPosClick(DialogFragment dialog) {
        Bundle bundle = dialog.getArguments();
        int id = bundle.getInt("id");
        //int table = bundle.getInt("table");
        String foodType = bundle.getString("foodType");
        int quantity = bundle.getInt("quantity");
        receipt_db.editDetailReceipt("receipt"+this.id, id, foodType, quantity);
        setData();
        detAdapter.refreshData(data);
    }

    public void onNegClick(DialogFragment dialog) {
        Bundle bundle = dialog.getArguments();
        int id = bundle.getInt("id");
        receipt_db.deleteFood(id, this.id);
        setData();
        detAdapter.refreshData(data);
    }

    public void setData(){
        Cursor dataCursor = receipt_db.getDetailedData(id);
        data = new ArrayList<>();
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
