package com.example.openweather;

import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class WeatherDownloadRunnable implements Runnable {

    private static final String TAG = "WeatherDownloadRunnable";

    private final MainActivity mainActivity;
    private final double lat;
    private final double lon;
    private final boolean fahrenheit;

    private static final String weatherURL = "https://api.openweathermap.org/data/2.5/onecall?";
    private static final String apiKey = "ac548b730f3649d405d190a5e5ec091f";

    WeatherDownloadRunnable(MainActivity mainActivity, double lat, double lon, boolean fahrenheit) {
        this.mainActivity = mainActivity;
        this.lat = lat;
        this.lon = lon;
        this.fahrenheit = fahrenheit;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void run() {

        Uri.Builder buildURL = Uri.parse(weatherURL).buildUpon();

        buildURL.appendQueryParameter("lat", Double.toString(lat));
        buildURL.appendQueryParameter("lon", Double.toString(lon));
        buildURL.appendQueryParameter("units", (fahrenheit ? "imperial" : "metric"));
        buildURL.appendQueryParameter("appid", apiKey);
        buildURL.appendQueryParameter("lang","en");
        buildURL.appendQueryParameter("exclude","minutely");
        String urlToUse = buildURL.build().toString();
        Log.d(TAG, "doInBackground: " + urlToUse);

        StringBuilder sb = new StringBuilder();

        try {

            URL url = new URL(urlToUse);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");
            connection.connect();

            if (connection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                handleResults(null);
                return;
            }

            InputStream is = connection.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }

            Log.d(TAG, "doInBackground: " + sb.toString());


        } catch (Exception e) {
            Log.e(TAG, "doInBackground: ", e);
            handleResults(null);
            return;
        }
        handleResults(sb.toString());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void handleResults(final String jsonString) {

        final CurrentWeather c = parseJSONCurrentWeather(jsonString);
        final List<HourlyWeather> hourlyWeatherList = parseJSONHourlyWeather(jsonString);
        final List<DailyForecast> dailyForecastList = parseJSONDailyForecast(jsonString);
        mainActivity.runOnUiThread(() -> mainActivity.updateData(c,hourlyWeatherList,dailyForecastList));

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private CurrentWeather parseJSONCurrentWeather(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            int timezone_offset = jObjMain.getInt("timezone_offset");
            JSONObject current = jObjMain.getJSONObject("current");

            Log.d(TAG,"doInBackground"+current.toString());
            int dt=current.getInt("dt");
            double temp=current.getDouble("temp");
            double feels_like=current.getDouble("feels_like");
            int clouds=current.getInt("clouds");

            JSONArray weather = current.getJSONArray("weather");
            JSONObject jWeather = (JSONObject) weather.get(0);
            String weather_icon_temp = jWeather.getString("icon");
            String desc = toTitleCase(jWeather.getString("description"));
            double wind_speed = current.getDouble("wind_speed");
            int wind_deg = current.getInt("wind_deg");
            int humidity = current.getInt("humidity");
            double uvi = current.getDouble("uvi");
            int visibility = current.getInt("visibility");
            int sunrise = current.getInt("sunrise");
            int sunset = current.getInt("sunset");
            String weather_icon = "_"+weather_icon_temp;


             //OPTIONAL
            double wind_gust;
            if(current.has("wind_gust")){
                wind_gust = current.getDouble("wind_gust");
            } else{
                wind_gust = 0;
            }

            //OPTIONAL
            double rain_1h;
            if(current.has("rain")){
                JSONObject rain = current.getJSONObject("rain");
                rain_1h = rain.getDouble("1h");
            } else{
                rain_1h = 0;
            }

            double snow_1h;
            if(current.has("snow")){
                JSONObject snow = current.getJSONObject("snow"); //OPTIONAL
                snow_1h = snow.getDouble("1h");
            } else{
                snow_1h = 0;
            }
            return new CurrentWeather(lat, lon, timezone_offset, dt, sunrise, sunset,
                    temp, feels_like, humidity, uvi, visibility, wind_speed, wind_deg,
                    wind_gust, desc, clouds, weather_icon, rain_1h, snow_1h);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<HourlyWeather> parseJSONHourlyWeather(String s) {

        try {
            JSONObject jObjMain = new JSONObject(s);

            JSONObject current = jObjMain.getJSONObject("current");
            JSONArray hourly = jObjMain.getJSONArray("hourly");
            List<HourlyWeather> h = new ArrayList<>();
            for(int i=0;i<hourly.length();i++){
                JSONObject jhourly = (JSONObject) hourly.get(i);
                JSONArray weather = jhourly.getJSONArray("weather");
                JSONObject jWeather = (JSONObject) weather.get(0);
                h.add(new HourlyWeather(current.getInt("dt"),
                        jObjMain.getInt("timezone_offset"),
                        jhourly.getInt("dt"),
                        fahrenheit? String.format("%.0f\u2109",jhourly.getDouble("temp")): String.format("%.0f\u2103",jhourly.getDouble("temp")),
                        toTitleCase(jWeather.getString("description")),
                        "_"+jWeather.getString("icon")
                ));
            }
            return h;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private List<DailyForecast> parseJSONDailyForecast(String s){
        try{
            JSONObject jObjMain = new JSONObject(s);
            JSONArray daily = jObjMain.getJSONArray("daily");
            List<DailyForecast> d = new ArrayList<>();
            for(int i=0; i<daily.length(); i++){
                JSONObject jdaily = (JSONObject) daily.get(i);
                JSONArray weather = jdaily.getJSONArray("weather");
                JSONObject jWeather = (JSONObject) weather.get(0);
                JSONObject temp=jdaily.getJSONObject("temp");
                d.add(new DailyForecast(
                        jObjMain.getInt("timezone_offset"),
                        jdaily.getInt("dt"),
                        temp.getDouble("day"),
                        temp.getDouble("min"),
                        temp.getDouble("max"),
                        temp.getDouble("night"),
                        temp.getDouble("eve"),
                        temp.getDouble("morn"),
                        toTitleCase(jWeather.getString("description")),
                        "_"+jWeather.getString("icon"),
                        jdaily.getInt("pop"),
                        jdaily.getDouble("uvi")
                ));
            }
            return d;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toTitleCase(String givenString) {
        String[] arr = givenString.split(" ");
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < arr.length; i++) {
            sb.append(Character.toUpperCase(arr[i].charAt(0)))
                    .append(arr[i].substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

}