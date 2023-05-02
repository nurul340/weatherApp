package com.photon.weatherapp.views;

import static com.photon.weatherapp.utils.Constants.WEATHER_TYPE_METRIC;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.photon.weatherapp.data.network.NetworkCallState;
import com.photon.weatherapp.data.repo.WeatherInfoRepository;
import com.photon.weatherapp.data.model.wether_details.WeatherResponse;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;


@HiltViewModel
public class WeatherInfoViewModel extends ViewModel{

    private CompositeDisposable compositeDisposable;

    private final WeatherInfoRepository weatherInfoRepository;
    private final MutableLiveData<NetworkCallState<WeatherResponse>> weatherInfoCallState = new MutableLiveData<>();


    @Inject
    public WeatherInfoViewModel(WeatherInfoRepository weatherInfoRepository){
        this.weatherInfoRepository = weatherInfoRepository;
        this.compositeDisposable = new CompositeDisposable();
    }

    public LiveData<NetworkCallState<WeatherResponse>> getWeatherInfoCallState(){
        return weatherInfoCallState;
    }

    // fetch weather report by lat long
    public void fetchWeatherInfoByLatLong(double latitude, double longitude) {
        fetchWeatherInfo(weatherInfoRepository.retrieveWeatherInfoByLatLong(latitude, longitude, WEATHER_TYPE_METRIC));
    }

    // fetch weather report by city name
    public void fetchWeatherInfoByCity(String cityName){
        fetchWeatherInfo(weatherInfoRepository.retrieveWeatherInfoByCity(cityName, WEATHER_TYPE_METRIC));
    }

    private void fetchWeatherInfo(Single<WeatherResponse> single){

        weatherInfoCallState.postValue(new NetworkCallState<>(true, null, false, null));

        Disposable disposable = single.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        if(weatherResponse != null) {
                            weatherInfoCallState.postValue(new NetworkCallState<>(false, weatherResponse, false,null));

                            //delete last saved data
                            deleteSavedWeatherInfo();
                            //insert new data
                            saveWeatherInfo(weatherResponse);
                        }
                        else {
                            weatherInfoCallState.postValue(new NetworkCallState<>(false, null, true,"Empty response"));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        weatherInfoCallState.postValue(new NetworkCallState<>(false, null, true, e.getMessage()));
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void fetchSavedWeatherInfo(){
        weatherInfoCallState.postValue(new NetworkCallState<>(true, null, false, null));
        Disposable disposable = weatherInfoRepository.getLastSavedWeatherInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<WeatherResponse>() {
                    @Override
                    public void onSuccess(WeatherResponse weatherResponse) {
                        if(weatherResponse != null) {
                            weatherInfoCallState.postValue(new NetworkCallState<>(false, weatherResponse, false,null));
                        }
                        else {
                            //No need to show error
                            weatherInfoCallState.postValue(new NetworkCallState<>(false, null, false,null));
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //No need to show error
                        weatherInfoCallState.postValue(new NetworkCallState<>(false, null, false, null));
                    }
                });
        compositeDisposable.add(disposable);
    }

    private void saveWeatherInfo(WeatherResponse weatherResponse){
        Disposable disposable = weatherInfoRepository.saveWeatherInfo(weatherResponse)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    private void deleteSavedWeatherInfo(){
        Disposable disposable = weatherInfoRepository.deleteSavedWeatherInfo()
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable.clear();
            compositeDisposable = null;
        }
    }
}
