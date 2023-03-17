package com.photon.weatherapp.data.network;

import static com.photon.weatherapp.utils.Constants.END_POINT;

import com.photon.weatherapp.data.wether_details.WeatherResponse;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET(END_POINT)
    Single<WeatherResponse> getWeatherInfoByLatLong(
            @Query("lat") double lat,
            @Query("lon") double lon,
            @Query("appid") String apiKey,
            @Query("units") String unit
    );

    @GET(END_POINT)
    Single<WeatherResponse> getWeatherInfoByCity(
            @Query("q") String cityName,
            @Query("appid") String apiKey,
            @Query("units") String unit
    );
}
