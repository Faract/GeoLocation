package com.example.geolocationbasis;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button showMapButton;
    TextView latText, lonText, timeText;

    LocationManager locationManager;
    Location location;

    private boolean granted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapButton   =   findViewById(R.id.toMapButton);
        lonText         =   findViewById(R.id.lon);
        latText         =   findViewById(R.id.lat);
        timeText        =   findViewById(R.id.timeText);

        //TODO подключить менеджер местоположения
    }

    //TODO описать LocationListener
    LocationListener listener;

    @Override
    protected void onResume() {
        super.onResume();
        //TODO реализовать получение координат с запросом разрешения

    }

    //TODO переопределить функцию обратного вызова для обработки ответа пользователя
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(listener);
    }
}
