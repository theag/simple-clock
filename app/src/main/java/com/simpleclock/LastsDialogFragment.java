package com.simpleclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * Created by nbp184 on 2016/02/11.
 */
public class LastsDialogFragment  extends DialogFragment implements DialogInterface.OnClickListener {

    public interface OnLastsSetListener {
        void onLastsSet(String tag, int index, int lasts);
    }

    private OnLastsSetListener listener;
    private int lasts;
    private int index;

    public LastsDialogFragment() {
        lasts = 5;
        listener = null;
        index = -1;
    }

    public LastsDialogFragment setLasts(int lasts) {
        this.lasts = lasts;
        return this;
    }

    public LastsDialogFragment setOnLastsSetListener(OnLastsSetListener listener) {
        this.listener = listener;
        return this;
    }

    public LastsDialogFragment setIndex(int index) {
        this.index = index;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_lasts, null);
        NumberPicker np = (NumberPicker)view.findViewById(R.id.numberPicker);
        np.setMinValue(1);
        np.setMaxValue(59);
        np.setValue(lasts);

        builder.setView(view)
                .setTitle(R.string.lasts_title)
                .setPositiveButton(R.string.ok, this)
                .setNegativeButton(R.string.cancel, this);
        return builder.create();

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == AlertDialog.BUTTON_POSITIVE && listener != null) {
            listener.onLastsSet(getTag(), index, ((NumberPicker)getDialog().findViewById(R.id.numberPicker)).getValue());
        }
    }

}
