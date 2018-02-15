package a498.capstone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * This class is a Custom Adapter used to display detailed receipt data in a list view
 *
 * Created by patrickgibson on 2018-02-01.
 */

public class DetailedAdapter extends ArrayAdapter<DetailedData> {
    ArrayList<DetailedData> list;


    public DetailedAdapter(Context context, ArrayList<DetailedData> users) {
        super(context, 0, users);
        list = users;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        DetailedData data = list.get(position);
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.detailed_textviews, parent, false);



        TextView tv1 = convertView.findViewById(R.id.textView1);
        TextView tv2 = convertView.findViewById(R.id.textView2);
        tv1.setText(data.getFoodType());
        tv2.setText(Integer.toString(data.getQuantity()));
        if(position %2 == 1){
            tv1.setBackgroundColor(Color.TRANSPARENT);
            tv2.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            tv1.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
            tv2.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
        }


        return convertView;
    }


    public void refreshData(ArrayList<DetailedData> data){
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }
}
