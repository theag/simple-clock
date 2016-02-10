package com.simpleclock;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by nbp184 on 2016/02/10.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public interface OnTimeSetListener {
        void onTimeSet(String tag, int index, int hourOfDay, int minute);
    }

    private Calendar date;
    private OnTimeSetListener listener;
    private int index;

    public TimePickerFragment() {
        date = Calendar.getInstance();
        listener = null;
        index = -1;
    }

    public TimePickerFragment setDate(Calendar date) {
        this.date = date;
        return this;
    }

    public TimePickerFragment setOnTimeSetListener(OnTimeSetListener listener) {
        this.listener = listener;
        return this;
    }

    public TimePickerFragment setIndex(int index) {
        this.index = index;
        return this;
    }

    public TimePickerFragment setTime(int hourOfDay, int minute) {
        date.set(Calendar.HOUR_OF_DAY, hourOfDay);
        date.set(Calendar.MINUTE, minute);
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new TimePickerDialog(getActivity(), this, date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE), false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if(listener != null) {
            listener.onTimeSet(getTag(), index, hourOfDay, minute);
        }
    }
}
