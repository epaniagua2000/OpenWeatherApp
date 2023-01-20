package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class HourlyWeather implements Serializable {
    final private int dayName; //dt
    final private int timezone_offset;
    final private int hourlyTime;
    final private String hourlyTemp;
    final private String hourlyDesc;
    final private String hourlyWeatherIcon;

    public HourlyWeather(int dayName, int timezone_offset, int hourlyTime, String hourlyTemp, String hourlyDesc, String hourlyWeatherIcon) {
        this.dayName = dayName;
        this.timezone_offset = timezone_offset;
        this.hourlyTime = hourlyTime;
        this.hourlyWeatherIcon = hourlyWeatherIcon;
        this.hourlyTemp = hourlyTemp;
        this.hourlyDesc = hourlyDesc;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getDayName() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(hourlyTime + timezone_offset, 0, ZoneOffset.UTC);
        LocalDateTime ldt_current = LocalDateTime.ofEpochSecond(dayName + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        String formattedCurrent=ldt_current.format(dtf);
        if(formattedCurrent.equals(formattedTimeString)){
            return "Today";
        }
        return formattedTimeString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getHourlyTime() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(hourlyTime + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }

    public String getHourlyWeatherIcon() {
        return hourlyWeatherIcon;
    }

    public String getHourlyTemp() {
        return hourlyTemp;
    }

    public String getHourlyDesc() {
        return hourlyDesc;
    }

}
