package com.weatheraggregator.events;

public class EventSettingSearch {
    private String searchValue;

    public EventSettingSearch(String searchValue) {
        this.searchValue = searchValue;
    }

    public String getSearchValue() {
        return searchValue;
    }

    public void setSearchValue(String searchValue) {
        this.searchValue = searchValue;
    }

}
