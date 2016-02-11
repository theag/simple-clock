package com.simpleclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Created by nbp184 on 2016/02/10.
 */
public class AlarmAdapter extends BaseAdapter implements ListAdapter {

    private Context context;

    public AlarmAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return Alarm.count();
    }

    @Override
    public Object getItem(int position) {
        return Alarm.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_alarm, null);
        }

        Alarm current = Alarm.get(position);

        TextView tv = (TextView)view.findViewById(R.id.text_time);
        tv.setText(current.getTime());

        tv = (TextView)view.findViewById(R.id.text_lasts);
        tv.setText(context.getString(R.string.lasts) + " " + current.lasts + " " + context.getString(R.string.minute_short));

        Switch s = (Switch)view.findViewById(R.id.switch_active);
        s.setChecked(current.isActive());

        if(current.expanded) {
            view.findViewById(R.id.layout_summary).setVisibility(View.GONE);
            view.findViewById(R.id.layout_expanded).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.color.colorPrimayLight);

            Button b = (Button)view.findViewById(R.id.button_sunday);
            if(current.repeatsOn(Alarm.SUNDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_monday);
            if(current.repeatsOn(Alarm.MONDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_tuesday);
            if(current.repeatsOn(Alarm.TUESDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_wednesday);
            if(current.repeatsOn(Alarm.WEDNESDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_thursday);
            if(current.repeatsOn(Alarm.THURSDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_friday);
            if(current.repeatsOn(Alarm.FRIDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_saturday);
            if(current.repeatsOn(Alarm.SATURDAY)) {
                b.setBackgroundResource(R.drawable.ic_circle_accent_36dp);
            } else {
                b.setBackgroundResource(R.drawable.ic_circle_transparent_36dp);
            }

            b = (Button)view.findViewById(R.id.button_label);
            if(current.label.isEmpty()) {
                b.setText(context.getString(R.string.label));
            } else {
                b.setText(current.label);
            }
        } else {
            view.findViewById(R.id.layout_summary).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layout_expanded).setVisibility(View.GONE);
            view.setBackgroundResource(0);
            tv = (TextView)view.findViewById(R.id.text_label);
            if(current.label.isEmpty()) {
                tv.setVisibility(View.GONE);
            } else {
                tv.setVisibility(View.VISIBLE);
                tv.setText(current.label);
            }
            tv = (TextView)view.findViewById(R.id.text_summary);
            tv.setText(current.getSummary());
        }

        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
