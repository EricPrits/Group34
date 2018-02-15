package a498.capstone;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Custom adapter to be used to populate the listView for the summary table in the ReceiptTab.
 *
 * Created by patrickgibson on 2018-01-28.
 */

public class SummaryAdapter extends ArrayAdapter<SummaryData> {
    ArrayList<SummaryData> list;
    public SummaryAdapter(Context context, ArrayList<SummaryData> users) {
        super(context, 0, users);
        list = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SummaryData data = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.name_entry);
        TextView dateView = convertView.findViewById(R.id.date_entry);

        nameView.setText(data.getName());
        dateView.setText(data.getDate());
        if(position %2 == 1){
            nameView.setBackgroundColor(Color.TRANSPARENT);
            dateView.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            nameView.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
            dateView.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
        }

        return convertView;
    }

    /**
     * Method is called when name or date has been changed
     * @param data
     */
    public void refreshData(ArrayList<SummaryData> data){
        this.list.clear();
        this.list.addAll(data);
        notifyDataSetChanged();
    }

}


