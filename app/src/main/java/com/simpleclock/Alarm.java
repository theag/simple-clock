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

    public static Alarm isAlarmStart(int hourOfDay, int minute) {
        for(Alarm alarm : alarms) {
            if(alarm.active && alarm.time.equals(hourOfDay, minute)) {
                return alarm;
            }
        }
        return null;
    }

    public static int isInAlarm(int hourOfDay, int minute) {
        Alarm alarm;
        for(int i = 0; i < alarms.size(); i++) {
            alarm = alarms.get(i);
            if(alarm.time.diff(hourOfDay, minute) < alarm.lasts) {
                return i;
            }
        }
        return -1;
    }

    public static void saveAll(File file, boolean isInAlarm) {
        try {
            PrintWriter outFile = new PrintWriter(file);
            outFile.println(isInAlarm);
            for(Alarm alarm : alarms) {
                outFile.println(alarm.saveString());
            }
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
        }
    }

    public static boolean loadAll(File file) {
        if(alarms == null) {
            alarms = new ArrayList<>();
        } else {
            alarms.clear();
        }
        boolean isInAlarm;
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(file));
            String line = inFile.readLine();
            isInAlarm = Boolean.parseBoolean(line);
            line = inFile.readLine();
            while(line != null) {
                alarms.add(new Alarm(line));
                line = inFile.readLine();
            }
            inFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            file.delete();
            isInAlarm = false;
        }
        return isInAlarm;
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
    private static final String nullStr = "" +((char)0);

    private Time time;
    private boolean active;
    public boolean expanded;
    public int lasts;
    private boolean[] repeats_on;
    public String label;

    public Alarm(int hourOfDay, int minute) {
        time =new Time(hourOfDay, minute);
        active = true;
        expanded = true;
        lasts = 1;
        repeats_on = new boolean[7];
        for(int i = 0; i < repeats_on.length; i++) {
            repeats_on[i] = true;
        }
        label = "";
    }

    private Alarm(String saveStr) {
        repeats_on = new boolean[7];
        StringTokenizer tokens = new StringTokenizer(saveStr, unitSep);
        time = new Time(Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
        active = Boolean.parseBoolean(tokens.nextToken());
        expanded = Boolean.parseBoolean(tokens.nextToken());
        lasts = Integer.parseInt(tokens.nextToken());
        for(int i = 0; i < repeats_on.length; i++) {
            repeats_on[i] = Boolean.parseBoolean(tokens.nextToken());
        }
        label = tokens.nextToken();
        if(label.equals(nullStr)) {
            label = "";
        }
    }

    private String saveString() {
        String rv = time.hourOfDay +unitSep +time.minute +unitSep +active +unitSep +expanded +unitSep +lasts;
        for(boolean repeat : repeats_on) {
            rv += unitSep +repeat;
        }
        if(label.isEmpty()) {
            rv += unitSep +nullStr;
        } else {
            rv += unitSep + label;
        }
        return rv;
    }


    public String getTime() {
        return time.print();
    }

    public String getTimePosition() {
        return time.getPosition();
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
        time.hourOfDay = hourOfDay;
        time.minute = minute;
    }

    public int getHour() {
        return time.hourOfDay;
    }

    public int getMinute() {
        return time.minute;
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

    public class Time {
        public int hourOfDay;
        public int minute;

        public Time(int hourOfDay, int minute) {
            this.hourOfDay = hourOfDay;
            this.minute = minute;
        }

        public String print() {
            String rv = "";
            if(hourOfDay > 12) {
                rv += (hourOfDay - 12);
            } else if(hourOfDay == 0) {
                rv += "12";
            } else {
                rv += hourOfDay;
            }
            rv += ":";
            if(minute < 10) {
                rv += "0";
            }
            rv += minute;
            return rv;
        }

        public String getPosition() {
            if(hourOfDay >= 12) {
                return "PM";
            } else {
                return "AM";
            }
        }

        public boolean equals(int hourOfDay, int minute) {
            return this.hourOfDay == hourOfDay && this.minute == minute;
        }

        public int diff(int hourOfDay, int minute) {
            int rv = this.minute - minute;
            int hourAdjust = 0;
            while(rv < 0) {
                rv += 60;
                hourAdjust++;
            }
            rv += this.hourOfDay - hourAdjust - hourOfDay;
            return rv;
        }
    }
}
