package com.weatheraggregator.localservice;
import com.weatheraggregator.entity.Forecast;

interface IDetailDataSourceServiceListener
{
   void callBack(int statusCode, in List<Forecast> detailForecast);
}