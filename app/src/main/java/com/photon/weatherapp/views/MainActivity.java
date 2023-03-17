package com.photon.weatherapp.views;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.photon.weatherapp.utils.Constants.REQUEST_CODE_LOCATION_PERMISSION;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;

import com.photon.weatherapp.databinding.ActivityMainBinding;
import com.photon.weatherapp.data.model.LocationLiveData;
import com.photon.weatherapp.utils.Constants;
import com.photon.weatherapp.utils.GpsUtils;
import com.photon.weatherapp.utils.PermissionUtils;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import pub.devrel.easypermissions.EasyPermissions;


@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private WeatherInfoViewModel weatherInfoViewModel;
    private ActivityMainBinding binding;

    @Inject
    public LocationLiveData locationLiveData;

    @Inject
    public GpsUtils gpsUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        weatherInfoViewModel = new ViewModelProvider(this).get(WeatherInfoViewModel.class);
        binding.setViewModel(weatherInfoViewModel);
        binding.setLifecycleOwner(this);

        checkLocationPermission();

        observeViewModelDataUpdate();
    }


    public void onSearchButtonClicked(View view) {
        //fetch weather data by location name
        if(binding.etLocation.getText().toString().isEmpty()) return;
        weatherInfoViewModel.fetchWeatherInfoByCity(binding.etLocation.getText().toString());
    }

    private void observeViewModelDataUpdate() {
        weatherInfoViewModel.getWeatherInfoCallState().observe(this, weatherResponseNetworkCallState -> {

            //loading state
            if(weatherResponseNetworkCallState.isLoading){
                if(binding.cvData.getVisibility() != View.VISIBLE){
                    binding.cvData.setVisibility(View.VISIBLE);
                }
                binding.layoutData.setVisibility(View.GONE);
                binding.layoutShimmer.startShimmer();
            }else {
                binding.layoutShimmer.stopShimmer();
                binding.tvError.setVisibility(View.GONE);
            }

            //error state
            if(weatherResponseNetworkCallState.isError){
                binding.layoutData.setVisibility(View.GONE);
            }

            //data received
            if(weatherResponseNetworkCallState.data != null){
                binding.layoutData.setVisibility(View.VISIBLE);
            }

        });
    }

    private void observeLocationDataUpdate(){
        if(locationLiveData == null) return;

        locationLiveData.observe(this, location ->
                weatherInfoViewModel.fetchWeatherInfoByLatLong(location.getLatitude(), location.getLatitude()));
    }


    @SuppressWarnings("MissingPermission") //suppressing since already checking
    private void checkLocationPermission() {
        if(PermissionUtils.hasLocationPermission(this)){
            if(isLocationEnabled()){
                observeLocationDataUpdate();
            }else {
                gpsUtils.turnGPSOn(gpsListener);
            }
            return;
        }
        EasyPermissions.requestPermissions(
                this,
                "Please allow location permission",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                ACCESS_FINE_LOCATION
        );

    }

    private boolean isLocationEnabled(){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager != null){
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }
        return false;
    }


    @Override
    @SuppressWarnings("all") //supressing since its onPermissionsGranted
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        if(isLocationEnabled()){
            observeLocationDataUpdate();
        }
        else {
            gpsUtils.turnGPSOn(gpsListener);
        }
    }


    private final GpsUtils.onGpsListener gpsListener = isGPSEnable -> {
        if(isGPSEnable) {
            // observe for location coordinates & fetch latest weather info
            observeLocationDataUpdate();
        }
        else {
            // fetch last saved weather info
            weatherInfoViewModel.fetchSavedWeatherInfo();
        }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //redirecting to easy permission framework
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.REQUEST_ENABLE_GPS) {
                observeLocationDataUpdate();
            }
            else {
                weatherInfoViewModel.fetchSavedWeatherInfo();
            }
        }
        else {
            weatherInfoViewModel.fetchSavedWeatherInfo();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        // No need to handle as location access is optional.
        // fetch last saved weather info.
        weatherInfoViewModel.fetchSavedWeatherInfo();
    }

}