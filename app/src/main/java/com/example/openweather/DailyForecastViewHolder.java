package com.example.openweather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class DailyForecastViewHolder extends RecyclerView.ViewHolder {
    TextView dailyDate;
    TextView dailyDayTemp;
    TextView dailyMaxMin;
    TextView dailyNightTemp;
    TextView dailyEveTemp;
    TextView dailyMornTemp;
    TextView dailyDesc;
    TextView dailyPop;
    TextView dailyUV;

    ImageView dailyWeatherIcon;
    TextView eightAM2;
    TextView onePM2;
    TextView fivePM2;
    TextView elevenPM2;

    DailyForecastViewHolder(View v){
        super(v);
        dailyDate = v.findViewById(R.id.dailyDate);
        dailyMaxMin = v.findViewById(R.id.dailyMaxMin);
        dailyDesc = v.findViewById(R.id.dailyDesc);
        dailyPop = v.findViewById(R.id.dailyPop);
        dailyUV = v.findViewById(R.id.dailyUV);
        dailyMornTemp = v.findViewById(R.id.dailyMornTemp);
        dailyDayTemp = v.findViewById(R.id.dailyDayTemp);
        dailyNightTemp = v.findViewById(R.id.dailyNightTemp);
        dailyEveTemp = v.findViewById(R.id.dailyEveTemp);
        dailyWeatherIcon = v.findViewById(R.id.dailyWeatherIcon);
        eightAM2 = v.findViewById(R.id.eightAM2);
        onePM2 = v.findViewById(R.id.onePM2);
        fivePM2 = v.findViewById(R.id.fivePM2);
        elevenPM2 = v.findViewById(R.id.elevenPM2);
    }

}
