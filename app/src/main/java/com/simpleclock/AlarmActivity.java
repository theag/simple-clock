package com.simpleclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class AlarmActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSetListener {

    private static final String ADD_TIME_DIALOG = "add time dialog";
    private static final String SET_TIME_DIALOG = "set time dialog";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        getSupportActionBar().setTitle("");
        ListView lv = (ListView)findViewById(R.id.listView);
        lv.setAdapter(new AlarmAdapter(this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_alarm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_clock) {
            finish();
        }
        return true;
    }

    public void buttonPress(View view) {
        ListView lv = (ListView)findViewById(R.id.listView);
        AlarmAdapter aa = (AlarmAdapter)lv.getAdapter();
        switch(view.getId()) {
            case R.id.button_add:
                TimePickerFragment tpf = new TimePickerFragment();
                tpf.setOnTimeSetListener(this)
                        .show(getSupportFragmentManager(), ADD_TIME_DIALOG);
                break;
            case R.id.button_expand:
                Alarm.get(lv.getPositionForView(view)).expanded = true;
                aa.notifyDataSetChanged();
                break;
            case R.id.button_shrink:
                Alarm.get(lv.getPositionForView(view)).expanded = false;
                aa.notifyDataSetChanged();
                break;
            case R.id.layout_time:
                int index = lv.getPositionForView(view);
                Alarm current = Alarm.get(index);
                tpf = new TimePickerFragment();
                tpf.setOnTimeSetListener(this)
                        .setTime(current.getHour(), current.getMinute())
                        .setIndex(index)
                        .show(getSupportFragmentManager(), SET_TIME_DIALOG);
                break;
            case R.id.text_lasts:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);

        }
    }

    @Override
    public void onTimeSet(String tag, int index, int hourOfDay, int minute) {
        ListView lv = (ListView)findViewById(R.id.listView);
        AlarmAdapter aa = (AlarmAdapter)lv.getAdapter();
        switch(tag) {
            case ADD_TIME_DIALOG:
                Alarm.add(new Alarm(hourOfDay, minute));
                aa.notifyDataSetChanged();
                break;
            case SET_TIME_DIALOG:
                Alarm.get(index).setTime(hourOfDay, minute);
                aa.notifyDataSetChanged();
                break;
        }
    }
}
