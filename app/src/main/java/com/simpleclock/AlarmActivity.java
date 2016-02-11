package com.simpleclock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Switch;

import java.io.File;

public class AlarmActivity extends AppCompatActivity implements TimePickerFragment.OnTimeSetListener, LabelDialogFragment.OnLabelSetListener, LastsDialogFragment.OnLastsSetListener, RemoveDialogFragment.OnRemoveListener {

    private static final String ADD_TIME_DIALOG = "add time dialog";
    private static final String SET_TIME_DIALOG = "set time dialog";
    private static final String SET_LASTS_DIALOG = "set lasts dialog";
    private static final String SET_LABEL_DIALOG = "set label dialog";
    private static final String REMOVE_DIALOG = "remove dialog";

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
                LastsDialogFragment ldf = new LastsDialogFragment();
                ldf.setOnLastsSetListener(this)
                        .setIndex(index)
                        .setLasts(current.lasts)
                        .show(getSupportFragmentManager(), SET_LASTS_DIALOG);
                break;
            case R.id.switch_active:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                Switch s = (Switch)view;
                if(current.setActive(s.isChecked())) {
                    aa.notifyDataSetChanged();
                } else {
                    s.setChecked(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(R.string.error_no_repeat)
                            .setTitle(R.string.error)
                            .setPositiveButton(R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                break;
            case R.id.button_sunday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.SUNDAY);
                aa.notifyDataSetChanged();
                break;
            case R.id.button_monday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.MONDAY);
                aa.notifyDataSetChanged();
                break;

            case R.id.button_tuesday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.TUESDAY);
                aa.notifyDataSetChanged();
                break;

            case R.id.button_wednesday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.WEDNESDAY);
                aa.notifyDataSetChanged();
                break;

            case R.id.button_thursday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.THURSDAY);
                aa.notifyDataSetChanged();
                break;

            case R.id.button_friday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.FRIDAY);
                aa.notifyDataSetChanged();
                break;

            case R.id.button_saturday:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                current.switchRepeat(Alarm.SATURDAY);
                aa.notifyDataSetChanged();
                break;
            case R.id.button_label:
                index = lv.getPositionForView(view);
                current = Alarm.get(index);
                LabelDialogFragment lbdf = new LabelDialogFragment();
                lbdf.setOnLabelSetListener(this)
                        .setIndex(index)
                        .setLabel(current.label)
                        .show(getSupportFragmentManager(), SET_LABEL_DIALOG);
                break;
            case R.id.button_delete:
                index = lv.getPositionForView(view);
                RemoveDialogFragment rdf = new RemoveDialogFragment();
                rdf.setOnRemoveListener(this)
                        .setIndex(index)
                        .show(getSupportFragmentManager(), REMOVE_DIALOG);
                break;
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

    @Override
    public void onLastsSet(String tag, int index, int lasts) {
        if(SET_LASTS_DIALOG.equals(tag)) {
            Alarm.get(index).lasts = lasts;
            ListView lv = (ListView)findViewById(R.id.listView);
            AlarmAdapter aa = (AlarmAdapter)lv.getAdapter();
            aa.notifyDataSetChanged();
        }
    }

    @Override
    public void onLabelSet(String tag, int index, String label) {
        if(SET_LABEL_DIALOG.equals(tag)) {
            Alarm.get(index).label = label;
            ListView lv = (ListView)findViewById(R.id.listView);
            AlarmAdapter aa = (AlarmAdapter)lv.getAdapter();
            aa.notifyDataSetChanged();
        }
    }

    @Override
    public void onRemove(int which, int index) {
        if(which == AlertDialog.BUTTON_POSITIVE) {
            Alarm.remove(index);
            ListView lv = (ListView)findViewById(R.id.listView);
            AlarmAdapter aa = (AlarmAdapter)lv.getAdapter();
            aa.notifyDataSetChanged();
        }
    }
}
