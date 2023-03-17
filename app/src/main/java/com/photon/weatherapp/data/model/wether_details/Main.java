package com.photon.weatherapp.data.model.wether_details;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.photon.weatherapp.BR;

public class Main extends BaseObservable {

    @SerializedName("temp")
    @Expose
    private Double temp;

    @SerializedName("feels_like")
    @Expose
    private Double feelsLike;

    @SerializedName("temp_min")
    @Expose
    private Double tempMin;

    @SerializedName("temp_max")
    @Expose
    private Double tempMax;

    @SerializedName("pressure")
    @Expose
    private Integer pressure;

    @SerializedName("humidity")
    @Expose
    private Integer humidity;

    @SerializedName("sea_level")
    @Expose
    private Integer seaLevel;

    @SerializedName("grnd_level")
    @Expose
    private Integer grndLevel;

    @Bindable
    public String getTemp() {
        String s = String.valueOf(temp);
        return s.substring(0, s.indexOf(".")) + "\u00B0C";
    }

    @Bindable
    public void setTemp(Double temp)     {
        this.temp = temp;
        notifyPropertyChanged(BR.temp);

    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Double getTempMin() {
        return tempMin;
    }

    public void setTempMin(Double tempMin) {
        this.tempMin = tempMin;
    }

    public Double getTempMax() {
        return tempMax;
    }

    public void setTempMax(Double tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Integer getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(Integer grndLevel) {
        this.grndLevel = grndLevel;
    }
}
