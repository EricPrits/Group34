package a498.capstone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by patrickgibson on 2018-02-08.
 */

public class ReceiptSummaryEdit extends DialogFragment {

    public interface ReceiptSummaryEditListener{
        void onDialogPositiveClick(DialogFragment dialog);
        void onDialogNegativeClick(DialogFragment dialog);
    }

    public ReceiptSummaryEdit(){
    }

    ReceiptSummaryEditListener mListener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        mListener = (ReceiptSummaryEditListener) getTargetFragment();
        Bundle bundle = getArguments();
        final int _id = bundle.getInt("id");
        final String name = bundle.getString("Name");
        final String date = bundle.getString("Date");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.dialog_summaryedit, null);
        final EditText editName = lay.findViewById(R.id.receiptName);
        final EditText editDate = lay.findViewById(R.id.receiptDate);

        builder.setView(lay)
                .setPositiveButton(R.string.summarydialog_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", _id);
                        bundle.putString("Name", editName.getText().toString());
                        bundle.putString("Date", editDate.getText().toString());
                        setArguments(bundle);
                        mListener.onDialogPositiveClick(ReceiptSummaryEdit.this);
                    }
                })
                .setNegativeButton(R.string.summarydialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id", _id);
                        mListener.onDialogNegativeClick(ReceiptSummaryEdit.this);
                    }
                });


        editName.setText(name);
        editDate.setText(date);

        return builder.create();
    }


}
