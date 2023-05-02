package com.photon.weatherapp.data.roomdb;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.photon.weatherapp.data.repo.RoomDataConverter;
import com.photon.weatherapp.data.model.wether_details.WeatherResponse;

@Database(entities = {WeatherResponse.class}, version = 1,exportSchema = false)

@TypeConverters(RoomDataConverter.class)
abstract public class WeatherDatabase extends RoomDatabase {

    private static WeatherDatabase instance;

    public abstract WeatherDao weatherDao();

    public static synchronized WeatherDatabase getInstance(Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            WeatherDatabase.class ,
                            "weather_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
