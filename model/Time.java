package com.alegria.utg.model;

import com.alegria.utg.utilities.Constants;

/**
 * @author Alberto Alegria
 */
public class Time {
    private int hour;
    private int absHour;
    private int shift;
    private String day;

    public Time(int hour, int shift, String day) {
        this.hour = hour;
        this.shift = shift;
        this.day = day;
    }

    public Time(int hour) {
        this.hour = hour % 14;
        if (this.hour == 0) {
            this.hour = 14;
        }

        shift = getShift(this.hour);

        day = getDay(((hour - 1) / 14) + 1);
    }

    public Time() {}

    public String getDay(int hour) {
        String str = "";
        switch (hour) {
            case 1:
                str = Constants.Day.MONDAY;
                break;

            case 2:
                str = Constants.Day.TUESDAY;
                break;

            case 3:
                str = Constants.Day.WEDNESDAY;
                break;

            case 4:
                str = Constants.Day.THURSDAY;
                break;

            case 5:
                str = Constants.Day.FRIDAY;
                break;

        }
        return str;
    }

    public int getShift(int hour) {
        return (hour <= 7) ? Constants.Shift.MORNING : Constants.Shift.AFTERNOON;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getShift() {
        return shift;
    }

    public String getShiftString() {
        return (shift == Constants.Shift.MORNING) ? "Morning" : "Afternoon";
    }

    public void setShift(int shift) {
        this.shift = shift;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int dayToInt() {
        int d = 0;
        switch (day) {
            case Constants.Day.MONDAY:
                d = 1;
                break;

            case Constants.Day.TUESDAY:
                d = 2;
                break;

            case Constants.Day.WEDNESDAY:
                d = 3;
                break;

            case Constants.Day.THURSDAY:
                d = 4;
                break;

            case Constants.Day.FRIDAY:
                d = 5;
                break;
        }
        return d;
    }

    public boolean isInSameDay(Time anotherTime) {
        return this.day.equals(anotherTime.getDay());
    }

    public int getHourDifference(Time anotherTime) {
        int difference = this.hour - anotherTime.getHour();

        if (difference < 0) {
            difference = difference * -1;
        }
        return difference;
    }

    public int getDayDifference(Time anotherTime) {
        int difference = this.dayToInt() - anotherTime.dayToInt();

        if (difference < 0) {
            difference = difference * -1;
        }

        return difference;
    }

    public int getAbsHour() {
        return hour + (14 * (dayToInt() - 1));
    }

    public String getFormattedTime() {
        String time = "null";

        switch (hour) {
            case 1:
                time = "7:00-8:00";
                break;

            case 2:
                time = "8:00-9:00";
                break;
            case 3:
                time = "9:00-10:00";
                break;

            case 4:
                time = "10:00-11:00";
                break;

            case 5:
                time = "11:00-12:00";
                break;

            case 6:
                time = "12:00-13:00";
                break;

            case 7:
                time = "13:00-14:00";
                break;

            case 8:
                time = "14:00-15:00";
                break;

            case 9:
                time = "15:00-16:00";
                break;

            case 10:
                time = "16:000-17:00";
                break;

            case 11:
                time = "17:00-18:00";
                break;

            case 12:
                time = "18:00-19:00";
                break;

            case 13:
                time = "19:00-20:00";
                break;

            case 14:
                time = "20:00-21:00";
                break;
        }

        return time;
    }

    @Override
    public boolean equals(Object time) {
        Time time1 = (Time) time;
        return (this.hour == time1.getHour()) && (this.day.equals(time1.getDay()));
    }




    public String toString() {
        return day + ", " + getFormattedTime() + ", " + getShiftString();
    }

}
