package a498.capstone;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;

/**
 * Created by Eric on 2018-02-10.
 */

public class ParsedAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    public ArrayList<String[]> list;
    Context thisContext;
    EditText itemView;
    EditText quantityView;
    int flag;

    public ParsedAdapter(Context context, ArrayList<String[]> receipt) {
        list=receipt;
        thisContext=context;
    }

    public int getCount() {
        return list.size();
    }

    public String[] getItem(int position) {
        String[] result = new String[2];
        result[0]=itemView.getText().toString();
        result[1]=quantityView.getText().toString();
        return result;
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        if (convertView == null) {
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.parse_list_layout, parent, false);
        }

        itemView = convertView.findViewById(R.id.item_entry);
        quantityView = convertView.findViewById(R.id.quantity_entry);

        itemView.setText(list.get(position)[0]);
        quantityView.setText(list.get(position)[1]);




       TextWatcher textWatcherItem = new TextWatcher() {

           public void afterTextChanged(Editable s) {
               String[] result = new String[2];
               result[0]=s.toString();
               result[1]=list.get(position)[1];
               list.set(position,result);
           }

           public void beforeTextChanged(CharSequence s, int start, int count, int after) {
           }

           public void onTextChanged(CharSequence s, int start, int before,int count) {

           }
       };
        TextWatcher textWatcherQuantity = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                String[] result = new String[2];
                result[0]=list.get(position)[0];
                result[1]=s.toString();
                list.set(position,result);
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,int count) {

            }
        };
       itemView.addTextChangedListener(textWatcherItem);
       quantityView.addTextChangedListener(textWatcherQuantity);
        return convertView;
    }
}


