package com.simpleclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by nbp184 on 2016/02/11.
 */
public class LabelDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public interface OnLabelSetListener {
        void onLabelSet(String tag, int index, String label);
    }

    private OnLabelSetListener listener;
    private String label;
    private int index;

    public LabelDialogFragment() {
        label = "";
        listener = null;
        index = -1;
    }

    public LabelDialogFragment setLabel(String label) {
        this.label = label;
        return this;
    }

    public LabelDialogFragment setOnLabelSetListener(OnLabelSetListener listener) {
        this.listener = listener;
        return this;
    }

    public LabelDialogFragment setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_label, null);
        if(!label.isEmpty()) {
            EditText et = (EditText)view.findViewById(R.id.text_label);
            et.setText(label);
            et.selectAll();
        }

        builder.setView(view)
                .setTitle(R.string.label)
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.cancel, this);
        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE && listener != null) {
            listener.onLabelSet(getTag(), index, ((EditText)getDialog().findViewById(R.id.text_label)).getText().toString());
        }
    }

}
