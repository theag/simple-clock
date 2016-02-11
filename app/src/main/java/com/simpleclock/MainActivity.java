package com.simpleclock;

import android.content.Context;
import android.content.Intent;
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
import android.widget.CheckBox;
import android.widget.TextClock;
import android.widget.TextView;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnLayoutChangeListener {

    private static final String FLAG_KEEP_SCREEN_ON = "flag keep screen on";
    private Rect bounds;

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
        Alarm.loadAll(new File(getFilesDir() + "alarms.txt"));
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
        if(view.getId() == R.id.checkBox) {
            if(((CheckBox)view).isChecked()) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
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
        Alarm.saveAll(new File(getFilesDir() + "alarms.txt"));
    }

}
