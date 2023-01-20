package com.example.openweather;

import android.graphics.Bitmap;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherViewHolder>{
    private static final String TAG = "HourlyWeatherAdapter";
    private final List<HourlyWeather> hourlyWeatherList;
    private final MainActivity mainAct;
    private final boolean fahrenheit;

    HourlyWeatherAdapter(List<HourlyWeather> hourlyWeatherList, boolean f, MainActivity ma){
        this.hourlyWeatherList = hourlyWeatherList;
        this.fahrenheit = f;
        mainAct = ma;
    }

    @NonNull
    @Override
    public HourlyWeatherViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewtype){
        Log.d(TAG, "HourlyWeatherViewHolder onCreateViewHolder: MAKING NEW");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather_layout, parent, false);

        return new HourlyWeatherViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull HourlyWeatherViewHolder holder, int position){
        HourlyWeather hourlyWeather = hourlyWeatherList.get(position);

        holder.dayName.setText(String.format("%s", hourlyWeather.getDayName()));
        holder.hourlyTime.setText(String.format("%s", hourlyWeather.getHourlyTime()));
        int iconResId = mainAct.getResources().getIdentifier(hourlyWeather.getHourlyWeatherIcon(),
                "drawable", mainAct.getPackageName());
        holder.hourlyWeatherIcon.setImageResource(iconResId);
        if(fahrenheit) {
            holder.hourlyTemp.setText(String.format("%s", hourlyWeather.getHourlyTemp()));
        }
        else{
            holder.hourlyTemp.setText(String.format("%s", hourlyWeather.getHourlyTemp()));
        }
        holder.hourlyDesc.setText(String.format("%s", hourlyWeather.getHourlyDesc()));

    }

    @Override
    public int getItemCount() {
        return hourlyWeatherList.size();
    }
}
