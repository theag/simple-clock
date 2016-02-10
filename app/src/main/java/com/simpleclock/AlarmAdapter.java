package com.simpleclock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
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

        tv = (TextView)view.findViewById(R.id.text_time_position);
        tv.setText(current.getTimePosition());

        tv = (TextView)view.findViewById(R.id.text_lasts);
        tv.setText(context.getString(R.string.lasts) +" " +current.lasts +" " +context.getString(R.string.minute_short));

        if(current.expanded) {
            view.findViewById(R.id.layout_summary).setVisibility(View.GONE);
            view.findViewById(R.id.layout_expanded).setVisibility(View.VISIBLE);
            view.setBackgroundResource(R.color.colorPrimayLight);
        } else {
            view.findViewById(R.id.layout_summary).setVisibility(View.VISIBLE);
            view.findViewById(R.id.layout_expanded).setVisibility(View.GONE);
            view.setBackgroundResource(0);
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
