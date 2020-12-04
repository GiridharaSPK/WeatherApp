package com.rupeek.weather.api;

import com.rupeek.weather.model.request.WeatherRequest;
import com.rupeek.weather.model.response.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

interface ApiService {
    @POST("v2/5d3a99ed2f0000bac16ec13a")
    Call<WeatherResponse> getWeatherData(@Body WeatherRequest request);

    @POST("sde2")
    Call<WeatherResponse> getWeatherDataFromSecondaryApi(@Body WeatherRequest request);
}
