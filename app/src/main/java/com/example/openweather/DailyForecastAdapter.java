package com.example.openweather;

import android.graphics.Bitmap;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyForecastAdapter extends RecyclerView.Adapter<DailyForecastViewHolder> {
    private static final String TAG = "DailyForecastAdapter";
    private final List<DailyForecast> dailyForecastList;
    private final boolean fahrenheit;
    private final DailyForecastActivity dfAct;

    DailyForecastAdapter(List<DailyForecast> dailyForecastList, boolean f, DailyForecastActivity ma){
        this.dailyForecastList = dailyForecastList;
        this.fahrenheit = f;
        dfAct = ma;
    }

    @NonNull
    @Override
    public DailyForecastViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {
        Log.d(TAG, "DailyForecastViewHolder onCreateViewHolder: MAKING NEW");

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_forecast_layout, parent, false);

        return new DailyForecastViewHolder(itemView);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull DailyForecastViewHolder holder, int position) {
        DailyForecast day = dailyForecastList.get(position);
        holder.dailyDate.setText(String.format("%s",day.getDailyDate()));

        if(fahrenheit){
            holder.dailyMaxMin.setText(String.format("%.0f\u2109/%.0f\u2109",day.getDailyMax(),day.getDailyMin()));
            holder.dailyMornTemp.setText(String.format("%.0f\u2109",day.getDailyMornTemp()));
            holder.dailyDayTemp.setText(String.format("%.0f\u2109",day.getDailyDayTemp()));
            holder.dailyEveTemp.setText(String.format("%.0f\u2109",day.getDailyEveTemp()));
            holder.dailyNightTemp.setText(String.format("%.0f\u2109",day.getDailyNightTemp()));
        } else{
            holder.dailyMaxMin.setText(String.format("%.0f\u2103/%.0f\u2103",day.getDailyMax(),day.getDailyMin()));
            holder.dailyMornTemp.setText(String.format("%.0f\u2103",day.getDailyMornTemp()));
            holder.dailyDayTemp.setText(String.format("%.0f\u2103",day.getDailyDayTemp()));
            holder.dailyEveTemp.setText(String.format("%.0f\u2103",day.getDailyEveTemp()));
            holder.dailyNightTemp.setText(String.format("%.0f\u2103",day.getDailyNightTemp()));

        }
        int iconResId = dfAct.getResources().getIdentifier(day.getDaily_weather_icon(),
                "drawable", dfAct.getPackageName());
        holder.dailyWeatherIcon.setImageResource(iconResId);
        holder.dailyDesc.setText(String.format("%s",day.getDailyDesc()));
        holder.dailyPop.setText(String.format("(%d%% precip.)",day.getDailyPop()));
        holder.dailyUV.setText(String.format("UV Index: %.0f",day.getDailyUVI()));


    }

    @Override
    public int getItemCount() {
        return dailyForecastList.size();
    }
}
