package a498.capstone;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
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

    public ParsedAdapter(Context context, ArrayList<String[]> receipt) {
        list=receipt;
        thisContext=context;
    }

    public int getCount() {
        return list.size();
    }

    public String[] getItem(int position) {
        String[] result = new String[2];
        result[0]= list.get(position)[0];
        result[1]=list.get(position)[1];
        return result;
    }
    public ArrayList<String[]> getList(int position){
        return list;
    }
    public void deleteItem(String name) {
        for(int i=0; i<list.size();i++)
        {
            if(list.get(i)[0]==name) {
                list.remove(i);
                break;
            }
        }

    }
    public void addBlank(){
        String[] temp = new String[2];
        temp[0]= "";
        temp[1]="1";
        list.add(0,temp);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.parse_list_layout, parent, false);
            holder.itemText = (EditText) convertView.findViewById(R.id.item_entry);
            holder.quantityText = (EditText) convertView.findViewById(R.id.quantity_entry);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        itemView = (EditText) convertView.findViewById(R.id.item_entry);
        quantityView = (EditText) convertView.findViewById(R.id.quantity_entry);

        holder.ref = position;

        itemView.setText(list.get(position)[0]);
        quantityView.setText(list.get(position)[1]);
        if(position %2 == 1){
            holder.itemText.setBackgroundColor(Color.TRANSPARENT);
            holder.quantityText.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            holder.itemText.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
            holder.quantityText.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
        }

        holder.itemText.setText(list.get(position)[0]);
        holder.quantityText.setText(list.get(position)[1]);
        holder.itemText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String[] result = new String[2];
                result[0]=arg0.toString();
                result[1]=list.get(holder.ref)[1];
                list.set(holder.ref,result);
            }
        });

        return convertView;
    }
    private class ViewHolder {
        EditText itemText;
        EditText quantityText;
        int ref;
    }
}


