package a498.capstone;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class SettingsTab extends Fragment implements AdditionalFoodsDeleteDialog.AdditionalFoodsDeleteDialogListener {
    Receipt_dbAdapter receipt_db;
    AdditionalFoodsAdapter myAdapter;
    ArrayList<String> list;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_tab, container, false);
        list = new ArrayList<String>();
        receipt_db = new Receipt_dbAdapter(getContext());
        list =receipt_db.getAdditionalFoods();

        ListView listView = rootView.findViewById(R.id.additionalFoodsList);
        myAdapter = new AdditionalFoodsAdapter(getContext(), list);
        listView.setAdapter(myAdapter);

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                String row= (String)parent.getItemAtPosition(position);
                Bundle bundle = new Bundle();
                bundle.putString("Name", row);

                AdditionalFoodsDeleteDialog dialog = new AdditionalFoodsDeleteDialog();
                dialog.setArguments(bundle);
                dialog.setTargetFragment(SettingsTab.this, 300);
                String tag = "AddtionalFoodsDeleteDialog";
                dialog.show(getFragmentManager(), tag);
                return true;
            }
        });

        return rootView;
    }
    public void onDialogNegativeClick(DialogFragment dialog) {
        String name = dialog.getArguments().getString("Name");
        myAdapter.deleteItem(name);
        myAdapter.notifyDataSetChanged();
    }
}
