package com.weatheraggregator.webservice.exception;

public enum ErrorType {
    HTTP_OK(0), NO_INTERNET_CONNECTION(1), HTTP_HOST_NOT_EVAILABLE(2), UNKNOW_ERROR(
	    3), INCORRECT_DATA(4),USER_NOT_AUHTORISED(5);

    private int code;

    ErrorType(int code) {
	this.code = code;
    }

    public int getCode() {
	return code;
    }
}
