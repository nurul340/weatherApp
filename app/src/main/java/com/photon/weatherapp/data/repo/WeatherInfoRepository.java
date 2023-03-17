package com.photon.weatherapp.data.repo;

import com.photon.weatherapp.BuildConfig;
import com.photon.weatherapp.data.network.ApiInterface;
import com.photon.weatherapp.data.roomdb.WeatherDao;
import com.photon.weatherapp.data.model.wether_details.WeatherResponse;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Single;


public class WeatherInfoRepository {

    private final ApiInterface apiInterface;
    private final WeatherDao weatherDao;

    @Inject
    public WeatherInfoRepository(
            ApiInterface apiInterface,
            WeatherDao weatherDao
    ){
        this.apiInterface = apiInterface;
        this.weatherDao = weatherDao;
    }

    public Single<WeatherResponse> retrieveWeatherInfoByLatLong(
            double latitude,
            double longitude,
            String unit
    ){

        return apiInterface.getWeatherInfoByLatLong(latitude, longitude, BuildConfig.OPEN_WETHER_API_KEY, unit);
    }

    public Single<WeatherResponse> retrieveWeatherInfoByCity(
            String cityName,
            String unit
    ){

        return apiInterface.getWeatherInfoByCity(cityName,  BuildConfig.OPEN_WETHER_API_KEY, unit);
    }

    public Single<WeatherResponse> getLastSavedWeatherInfo(){
        return weatherDao.fetchedLastWeatherInfo();
    }

    public Completable saveWeatherInfo(WeatherResponse weatherResponse){
        return weatherDao.insertWeatherInfo(weatherResponse);
    }

    public Completable deleteSavedWeatherInfo(){
        return weatherDao.deleteWeatherInfo();
    }

}
