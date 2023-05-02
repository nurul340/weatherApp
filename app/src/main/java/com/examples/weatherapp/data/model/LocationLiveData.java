package com.photon.weatherapp.data.model;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import javax.inject.Inject;

import dagger.hilt.android.qualifiers.ActivityContext;


public class LocationLiveData extends MutableLiveData<Location> {

    public final static long INTERVAL = 10000L;
    private final static long MINIMUM_UPDATE_INTERVAL = 5000L;

    private final FusedLocationProviderClient fusedLocationProviderClient;

    @Inject
    public LocationLiveData(@ActivityContext Context context){
        fusedLocationProviderClient
                = LocationServices.getFusedLocationProviderClient(context);
    }

    private void setLocationData(Location location) {
         setValue(location);
    }


    @Override
    protected void onInactive() {
        super.onInactive();
        if(fusedLocationProviderClient == null) return;
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }


    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION})
    protected void onActive() {
        super.onActive();
        if(fusedLocationProviderClient == null) return;
        fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(location -> {
                if(location != null){
                    setLocationData(location);
                }
                else {
                    startLocationUpdates();
                }
            });
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);
            setLocationData(locationResult.getLastLocation());
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }
    };


    @RequiresPermission(allOf = {ACCESS_FINE_LOCATION, ACCESS_FINE_LOCATION})
    protected void startLocationUpdates() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_BALANCED_POWER_ACCURACY, INTERVAL)
                .setMinUpdateIntervalMillis(MINIMUM_UPDATE_INTERVAL)
                .build();
        fusedLocationProviderClient.requestLocationUpdates(
                locationRequest ,
                locationCallback,
                null
        );
    }


}
