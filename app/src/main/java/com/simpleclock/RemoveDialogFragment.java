package com.simpleclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Created by nbp184 on 2016/02/11.
 */
public class RemoveDialogFragment  extends DialogFragment implements DialogInterface.OnClickListener {

    public interface OnRemoveListener {
        void onRemove(int which, int index);
    }

    private int index;
    private OnRemoveListener listener;

    public RemoveDialogFragment() {
        index = -1;
        listener = null;
    }

    public RemoveDialogFragment setIndex(int index) {
        this.index = index;
        return this;
    }

    public RemoveDialogFragment setOnRemoveListener(OnRemoveListener listener) {
        this.listener = listener;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.confirm_delete)
                .setTitle(R.string.confirm)
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if(listener != null) {
            listener.onRemove(which, index);
        }
    }

}
