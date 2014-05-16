package com.weatheraggregator.webservice;

public class URLConstant {
    public static final String SERVER_WORK = "http://kremen.eleks.com:8585/api/";//"http://172.20.1.208:80/api/";
    public static final String SERVER_HOST = "http://kremen.eleks.com:8585";//"http://172.20.1.208";//"172.20.1.208"; // "172.20.1.208:3285";
    // public static final String SERVER_WORK
    // ="http://193.109.249.203:8585/api";
    // public static final String SERVER_WORK = "http://172.20.1.208:82/api/";
    // private static final String SERVER_WORK =
    // "http://172.20.1.208:3285/api/";

    // test server
    // private static final String SERVER_WORK = "http://172.20.1.208:82/api/";

    public static final String URL_CITIES = SERVER_WORK + "UserCity?userId=%s";

    public static final String URL_SEARCH_CITY = SERVER_WORK
	    + "Searcher?cityName=%s&lang=%s";
    public static final String URL_CITY_BY_LOCATION = SERVER_WORK
	    + "Searcher?lat=%s&lon=%s";
    public static final String URL_CITY_WITH_FARECASTS_BY_LOCATION = SERVER_WORK
	    + "Searcher?lat=%s&lon=%s&userId=%s";

    public static final String URL_SEVICES = SERVER_WORK
	    + "UserServices?userId=%s";

    public static final String URL_REGISTER_USER = SERVER_WORK + "User";

    public static final String URL_FORECAST = SERVER_WORK
	    + "Forecast?cityId=%s&serviceId=%s&uDateTime=%d";

    public static final String URL_FORECAST_BY_PERIOD = SERVER_WORK
	    + "Forecast?uStartDate=%d&uEndDate=%d&userId=%s";
    public static final String URL_ACTUAL_FORECAST_DATA = SERVER_WORK
	    + "Forecast?userId=%s&uDateTime=%d";

    public static final String URL_FORECAST_BY_DATE = SERVER_WORK
	    + "Forecast?userId=%s&uDateTime=%d";

    public static final String URL_RATING_SERVICE = SERVER_WORK
	    + "UserServices?serviceId=%s&vote=%s";

    public static final String URL_DETAIL_FORECAST = SERVER_WORK
	    + "DetailForecast?cityId=%s&serviceId=%s&uDateTime=%d";

}
