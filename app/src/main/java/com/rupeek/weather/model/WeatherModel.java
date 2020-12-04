package com.rupeek.weather.model;

public class WeatherModel {
    /*{
        "temp":24,
            "time":1564012800,
            "rain":40,
            "wind":15
    },*/
    private String temp;
    private String time;
    private String rain;
    private String wind;

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getWind() {
        return wind;
    }

    public void setWind(String wind) {
        this.wind = wind;
    }

    @Override
    public String toString() {
        return "WeatherModel{" +
                "temp='" + temp + '\'' +
                ", time='" + time + '\'' +
                ", rain='" + rain + '\'' +
                ", wind='" + wind + '\'' +
                '}';
    }
}
