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
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by patrickgibson on 2018-01-28.
 */

public class SummaryAdapter extends ArrayAdapter<SummaryData> {
    public SummaryAdapter(Context context, ArrayList<SummaryData> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        SummaryData data = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout, parent, false);
        }

        TextView nameView = convertView.findViewById(R.id.name_entry);
        TextView dateView = convertView.findViewById(R.id.date_entry);
        TextView idView = convertView.findViewById(R.id.things);

        nameView.setText(data.getName());
        dateView.setText(data.getDate());
        idView.setText(Integer.toString(data.getID()));

        return convertView;
    }

}
