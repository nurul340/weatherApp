package com.photon.weatherapp.data;

import com.google.gson.annotations.SerializedName;

public class GeocodingDetails {

    @SerializedName("name")
    private String cityName;

    @SerializedName("local_names")
    private LocaleName localeName;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("lon")
    private String longitude;

    @SerializedName("country")
    private String country;

    @SerializedName("state")
    private String state;


    static class LocaleName{
        @SerializedName("bn")
        private String bengaliName;

    }

}
