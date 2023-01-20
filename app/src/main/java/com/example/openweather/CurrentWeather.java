package com.example.openweather;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CurrentWeather {
    final private double lat; //
    final private double lon; //
    final private int timezone_offset; //
    final private int current_dt; //
    final private int sunrise; //
    final private int sunset; //
    final private double current_temp; //
    final private double current_feels_like; //
    final private int current_humidity; //
    final private double current_uvi; //
    final private int current_visibility; //
    final private double current_wind_speed; //
    final private int wind_deg; //
    final private double wind_gust; //optional
    final private String weather_desc; //
    final private int current_clouds; //
    final private String weather_icon;//
    final private double rain; //optional
    final private double snow; //optional

    public CurrentWeather(double lat, double lon, int timezone_offset, int current_dt, int sunrise, int sunset,
                          double current_temp, double current_feels_like, int current_humidity, double current_uvi,
                          int current_visibility, double current_wind_speed, int wind_deg, double wind_gust,
                          String weather_desc, int current_clouds, String weather_icon, double rain, double snow) {
        this.lat = lat;
        this.lon = lon;
        this.timezone_offset = timezone_offset;
        this.current_dt = current_dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.current_temp = current_temp;
        this.current_feels_like = current_feels_like;
        this.current_humidity = current_humidity;
        this.current_uvi = current_uvi;
        this.current_visibility = current_visibility;
        this.current_wind_speed = current_wind_speed;
        this.wind_deg = wind_deg;
        this.wind_gust = wind_gust;
        this.weather_desc = weather_desc;
        this.current_clouds = current_clouds;
        this.weather_icon = weather_icon;
        this.rain = rain;
        this.snow = snow;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getCurrent_dt() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(current_dt + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE MMM dd h:mm a, yyyy", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSunrise() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(sunrise + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String getSunset() {
        LocalDateTime ldt = LocalDateTime.ofEpochSecond(sunset + timezone_offset, 0, ZoneOffset.UTC);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("h:mm a", Locale.getDefault());
        String formattedTimeString = ldt.format(dtf);
        return formattedTimeString;
    }

    public double getCurrent_temp() { return current_temp; }

    public double getCurrent_feels_like() { return current_feels_like; }

    public int getCurrent_humidity() { return current_humidity; }

    public double getCurrent_uvi() { return current_uvi; }

    public int getCurrent_visibility() { return current_visibility; }

    public double getCurrent_wind_speed() { return current_wind_speed; }

    public int getWind_deg() { return wind_deg; }

    public double getWind_gust() { return wind_gust; }

    public String getWeather_desc() { return weather_desc; }

    public int getCurrent_clouds() { return current_clouds; }

    public String getWeather_icon() { return weather_icon; }

    public double getRain() { return rain; }

    public double getSnow() { return snow; }
}
