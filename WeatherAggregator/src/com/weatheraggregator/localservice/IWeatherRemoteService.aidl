package com.weatheraggregator.localservice;

import com.weatheraggregator.localservice.IDataSourceServiceListener;
import com.weatheraggregator.entity.City;
import com.weatheraggregator.localservice.IDetailDataSourceServiceListener;
import com.weatheraggregator.entity.WeatherService;

interface IWeatherRemoteService 
{
   void addFavouriteCity(in City city,IDataSourceServiceListener callBack);
   void removeFavouriteCity(in String cityId, IDataSourceServiceListener callBack);
   void reorderCity(IDataSourceServiceListener callBack);
   void reorderWeatherService(IDataSourceServiceListener callBack);
   void userRegistration(IDataSourceServiceListener callBack);
   void loadWeatherService(IDataSourceServiceListener callBack);
   void loadUserCity(IDataSourceServiceListener callBack);
   void updateWeatherService(IDataSourceServiceListener callBack);

   void loadForecastByPeriod(long uStartDate, long uEndDate,IDataSourceServiceListener callBack);
   void loadActualForecast(IDataSourceServiceListener callBack, long uDate);   
   void loadForecast(String cityId, String serviceId, long uDate,IDataSourceServiceListener callBack);
   void loadForecastByDate(long uDate, IDataSourceServiceListener callBack);
   void setFavouriteWeatherService(String newServiceId, String oldServiceId,IDataSourceServiceListener callBack);
   void updateUser(IDataSourceServiceListener callBack);
   void synUserData(IDataSourceServiceListener callBack);
   void loadUserCityWithForecast(IDataSourceServiceListener callBack);
   void sendServiceRating(long serviceRatingId, IDataSourceServiceListener callBack);
   void loadCityByLocation(double lat, double lon,IDataSourceServiceListener callBack);
   void loadCityWithForecastsByLocation(double lat, double lon, IDataSourceServiceListener callBack);
   void registerUserWithLoadingData(IDataSourceServiceListener callBack);
   void loadDetailForecast(String cityId,String ServiceId, long uDate, IDetailDataSourceServiceListener callBack);
}