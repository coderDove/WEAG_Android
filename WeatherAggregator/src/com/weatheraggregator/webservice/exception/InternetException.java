package com.weatheraggregator.webservice.exception;

public class InternetException extends Exception {

    private static final long serialVersionUID = -2175544474279249144L;
    private Exception mEx;
    private ErrorType mErrorStatus;

    public InternetException(Exception ex) {
	super();
	this.mEx = ex;
    }

    public InternetException(Exception ex, ErrorType errorStatus) {
	super();
	this.mEx = ex;
	this.mErrorStatus = errorStatus;
    }
    
    public InternetException(ErrorType errorStatus) {
	super();
	this.mErrorStatus = errorStatus;
    }

    public ErrorType getErrorStatus() {
	return mErrorStatus;
    }

    public String getMessage() {
	if (mEx != null && mEx.getMessage() != null) {
	    return mEx.getMessage();
	}
	return "";
    }

    public String getTypeErrorType() {
	return "InternetExeption";
    }
}
