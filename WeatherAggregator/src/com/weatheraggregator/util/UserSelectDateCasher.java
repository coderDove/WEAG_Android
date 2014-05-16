package com.weatheraggregator.util;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class UserSelectDateCasher {

    private HashMap<String, Long> userSelectDate;

    private volatile static UserSelectDateCasher sSingleton;

    public synchronized static UserSelectDateCasher getSingleton() {
	if (sSingleton == null) {
	    sSingleton = new UserSelectDateCasher();
	}
	return sSingleton;
    }

    private UserSelectDateCasher() {
	userSelectDate = new HashMap<String, Long>();
    }

    public synchronized void putUserSelectDate(String CityId, long date) {
	userSelectDate.put(CityId, Long.valueOf(date));
    }

    public synchronized Long getDate(String CityId) {
	return userSelectDate.get(CityId);
    }

    public synchronized void clear() {
	if (userSelectDate != null) {
	    userSelectDate.clear();
	}
    }
}
