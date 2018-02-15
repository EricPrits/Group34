package a498.capstone;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Eric on 2018-02-10.
 */

public class AdditionalFoodsAdapter extends BaseAdapter{
    private LayoutInflater mInflater;
    public ArrayList<String> list;
    Context thisContext;
    TextView itemView;

    public AdditionalFoodsAdapter(Context context, ArrayList<String> receipt) {
        list=receipt;
        thisContext=context;
    }

    public int getCount() {
        return list.size();
    }

    public String getItem(int position) {
        String result= list.get(position);
        return result;
    }
    public ArrayList<String> getList(int position){
        return list;
    }

    public void deleteItem(String name) {
        for(int i=0; i<list.size();i++)
        {
            if(list.get(i)==name) {
                list.remove(i);
                break;
            }
        }

    }
    public void addBlank(){
        String temp= "";
        list.add(0,temp);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent){

        final ViewHolder holder;
        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(thisContext).inflate(R.layout.additionalfood_list_layout, parent, false);
            holder.itemText = (TextView) convertView.findViewById(R.id.item_entry);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        itemView = (TextView) convertView.findViewById(R.id.item_entry);

        holder.ref = position;

        itemView.setText(list.get(position));

        holder.itemText.setText(list.get(position));
        if(position %2 == 1){
            holder.itemText.setBackgroundColor(Color.TRANSPARENT);
        }
        else{
            holder.itemText.setBackgroundColor(Color.parseColor("#AFAFAFAF"));
        }
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
                String result=arg0.toString();
                list.set(holder.ref,result);
            }
        });

        return convertView;
    }
    private class ViewHolder {
        TextView itemText;
        int ref;
    }
}


