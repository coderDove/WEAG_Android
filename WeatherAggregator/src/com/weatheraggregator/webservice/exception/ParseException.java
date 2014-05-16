package com.weatheraggregator.webservice.exception;

public class ParseException extends Exception {
    private static final long serialVersionUID = -4504482155978221577L;
    private Exception mEx;
    private ErrorType mErrorStatus;

    public ParseException(Exception ex) {
	super();
	this.mEx = ex;
    }

    public ParseException(Exception ex, ErrorType errorStatus) {
	init(errorStatus, ex);
    }

    public ParseException(ErrorType errorStatus) {
	init(errorStatus, null);
    }

    public void init(ErrorType errorStatus, Exception ex) {
	mErrorStatus = errorStatus;
	mEx = ex;
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
	return "ParseExeption";
    }
}
