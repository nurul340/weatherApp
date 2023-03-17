package com.photon.weatherapp.di;
import static com.photon.weatherapp.utils.Constants.BASE_URL;

import android.content.Context;

import com.photon.weatherapp.data.network.ApiInterface;
import com.photon.weatherapp.data.roomdb.WeatherDao;
import com.photon.weatherapp.data.roomdb.WeatherDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@InstallIn(SingletonComponent.class)
@Module
public class AppModule {

    @Singleton
    @Provides
    public Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public ApiInterface getApiInterface(Retrofit retrofitClient){
        return retrofitClient.create(ApiInterface.class);
    }


    @Singleton
    @Provides
    public WeatherDao getDao(@ApplicationContext Context context){
        return WeatherDatabase.getInstance(context).weatherDao();
    }

}
