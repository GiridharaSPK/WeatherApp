package com.rupeek.weather.model.response;

import com.rupeek.weather.model.WeatherModel;

import java.util.ArrayList;

public class WeatherResponse {
    private ArrayList<WeatherModel> data;

    public ArrayList<WeatherModel> getData() {
        return data;
    }

    public void setData(ArrayList<WeatherModel> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "WeatherResponse{" +
                "data=" + data +
                '}';
    }
}
