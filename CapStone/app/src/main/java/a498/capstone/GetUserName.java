package a498.capstone;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by patrickgibson on 2018-02-14.
 */

public class GetUserName extends DialogFragment {

    public GetUserName(){

    }
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LinearLayout lay = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.newname, null);
        final EditText editName = lay.findViewById(R.id.newName);
        editName.setHint("Enter Name");
        builder.setView(lay).setMessage("Please enter your name")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String name = editName.getText().toString();
                        getActivity().getSharedPreferences("NAME", getActivity().MODE_PRIVATE)
                                .edit()
                                .putString("Name", name)
                                .apply();
                    }
                });

        return builder.create();
    }
}
