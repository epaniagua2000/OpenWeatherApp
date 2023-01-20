package com.example.openweather;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private boolean fahrenheit = true;
    private SharedPreferences.Editor editor;
    private double lat = 41.8675766;
    private double lon = -87.616232;
    private final List<DailyForecast> dailyForecastList = new ArrayList<>();
    private final List<HourlyWeather> hourlyList = new ArrayList<>();
    private String locale = "Chicago, Illinois";
    private HourlyWeatherAdapter hAdapter;
    private RecyclerView hourlyRecycler;
    private SwipeRefreshLayout swiper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("OpenWeather App");
        swiper = findViewById(R.id.swiper);

        swiper.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(hasNetworkConnection()){
                    swiper.setRefreshing(false); // This stops the busy-circle
                    Toast.makeText(swiper.getContext(), "Data Refreshed!", Toast.LENGTH_SHORT).show();
                    doDownload();
                } else{
                    swiper.setRefreshing(false); // This stops the busy-circle
                    Toast.makeText(swiper.getContext(), "Internet connection needed.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        hourlyRecycler = findViewById(R.id.hourlyRecycler);
        hAdapter = new HourlyWeatherAdapter(hourlyList, fahrenheit, this);
        hourlyRecycler.setAdapter(hAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        hourlyRecycler.setLayoutManager(layoutManager);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        if (!sharedPref.contains("FAHRENHEIT")) {
            editor.putBoolean("FAHRENHEIT", true);
            editor.apply();
        }

        if(hasNetworkConnection()){
            doDownload();

        } else{
            TextView c = findViewById(R.id.currentDate);
            c.setText("No Internet Connection");

            TextView eight = findViewById(R.id.eightAM);
            eight.setText("");

            TextView one = findViewById(R.id.onePM);
            one.setText("");

            TextView five = findViewById(R.id.fivePM);
            five.setText("");

            TextView eleven = findViewById(R.id.elevenPM);
            eleven.setText("");

            ImageView icon = findViewById(R.id.currentWeatherIcon);
            icon.setImageResource(0);
        }

    }

    private void doDownload(){
        WeatherDownloadRunnable loaderTaskRunnable = new WeatherDownloadRunnable(this, lat, lon, fahrenheit);
        new Thread(loaderTaskRunnable).start();
    }

    private boolean hasNetworkConnection() {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }

    private String getLocationName(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            String country = address.get(0).getCountryCode();
            String p1 = "";
            String p2 = "";
            if (country.equals("US")) {
                p1 = address.get(0).getLocality();
                p2 = address.get(0).getAdminArea();
            } else {
                p1 = address.get(0).getLocality();
                if (p1 == null)
                    p1 = address.get(0).getSubAdminArea();
                p2 = address.get(0).getCountryName();
            }
            locale = p1 + ", " + p2;
            return locale;
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    private double[] getLatLon(String userProvidedLocation) {
        Geocoder geocoder = new Geocoder(this); // Here, “this” is an Activity
        try {
            List<Address> address =
                    geocoder.getFromLocationName(userProvidedLocation, 1);
            if (address == null || address.isEmpty()) {
                // Nothing returned!
                return null;
            }
            lat = address.get(0).getLatitude();
            lon = address.get(0).getLongitude();

            return new double[] {lat, lon};
        } catch (IOException e) {
            // Failure to get an Address object
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(CurrentWeather c, List<HourlyWeather> hl, List<DailyForecast> df){

        TextView location = findViewById(R.id.location);
        location.setText(String.format("%s",locale));
        TextView dt = findViewById(R.id.currentDate);
        dt.setText(String.format("%s", c.getCurrent_dt()));
        TextView temp = findViewById(R.id.temperature);
        TextView feelslLike = findViewById(R.id.feelsLike);
        TextView windDescr = findViewById(R.id.windSDG);
        TextView visibility = findViewById(R.id.visibility);
        TextView morn = findViewById(R.id.morningTemp);
        TextView day = findViewById(R.id.dayTemp);
        TextView eve = findViewById(R.id.eveTemp);
        TextView night = findViewById(R.id.nightTemp);
        DailyForecast dayF= df.get(0);

        if(fahrenheit) {
            temp.setText(String.format("%.0f\u2109", c.getCurrent_temp()));
            feelslLike.setText(String.format("Feels Like %.0f \u2109", c.getCurrent_feels_like()));

            if(c.getWind_gust()==0){
                windDescr.setText(String.format("Winds: %s at %.0f mph", getDirection(c.getWind_deg()), c.getCurrent_wind_speed()));
            } else {
                windDescr.setText(String.format("Winds: %s at %.0f mph gusting to %.0f mph", getDirection(c.getWind_deg()), c.getCurrent_wind_speed(), c.getWind_gust()));
            }
            windDescr.setText(String.format("Winds: %s at %.0f mph", getDirection(c.getWind_deg()), c.getCurrent_wind_speed()));
            visibility.setText(String.format("Visibility: %.1f mi", (c.getCurrent_visibility() / 1609.344)));
            morn.setText(String.format("%.0f\u2109",dayF.getDailyMornTemp()));
            day.setText(String.format("%.0f\u2109",dayF.getDailyDayTemp()));
            eve.setText(String.format("%.0f\u2109",dayF.getDailyEveTemp()));
            night.setText(String.format("%.0f\u2109",dayF.getDailyNightTemp()));

        }
        else{
            temp.setText(String.format("%.0f\u2103", c.getCurrent_temp()));
            feelslLike.setText(String.format("Feels Like %.0f \u2103", c.getCurrent_feels_like()));

            if(c.getWind_gust()==0){
                windDescr.setText(String.format("Winds: %s at %.0f kmph", getDirection(c.getWind_deg()), c.getCurrent_wind_speed()));
            } else{
                windDescr.setText(String.format("Winds: %s at %.0f kmph gusting to %.0f m/s", getDirection(c.getWind_deg()), c.getCurrent_wind_speed(), c.getWind_gust()));
            }

            visibility.setText(String.format("Visibility: %d km", (c.getCurrent_visibility() / 1000)));
            morn.setText(String.format("%.0f\u2103",dayF.getDailyMornTemp()));
            day.setText(String.format("%.0f\u2103",dayF.getDailyDayTemp()));
            eve.setText(String.format("%.0f\u2103",dayF.getDailyEveTemp()));
            night.setText(String.format("%.0f\u2103",dayF.getDailyNightTemp()));
        }

        ImageView image_weather = findViewById(R.id.currentWeatherIcon);
        int iconResId = this.getResources().getIdentifier(c.getWeather_icon(),
                "drawable", this.getPackageName());

        image_weather.setImageResource(iconResId);

        TextView weatherDescription = findViewById(R.id.weatherDesc);
        weatherDescription.setText(String.format("%s (%d%%",c.getWeather_desc(),c.getCurrent_clouds()) + " clouds)");

        TextView humidity = findViewById(R.id.humidity);
        humidity.setText(String.format("Humidity: %d%%",c.getCurrent_humidity()));

        TextView uvi=findViewById(R.id.uvIndex);
        uvi.setText(String.format("UV Index: %.1f",c.getCurrent_uvi()));

        TextView sunrise = findViewById(R.id.sunrise);
        sunrise.setText(String.format("Sunrise: %s",c.getSunrise()));

        TextView sunset = findViewById(R.id.sunset);
        sunset.setText(String.format("Sunset: %s",c.getSunset()));

        TextView rain = findViewById(R.id.rain);
        if(c.getRain() == 0){
            rain.setText("");
        } else{
            rain.setText(c.getRain() + " mm");
        }

        TextView snow = findViewById(R.id.snow);
        if(c.getSnow() == 0){
            snow.setText("");
        } else{
            snow.setText(c.getSnow() + " mm");
        }

        hourlyList.clear();
        hourlyList.addAll(hl);
        hAdapter.notifyDataSetChanged();
        dailyForecastList.clear();
        dailyForecastList.addAll(df);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(hasNetworkConnection()){
            if (item.getItemId() == R.id.toggleUnits) {
                if(fahrenheit){
                    item.setIcon(ContextCompat.getDrawable(this,R.drawable.units_c));
                    editor.putBoolean("FAHRENHEIT", false);
                    fahrenheit = false;
                    hAdapter.notifyDataSetChanged();
                    editor.apply();
                    doDownload();

                } else{
                    item.setIcon(ContextCompat.getDrawable(this, R.drawable.units_f));
                    editor.putBoolean("FAHRENHEIT", true);
                    fahrenheit = true;
                    hAdapter.notifyDataSetChanged();
                    editor.apply();
                    doDownload();
                }
                return true;

            } else if (item.getItemId() == R.id.dailyForecast) {
                Intent intent = new Intent(this, DailyForecastActivity.class);
                intent.putExtra("DAILY", (Serializable) dailyForecastList);
                intent.putExtra("FAHRENHEIT", fahrenheit);
                intent.putExtra("LOCALE", locale);
                startActivity(intent);
                return true;
            } else if (item.getItemId() == R.id.changeLoc) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                final EditText et = new EditText(this);
                et.setInputType(InputType.TYPE_CLASS_TEXT);
                et.setGravity(Gravity.CENTER_HORIZONTAL);
                builder.setView(et);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        locale = getLocationName(et.getText().toString());
                        if(locale == null){
                            Toast.makeText(builder.getContext(), "Please Enter a Valid Location", Toast.LENGTH_LONG).show();
                        } else{
                            double[] latlon = getLatLon(locale);
                            lat = latlon[0];
                            lon = latlon[1];
                            doDownload();
                        }

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, "No location entered.", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setMessage("For US locations enter as 'City', or 'City, State'\n\nFor international locations enter as 'City, Country'");
                builder.setTitle("Enter a Location");

                AlertDialog dialog = builder.create();
                dialog.show();
                return true;

            } else return super.onOptionsItemSelected(item);
        } else{
            switch(item.getItemId()){
                case R.id.toggleUnits:
                    Toast.makeText(MainActivity.this, "Internet connection needed.", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.dailyForecast:
                    Toast.makeText(MainActivity.this, "Internet connection needed.", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.changeLoc:
                    Toast.makeText(MainActivity.this, "Internet connection needed.", Toast.LENGTH_SHORT).show();
                    return true;
            }
        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putDouble("Lat",lat);
        outState.putDouble("Lon",lon);
        outState.putBoolean("Fahrenheit",fahrenheit);
        outState.putString("Locale",locale);
        Parcelable mState=hourlyRecycler.getLayoutManager().onSaveInstanceState();
        outState.putParcelable("mState",mState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lat=savedInstanceState.getDouble("Lat");
        lon=savedInstanceState.getDouble("Lon");
        fahrenheit=savedInstanceState.getBoolean("Fahrenheit");
        locale=savedInstanceState.getString("Locale");
        Parcelable savedRecycle=savedInstanceState.getParcelable("mState");
        hourlyRecycler.getLayoutManager().onRestoreInstanceState(savedRecycle);
        doDownload();
    }

    private String getDirection(double degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "N";
        if (degrees >= 22.5 && degrees < 67.5)
            return "NE";
        if (degrees >= 67.5 && degrees < 112.5)
            return "E";
        if (degrees >= 112.5 && degrees < 157.5)
            return "SE";
        if (degrees >= 157.5 && degrees < 202.5)
            return "S";
        if (degrees >= 202.5 && degrees < 247.5)
            return "SW";
        if (degrees >= 247.5 && degrees < 292.5)
            return "W";
        if (degrees >= 292.5 && degrees < 337.5)
            return "NW";
        return "X"; // We'll use 'X' as the default if we get a bad value
    }
}
