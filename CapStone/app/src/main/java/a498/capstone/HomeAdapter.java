package a498.capstone;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is a Custom Adapter used to display detailed receipt data in a list view
 *
 * Created by ericprits on 2018-02-01.
 */

public class HomeAdapter extends ArrayAdapter<DetailedData> {
    ArrayList<DetailedData> list;


    public HomeAdapter(Context context, ArrayList<DetailedData> users) {
        super(context, 0, users);
        list = users;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        DetailedData data = list.get(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.home_textviews, parent, false);


        TextView Name = convertView.findViewById(R.id.itemName);
        Name.setText(data.getFoodType());
        if(position %2 == 1){
            Name.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            Name.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
        }
        return convertView;
    }


    public void refreshData(ArrayList<DetailedData> data){
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }
}
