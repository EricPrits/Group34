package a498.capstone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by ericprits on 2018-02-08.
 */

public class AdditionalFoodsDeleteDialog extends DialogFragment {
    AdditionalFoodsDeleteDialogListener mListener;

    public interface AdditionalFoodsDeleteDialogListener{
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public AdditionalFoodsDeleteDialog(){
    }

    public void setListener(AdditionalFoodsDeleteDialog.AdditionalFoodsDeleteDialogListener context){
        this.mListener = context;
    }




    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        mListener = (AdditionalFoodsDeleteDialog.AdditionalFoodsDeleteDialogListener) getTargetFragment();
        final String Name = bundle.getString("Name");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.dialog_parsedelete, null);
        final TextView NameView = lay.findViewById(R.id.itemName);

        builder.setView(lay)
                .setNegativeButton(R.string.summarydialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putString("Name", Name);
                        mListener.onDialogNegativeClick(AdditionalFoodsDeleteDialog.this);
                    }
                });


        NameView.setText(Name);

        return builder.create();
    }


}
