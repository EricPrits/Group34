package a498.capstone;

import android.content.Context;
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


        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.detailed_textviews, parent, false);


        ViewHolder holder = new ViewHolder();
        DetailedData data = list.get(position);
        holder.tv1 = convertView.findViewById(R.id.textView1);
        holder.tv2 = convertView.findViewById(R.id.textView2);
        holder.tv1.setText(data.getFoodType());
        holder.tv2.setText(Integer.toString(data.getQuantity()));
        return convertView;
    }

    static class ViewHolder{
        TextView tv1, tv2;
    }
}
