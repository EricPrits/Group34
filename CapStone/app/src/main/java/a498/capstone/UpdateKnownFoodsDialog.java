package a498.capstone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ericprits on 2018-02-08.
 */

public class UpdateKnownFoodsDialog extends DialogFragment {
    UpdateKnownFoodsDialogListener mListener;

    public interface UpdateKnownFoodsDialogListener{
        void onDialogNegativeKnownClick(DialogFragment dialog);
        void onDialogPositiveKnownClick(DialogFragment dialog);
    }

    public UpdateKnownFoodsDialog(){
    }

    public void setListener(UpdateKnownFoodsDialog.UpdateKnownFoodsDialogListener context){
        this.mListener = context;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final ArrayList<String> list = bundle.getStringArrayList("List");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.dialog_updateknownfoods, null);
        final TextView message = lay.findViewById(R.id.updateMessage);
        final TextView message2 = lay.findViewById(R.id.updateMessage2);
        String messageText;
        String messageText2;



        messageText2 = list.size() + " item(s) will be added to known foods,";
        messageText ="known foods can be altered in the settings tab";

        builder.setView(lay)
                .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("List", list);
                        mListener.onDialogNegativeKnownClick(UpdateKnownFoodsDialog.this);
                    }
                })
                .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putStringArrayList("List", list);
                        mListener.onDialogPositiveKnownClick(UpdateKnownFoodsDialog.this);
                    }
                });


        message.setText(messageText);
        message2.setText(messageText2);
        return builder.create();
    }


}
