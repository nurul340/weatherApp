package com.photon.weatherapp.data.repo;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.photon.weatherapp.data.model.wether_details.Clouds;
import com.photon.weatherapp.data.model.wether_details.Coord;
import com.photon.weatherapp.data.model.wether_details.Main;
import com.photon.weatherapp.data.model.wether_details.Sys;
import com.photon.weatherapp.data.model.wether_details.Weather;
import com.photon.weatherapp.data.model.wether_details.Wind;

import java.util.List;

public class RoomDataConverter {

    @TypeConverter
    public String listToString(List<Weather> dataList){
        return new Gson().toJson(dataList);
    }

    @TypeConverter
    public List<Weather> stringToJson(String data){
        return new Gson().fromJson(data, new TypeToken<List<Weather>>(){}.getType());
    }


    @TypeConverter
    public String coordToString(Coord coord){
        return new Gson().toJson(coord);
    }

    @TypeConverter
    public Coord stringToCoord(String data){
        return new Gson().fromJson(data, new TypeToken<Coord>(){}.getType());
    }

    @TypeConverter
    public String MainToString(Main main){
        return new Gson().toJson(main);
    }

    @TypeConverter
    public Main stringToMain(String data){
        return new Gson().fromJson(data, new TypeToken<Main>(){}.getType());
    }

    @TypeConverter
    public String WindToString(Wind wind){
        return new Gson().toJson(wind);
    }

    @TypeConverter
    public Wind stringToWind(String data){
        return new Gson().fromJson(data, new TypeToken<Wind>(){}.getType());
    }


    @TypeConverter
    public String WindToString(Clouds clouds){
        return new Gson().toJson(clouds);
    }

    @TypeConverter
    public Clouds stringToCloud(String data){
        return new Gson().fromJson(data, new TypeToken<Clouds>(){}.getType());
    }


    @TypeConverter
    public String SysToString(Sys data){
        return new Gson().toJson(data);
    }

    @TypeConverter
    public Sys stringToSys(String data){
        return new Gson().fromJson(data, new TypeToken<Sys>(){}.getType());
    }



}
