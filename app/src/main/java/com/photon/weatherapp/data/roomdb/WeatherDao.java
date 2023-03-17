package com.photon.weatherapp.data.roomdb;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.photon.weatherapp.data.wether_details.WeatherResponse;

import io.reactivex.Completable;
import io.reactivex.Single;


@Dao
public interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertWeatherInfo(WeatherResponse weatherResponse);

    @Query("DELETE FROM weather_info")
    Completable deleteWeatherInfo();

    @Query("SELECT * FROM weather_info")
    Single<WeatherResponse> fetchedLastWeatherInfo();


}
