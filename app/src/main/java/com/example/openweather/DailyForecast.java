package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DailyForecast implements Serializable {
    final private int timezone_offset;
    final private double dailyDate; //dt
    final private double dailyDayTemp;
    final private double dailyMin;
    final private double dailyMax;
    final private double dailyNightTemp;
    final private double dailyEveTemp;
    final private double dailyMornTemp;
    final private String dailyDesc;
    final private String daily_weather_icon;
    final private int dailyPop;
    final private double dailyUVI;

    public DailyForecast(int timezone_offset, double dailyDate, double dailyDayTemp, double dailyMin,
                         double dailyMax, double dailyNightTemp, double dailyEveTemp, double dailyMornTemp,
                         String dailyDesc, String daily_weather_icon, int dailyPop, double dailyUVI) {
        this.timezone_offset = timezone_offset;
        this.dailyDate = dailyDate;
        this.dailyDayTemp = dailyDayTemp;
        this.dailyMin = dailyMin;
        this.dailyMax = dailyMax;
        this.dailyNightTemp = dailyNightTemp;
        this.dailyEveTemp = dailyEveTemp;
        this.dailyMornTemp = dailyMornTemp;
        this.dailyDesc = dailyDesc;
        this.daily_weather_icon = daily_weather_icon;
        this.dailyPop = dailyPop;
        this.dailyUVI = dailyUVI;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDailyDate() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond((long) (dailyDate + timezone_offset), 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE, M/dd", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }

    public double getDailyDayTemp() { return dailyDayTemp; }

    public double getDailyMin() { return dailyMin; }

    public double getDailyMax() { return dailyMax; }

    public double getDailyNightTemp() { return dailyNightTemp; }

    public double getDailyEveTemp() { return dailyEveTemp; }

    public double getDailyMornTemp() { return dailyMornTemp; }

    public String getDailyDesc() { return dailyDesc; }

    public String getDaily_weather_icon() { return daily_weather_icon; }

    public int getDailyPop() { return dailyPop; }

    public double getDailyUVI() { return dailyUVI; }
}