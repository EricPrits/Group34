package a498.capstone;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by patrickgibson on 2017-11-28.
 */

public class CaptureTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.capture_tab, container, false);
        return rootView;
    }
    public void openCamera(View view){
        Intent intent = new Intent(view.getContext(), OcrCaptureActivity.class);
        //intent.putExtra("array_list", array);
        startActivity(intent);
    }
}


