package com.weatheraggregator.util;


public class ConstantQuery {
    // don't rename this fields
    public static final String F_SERVICE_NAME = "SName";
    public static final String F_CITY_NAME = "CName";
    public static String Q_WEATHER_SERVICE_COUNT = "select count() as count from WeatherService ";
    //private static final String WHERE_SERVICE_RATING = "%s='%s' and %s='%s' and %s BETWEEN %d and %d";
    //private static final String WHERE_FORECAST = "%s = '%s' and %s BETWEEN %d and %d";

    public static String Q_FORECAST_FOR_CASHE = "select * from Forecast as fr\n join \n (\n select [service_id],[city_id], max([requert_day]) as [requert_day],[forecast_day]\n from Forecast\n where city_id = ? and service_id = ? and forecast_day between ? and ?  group by [service_id],[city_id],[forecast_day]\n )as fdate on fdate.city_id = fr.city_id and fdate.service_id = fr.service_id and fdate.[requert_day] = fr.[requert_day] and fdate.[forecast_day] = fr.[forecast_day]\norder by fr.forecast_day";

    /**
     * select WeatherService .Name as ServiceName, ff.* from WeatherService left
     * join ( select city.Name as CityName, f.* from Forecast as f join ( select
     * fr.[service_id],fr.[city_id],fr.[forecast_day],max(fr.[requert_day]) as
     * [requert_day] from Forecast as fr join ( select
     * [service_id],[city_id],min([forecast_day]) as [forecast_day] from
     * Forecast where city_id = city_id and forecast_day >=@date group by
     * [service_id],[city_id] )as fdate on fdate.city_id = fr.city_id and
     * fdate.[service_id] = fr.[service_id] and fdate.[forecast_day] =
     * fr.[forecast_day] group by fr.[service_id],fr.[city_id],fr.[forecast_day]
     * )as fjoin on fjoin.city_id = f.city_id and fjoin.forecast_day =
     * f.forecast_day and fjoin.requert_day = f.requert_day and fjoin.service_id
     * = f.service_id left join City on City._object_id = f.city_id )as ff on
     * WeatherService._object_id = ff.service_id
     */

    // private static final String FORECAST_FIELDS =
    // "ff._object_id as _object_id, \nff.city_id as city_id,\nff.cloudy as cloudy,\nff._id as _id,\nff.condition as condition,\nff.forecast_day as forecast_day,\nff.humidity as humidity,\nff.is_current as is_current,\nff.max_pressure as max_pressure,\nff.max_temp as max_temp,\nff.max_wind_speed as max_wind_speed,\nff.min_pressure as min_pressure,\nff.min_temp as min_temp,\nff.min_wind_speed as min_wind_speed,\nff.precipitation as precipitation,\nff.pressure as pressure, \nff.requert_day as requert_day, \nff.revision as revision, \nff.service_id as service_id, \nff.sunrise as sunrise, \nff.sunrise as sunrise, \nff.[temp] as [temp],\nff.wind_direction as wind_direction, \nff.wind_speed as wind_speed\n";
    // private static final String Q_GET_FORECAST =
    // "select WeatherService .Name as SName, WeatherService.rating as rating, "
    // + FORECAST_FIELDS +
    // " \n\nfrom WeatherService \nleft join\n(\n select city.Name, f.* from Forecast as f\n join\n (\n select fr.[service_id],fr.[city_id],fr.[forecast_day],max(fr.[requert_day]) as [requert_day] from Forecast as fr\n join \n (\n select [service_id],[city_id],min([forecast_day]) as [forecast_day]\n \n from Forecast\n where \n city_id = \'%s\'   and forecast_day >=%d  and  \n forecast_day between %d and %d \n group by [service_id], [city_id]\n )as \n fdate on \n fdate.city_id = fr.city_id and\n fdate.[service_id] = fr.[service_id] and \n fdate.[forecast_day] = fr.[forecast_day]\n group by \n fr.[service_id],fr.[city_id],fr.[forecast_day]\n )as \n fjoin on \n fjoin.city_id = f.city_id and\n fjoin.forecast_day = f.forecast_day and\n fjoin.requert_day = f.requert_day and \n fjoin.service_id = f.service_id\n left join City on City._object_id = f.city_id\n)as ff on \n WeatherService._object_id = ff.service_id \n where \n WeatherService.is_favorite = 0 and \n WeatherService.is_delete = 0";
    // private static final String Q_GET_ACTUAL_FORECAST_BY_SERVICE =
    // "select WeatherService .Name as SName, WeatherService.rating as rating, "
    // + FORECAST_FIELDS +
    // " from WeatherService \nleft join\n(\n select city.Name, f.* from Forecast as f\n join\n (\n select fr.[service_id],fr.[city_id],fr.[forecast_day],max(fr.[requert_day]) as [requert_day] from Forecast as fr\n join \n (\n select [service_id],[city_id],min([forecast_day]) as [forecast_day]\n from Forecast\n where city_id = \'%s\' and forecast_day >=%d and service_id = \'%s\' \n group by [service_id],[city_id]\n )as fdate on fdate.city_id = fr.city_id and fdate.[service_id] = fr.[service_id] and fdate.[forecast_day] = fr.[forecast_day]\n group by fr.[service_id],fr.[city_id],fr.[forecast_day]\n )as fjoin on fjoin.city_id = f.city_id and fjoin.forecast_day = f.forecast_day and fjoin.requert_day = f.requert_day and fjoin.service_id = f.service_id\n left join City on City._object_id = f.city_id\n)as ff on WeatherService._object_id = ff.service_id where  WeatherService.is_delete = 0";

    // public static String getServiceRatingWhere(City city, WeatherService
    // service, Date date)
    // {
    // String where = "";
    // if (city != null && service != null && date != null)
    // {
    // where = String.format(WHERE_SERVICE_RATING, ServiceRating.F_CITY_ID,
    // city.getObjectId(), ServiceRating.F_SERVICE_ID, service.getObjectId(),
    // ServiceRating.F_DATE, DateHelper.getStartDateByDate(date),
    // DateHelper.getEndDateByDate(date));
    // }
    // return where;
    // }

    // public static String getForecastWhere1(final City city, final Date date)
    // {
    // String where = "";
    // if (city != null && date != null)
    // {
    // where = String.format(WHERE_FORECAST, Forecast.F_CITY_ID,
    // city.getObjectId(), Forecast.F_FORECAST_DAY,
    // DateHelper.getStartDateByDate(date), DateHelper.getEndDateByDate(date));
    // }
    // return where;
    // }
}
