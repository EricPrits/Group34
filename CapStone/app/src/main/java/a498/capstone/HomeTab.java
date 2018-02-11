package a498.capstone;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class HomeTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.home_tab, container, false);
        return rootView;
    }
    public void generateList(View view){
       //Remove current view viewToUse.setImageResource(0);

    }




}
