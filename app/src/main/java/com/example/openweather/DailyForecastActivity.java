package com.example.openweather;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DailyForecastActivity extends AppCompatActivity {

    private DailyForecastAdapter dAdapter;
    private RecyclerView dailyRecycler;
    private final List<DailyForecast> dailyForecastList = new ArrayList<>();
    private boolean fahrenheit;
    private String locale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forecast_activity);
        Intent intent = getIntent();

        if (intent.hasExtra("DAILY")) {
            dailyForecastList.addAll((List<DailyForecast>) intent.getSerializableExtra("DAILY"));
            fahrenheit = intent.getBooleanExtra("FAHRENHEIT", false);
            locale = intent.getStringExtra("LOCALE");
        }

        dailyRecycler = findViewById(R.id.dailyRecycler);
        dAdapter = new DailyForecastAdapter(dailyForecastList, fahrenheit, this);
        dailyRecycler.setAdapter(dAdapter);
        dailyRecycler.setLayoutManager(new LinearLayoutManager(this));
        setTitle(locale);
    }
}
