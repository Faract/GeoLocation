package com.example.geolocationbasis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.security.Permission;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener {
    private static final int REQUEST_LOCATION = 1;

    Button showMapButton;
    TextView latText, lonText, timeText;

    LocationManager locationManager;
    Location location;

    GoogleMap map;
    Marker marker;

    private boolean granted = false;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showMapButton = findViewById(R.id.toMapButton);
        lonText = findViewById(R.id.lon);
        latText = findViewById(R.id.lat);
        timeText = findViewById(R.id.timeText);

        showMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (map != null && location != null)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                            location.getLongitude()), 18));
            }
        });

        SupportMapFragment map = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);
        map.getMapAsync(this);

        if (isPermissionsGranted()) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 20, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isPermissionsGranted()) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
    }

    private boolean isPermissionsGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION) {
            boolean isAllGranted = true;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 10, 20, this);
            }
        }
    }

    //TODO переопределить функцию обратного вызова для обработки ответа пользователя
    @Override
    protected void onPause() {
        super.onPause();
        if (locationManager != null) locationManager.removeUpdates(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (location != null) {
                    Location location2 = new Location("");
                    location2.setLatitude(latLng.latitude);
                    location2.setLongitude(latLng.longitude);
                    Toast.makeText(MainActivity.this, "Расстояние от текущего местоположения до " +
                            "выбранной точки равно " + location.distanceTo(location2) + " метров.", Toast.LENGTH_SHORT).show();
                }
                System.out.println(latLng.toString());
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

        latText.setText(Double.toString(location.getLatitude()));
        lonText.setText(Double.toString(location.getLongitude()));
        if (map != null) {
            if (marker != null) marker.remove();

            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(location.getLatitude(), location.getLongitude()));
            markerOptions.title("Ваше местоположение");
            marker = map.addMarker(markerOptions);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
