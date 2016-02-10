package com.simpleclock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by nbp184 on 2016/02/10.
 */
public class Alarm {

    private static ArrayList<Alarm> alarms = null;

    public static int count() {
        if(alarms == null) {
            return 0;
        } else {
            return alarms.size();
        }
    }

    public static void add(Alarm alarm) {
        if (alarms == null) {
            alarms = new ArrayList<>();
        }
        alarms.add(alarm);
    }

    public static Alarm get(int index) {
        if(alarms == null) {
            return null;
        } else {
            return alarms.get(index);
        }
    }

    private int[] time;
    public boolean isActive;
    public boolean expanded;
    public int lasts;

    public Alarm(int hourOfDay, int minute) {
        time = new int[]{hourOfDay, minute};
        isActive = true;
        expanded = true;
        lasts = 5;
    }

    public String getTime() {
        String rv = "";
        if(time[0] > 12) {
            rv += (time[0] - 12);
        } else if(time[0] == 0) {
            rv += "12";
        } else {
            rv += time[0];
        }
        rv += ":";
        if(time[1] < 10) {
            rv += "0";
        }
        rv += time[1];
        return rv;
    }

    public String getTimePosition() {
        if(time[0] >= 12) {
            return "PM";
        } else {
            return "AM";
        }
    }

    public String getSummary() {
        Calendar now = Calendar.getInstance();
        if(time[0] < now.get(Calendar.HOUR_OF_DAY) || (time[0] == now.get(Calendar.HOUR_OF_DAY) && time[1] <= now.get(Calendar.MINUTE))) {
            return "Tomorrow";
        } else {
            return "Today";
        }
    }

    public void setTime(int hourOfDay, int minute) {
        time[0] = hourOfDay;
        time[1] = minute;
    }

    public int getHour() {
        return time[0];
    }

    public int getMinute() {
        return time[1];
    }
}
