package a498.capstone;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by patrickgibson on 2018-02-09.
 */

public class ReceiptDetailedEdit extends DialogFragment {

    public interface ReceiptDetailedEditListener{
        void onPosClick(DialogFragment dialog);
        void onNegClick(DialogFragment dialog);
    }

    public ReceiptDetailedEdit(){
    }

    ReceiptDetailedEditListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstance){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        final String foodType = bundle.getString("foodType");
        final int quantity = bundle.getInt("quantity");
        final int _id = bundle.getInt("id");
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.dialog_detailededit, null);
        final EditText editFoodType = lay.findViewById(R.id.foodType);
        final EditText editQuantity = lay.findViewById(R.id.quantity);

        builder.setView(lay)
                .setPositiveButton(R.string.summarydialog_update, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("id",_id);
                        bundle.putString("foodType", foodType);
                        bundle.putInt("quantity", quantity);
                        setArguments(bundle);
                        mListener.onPosClick(ReceiptDetailedEdit.this);
                    }
                })
                .setNegativeButton(R.string.summarydialog_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Bundle bundle = new Bundle();
                        mListener.onNegClick(ReceiptDetailedEdit.this);
                    }
                });


        editFoodType.setText(foodType);
        editQuantity.setText(quantity);

        return builder.create();
    }



}