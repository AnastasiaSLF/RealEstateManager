package com.example.realestatemanager.providers;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.realestatemanager.ui.LocationPermission;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.function.Consumer;

import javax.inject.Inject;

// TODO Refactoring
public class
LocationProvider {

    private final FusedLocationProviderClient fusedLocationProviderClient;
    private final LocationPermission locationPermission;
    private Location lastKnownLocation;
    private Consumer<Location> onResultListener;


    @Inject
    public LocationProvider(
            FusedLocationProviderClient fusedLocationProviderClient, LocationPermission locationPermission) {
        this.locationPermission = locationPermission;
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        warmUpTheLocationProvider();
    }

    @SuppressLint("MissingPermission")
    public void getCurrentCoordinates(Consumer<Location> listener) {
        if (locationPermission.hasPermission()) {
            fusedLocationProviderClient
                    .getLastLocation()
                    .addOnCompleteListener(getOnCompleteListener(listener));
        }else{
            onResultListener = listener;
        }
    }

    private OnCompleteListener<Location> getOnCompleteListener(Consumer<Location> listener) {
        return task -> {
            final Location location = task.getResult();
            if (task.isSuccessful() && location != null) {
                listener.accept(location);
                lastKnownLocation = location;
            } else if (lastKnownLocation != null) {
                listener.accept(lastKnownLocation);
            } else onResultListener = listener;
        };
    }


    @SuppressLint("MissingPermission")
    private void warmUpTheLocationProvider() {
        final LocationRequest locationRequest =
                LocationRequest.create()
                        .setInterval(100)
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (locationPermission.hasPermission()) {
            fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest,
                    new LocationCallback() {
                        @Override
                        public void onLocationResult(@NonNull LocationResult locationResult) {
                            super.onLocationResult(locationResult);
                            lastKnownLocation = locationResult.getLastLocation();
                            if (onResultListener != null) {
                                onResultListener.accept(lastKnownLocation);
                            }
                            fusedLocationProviderClient.removeLocationUpdates(this);
                        }
                    },
                    Looper.getMainLooper());
        } else locationPermission.requestPermission(this::warmUpTheLocationProvider);
    }

}
