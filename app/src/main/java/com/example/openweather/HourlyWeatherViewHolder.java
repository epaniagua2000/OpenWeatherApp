package com.example.openweather;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class HourlyWeatherViewHolder extends RecyclerView.ViewHolder {

    TextView dayName;
    TextView hourlyTime;
    TextView hourlyTemp;
    TextView hourlyDesc;
    ImageView hourlyWeatherIcon;

    HourlyWeatherViewHolder(View v){
        super(v);
        dayName = v.findViewById(R.id.dayName);
        hourlyTime = v.findViewById(R.id.hourlyTime);
        hourlyTemp = v.findViewById(R.id.hourlyTemp);
        hourlyDesc = v.findViewById(R.id.hourlyDesc);
        hourlyWeatherIcon = v.findViewById(R.id.hourlyWeatherIcon);
    }
}
