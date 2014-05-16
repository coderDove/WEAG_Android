package com.weatheraggregator.model;

import java.util.Date;

public class ForecastInfoKey {
    private final String cityId;
    private final String serviceId;
    private final Date forecastDate;

    public ForecastInfoKey(String cityId, String serviceId, Date date) {
        this.cityId = cityId;
        this.serviceId = serviceId;
        this.forecastDate = date;
    }

    public String getCityId() {
        return cityId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public String getCityName() {
        return cityId;
    }

    public Date getForecastDate() {
        return forecastDate;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((cityId == null) ? 0 : cityId.hashCode());
        result = prime * result + ((forecastDate == null) ? 0 : forecastDate.hashCode());
        result = prime * result + ((serviceId == null) ? 0 : serviceId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ForecastInfoKey other = (ForecastInfoKey) obj;
        if (cityId == null) {
            if (other.cityId != null)
                return false;
        } else if (!cityId.equals(other.cityId))
            return false;
        if (forecastDate == null) {
            if (other.forecastDate != null)
                return false;
        } else if (!forecastDate.equals(other.forecastDate))
            return false;
        if (serviceId == null) {
            if (other.serviceId != null)
                return false;
        } else if (!serviceId.equals(other.serviceId))
            return false;
        return true;
    }
}
