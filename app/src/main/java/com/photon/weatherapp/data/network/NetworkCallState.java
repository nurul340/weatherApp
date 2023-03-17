package com.photon.weatherapp.data.network;

public class NetworkCallState<T> {

    public final boolean isLoading;
    public final T data;

    public final boolean isError;
    public final String errorMessage;

    public NetworkCallState(boolean isLoading, T data, boolean isError, String errorMessage) {
        this.isLoading = isLoading;
        this.data = data;
        this.isError = isError;
        this.errorMessage = errorMessage;
    }

}
