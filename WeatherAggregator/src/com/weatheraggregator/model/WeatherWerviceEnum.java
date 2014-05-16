package com.weatheraggregator.model;

public enum WeatherWerviceEnum {
    ZUPR(0), YANDEX(1), YAHOO(2), HAMWEATHER(3), WEATHERUA(4);
    private int code;

    public int getCode() {
	return code;
    }

   private WeatherWerviceEnum(int code) {
	this.code = code;
    }

}
