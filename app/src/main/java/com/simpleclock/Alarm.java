package com.simpleclock;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

    public static void remove(int index) {
        if(alarms != null) {
            alarms.remove(index);
        }
    }

    public static void saveAll(File file) {
        try {
            PrintWriter outFile = new PrintWriter(file);
            for(Alarm alarm : alarms) {
                outFile.println(alarm.saveString());
            }
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        }
    }

    public static void loadAll(File file) {
        if(alarms == null) {
            alarms = new ArrayList<>();
        } else {
            alarms.clear();
        }
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(file));
            String line = inFile.readLine();
            while(line != null) {
                alarms.add(new Alarm(line));
                line = inFile.readLine();
            }
            inFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        }
    }

    public static final int SUNDAY = 0;
    public static final int MONDAY = 1;
    public static final int TUESDAY = 2;
    public static final int WEDNESDAY = 3;
    public static final int THURSDAY = 4;
    public static final int FRIDAY = 5;
    public static final int SATURDAY = 6;

    private static final String[] weekDays = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private static final String unitSep = "" +((char)31);

    private int[] time;
    private boolean active;
    public boolean expanded;
    public int lasts;
    private boolean[] repeats_on;
    public String label;

    public Alarm(int hourOfDay, int minute) {
        time = new int[]{hourOfDay, minute};
        active = true;
        expanded = true;
        lasts = 5;
        repeats_on = new boolean[7];
        for(int i = 0; i < repeats_on.length; i++) {
            repeats_on[i] = true;
        }
        label = "";
    }

    private Alarm(String saveStr) {
        time = new int[2];
        repeats_on = new boolean[7];
        StringTokenizer tokens = new StringTokenizer(saveStr, unitSep);
        time[0] = Integer.parseInt(tokens.nextToken());
        time[1] = Integer.parseInt(tokens.nextToken());
        active = Boolean.parseBoolean(tokens.nextToken());
        expanded = Boolean.parseBoolean(tokens.nextToken());
        lasts = Integer.parseInt(tokens.nextToken());
        for(int i = 0; i < repeats_on.length; i++) {
            repeats_on[i] = Boolean.parseBoolean(tokens.nextToken());
        }
        label = tokens.nextToken();
    }

    private String saveString() {
        String rv = time[0] +unitSep +time[1] +unitSep +active +unitSep +expanded +unitSep +lasts;
        for(boolean repeat : repeats_on) {
            rv += unitSep +repeat;
        }
        rv += unitSep +label;
        return rv;
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
        String rv = "";
        for(int i = 0; i < repeats_on.length; i++) {
            if(repeats_on[i]) {
                if(!rv.isEmpty()) {
                    rv += ", ";
                }
                rv += weekDays[i];
            }
        }
        if(rv.isEmpty()) {
            rv = "Doesn't occur";
        }
        return rv;
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

    public void switchRepeat(int dayOfWeek) {
        repeats_on[dayOfWeek] = !repeats_on[dayOfWeek];
        if(noRepeat()) {
            active = false;
        }
    }

    private boolean noRepeat() {
        for(boolean repeatOn : repeats_on) {
            if(repeatOn) {
                return false;
            }
        }
        return true;
    }

    public boolean setActive(boolean active) {
        if(active && !this.active && noRepeat()) {
            return false;
        }
        this.active = active;
        return true;
    }

    public boolean isActive() {
        return active;
    }

    public boolean repeatsOn(int dayOfWeek) {
        return repeats_on[dayOfWeek];
    }
}
