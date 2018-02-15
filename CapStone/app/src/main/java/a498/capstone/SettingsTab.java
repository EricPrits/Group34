package a498.capstone;


import android.support.v4.app.DialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;


/**
 * Created by patrickgibson on 2017-11-28.
 */

public class SettingsTab extends Fragment implements AdditionalFoodsDeleteDialog.AdditionalFoodsDeleteDialogListener {
    Receipt_dbAdapter receipt_db;
    AdditionalFoodsAdapter myAdapter;
    ArrayList<String> list;
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_DARK_THEME = "dark_theme";
    boolean useDarkTheme;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.settings_tab, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean(PREF_DARK_THEME, false);

        list = new ArrayList<String>();
        receipt_db = new Receipt_dbAdapter(getContext());
        list =receipt_db.getNewAdditionalFoods();

        ListView listView = rootView.findViewById(R.id.additionalFoodsList);
        myAdapter = new AdditionalFoodsAdapter(getContext(), list);
        listView.setAdapter(myAdapter);

        listView.setOnItemLongClickListener(new ListView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String row = (String) parent.getItemAtPosition(position);
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


        Switch choice = rootView.findViewById(R.id.switch1);
        choice.setChecked(useDarkTheme);
        choice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                toggleTheme(isChecked);
            }
        });

        final EditText et = rootView.findViewById(R.id.setName);
        SharedPreferences preferences1 = getActivity().getSharedPreferences("NAME", getActivity().MODE_PRIVATE);
        String name = preferences1.getString("Name", "Name");
        et.setText(name);

        Button button = rootView.findViewById(R.id.button4) ;
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                String name = et.getText().toString();
                getActivity().getSharedPreferences("NAME", getActivity().MODE_PRIVATE)
                        .edit()
                        .putString("Name",name )
                        .apply();
            }
        });


        return rootView;
    }

    public void onDialogNegativeClick(DialogFragment dialog) {
        String name = dialog.getArguments().getString("Name");
        receipt_db.deleteAdditionalFoods(name);
        myAdapter.deleteItem(name);
        myAdapter.notifyDataSetChanged();
    }


    public void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        View view = getLayoutInflater().inflate(R.layout.settings_tab, null);

    }

    public void toggleTheme(boolean darkTheme) {
        SharedPreferences.Editor editor = getActivity().getSharedPreferences(PREFS_NAME, getActivity().MODE_PRIVATE).edit();
        editor.putBoolean(PREF_DARK_THEME, darkTheme);
        editor.apply();

        Intent intent = getActivity().getIntent();
        getActivity().finish();

        startActivity(intent);
    }
}
