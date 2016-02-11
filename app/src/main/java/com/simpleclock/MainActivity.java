package com.simpleclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.CheckBox;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.File;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnLayoutChangeListener {

    private static final String FLAG_KEEP_SCREEN_ON = "flag keep screen on";
    private Rect bounds;
    private BroadcastReceiver receiver;
    private IntentFilter filter;
    private BlinkAnimation blink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar)findViewById(R.id.toolBar));
        getSupportActionBar().setTitle("");
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        cb.setChecked(sharedPref.getBoolean(FLAG_KEEP_SCREEN_ON, false));
        if(cb.isChecked()) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        bounds = new Rect();
        findViewById(R.id.textClock).addOnLayoutChangeListener(this);
        boolean wasInAlarm = Alarm.loadAll(new File(getFilesDir() + "alarms.txt"));
        blink = new BlinkAnimation(1.0f, 0.0f);
        if(wasInAlarm) {
            Calendar now = Calendar.getInstance();
            int index = Alarm.isInAlarm(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
            if(index >= 0) {
                blink.setLasts(Alarm.get(index).lasts);
                findViewById(R.id.textClock).startAnimation(blink);
                findViewById(R.id.button_dismiss_alarm).setVisibility(View.VISIBLE);
            }
        }
        filter = new IntentFilter();
        filter.addAction("android.intent.action.TIME_TICK");
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doAlarmCheck();
            }
        };
        registerReceiver(receiver, filter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_alarm) {
            Intent intent = new Intent(this, AlarmActivity.class);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(left != oldLeft || right != oldRight || top != oldTop || bottom != oldBottom) {
            resizeTextClock(left, top, right, bottom);
        }
    }

    private void resizeTextClock(int left, int top, int right, int bottom) {
        TextClock tc = (TextClock)findViewById(R.id.textClock);
        String text = "hh:mm aa";
        int width = right - left + 1;
        int height = bottom - top + 1;
        Paint p = tc.getPaint();
        float size = p.getTextSize();
        p.getTextBounds(text, 0, text.length(), bounds);
        while(bounds.width() < width && bounds.height() < height) {
            size++;
            p.setTextSize(size);
            p.getTextBounds(text, 0, text.length(), bounds);
        }
        size--;
        tc.setTextSize(TypedValue.COMPLEX_UNIT_PX, size);
    }

    public void buttonPress(View view) {
        switch(view.getId()) {
            case R.id.checkBox:
                if(((CheckBox)view).isChecked()) {
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                } else {
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
                break;
            case R.id.button_dismiss_alarm:
                blink.cancel();
                findViewById(R.id.textClock).clearAnimation();
                view.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CheckBox cb = (CheckBox)findViewById(R.id.checkBox);
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(FLAG_KEEP_SCREEN_ON, cb.isChecked());
        editor.commit();
        Alarm.saveAll(new File(getFilesDir() + "alarms.txt"), blink.hasStarted());
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
        if(findViewById(R.id.button_dismiss_alarm).getVisibility() == View.VISIBLE) {
            Calendar now = Calendar.getInstance();
            if(Alarm.isInAlarm(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE)) < 0) {
                blink.cancel();
                findViewById(R.id.textClock).clearAnimation();
                findViewById(R.id.button_dismiss_alarm).setVisibility(View.INVISIBLE);
            }
        }
    }

    private void doAlarmCheck() {
        Calendar now = Calendar.getInstance();
        Alarm alarm = Alarm.isAlarmStart(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE));
        if(alarm != null) {
            blink.setLasts(alarm.lasts);
            findViewById(R.id.textClock).startAnimation(blink);
            findViewById(R.id.button_dismiss_alarm).setVisibility(View.VISIBLE);
        } else if(blink.hasStarted()) {
            blink.increment();
            if(blink.isDone()) {
                findViewById(R.id.textClock).clearAnimation();
                findViewById(R.id.button_dismiss_alarm).setVisibility(View.INVISIBLE);
            }
        }
    }

}
