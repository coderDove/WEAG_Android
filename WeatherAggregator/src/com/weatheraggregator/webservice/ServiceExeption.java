package com.weatheraggregator.webservice;

public class ServiceExeption extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = -2175544474279249144L;
    private Exception ex;
    private ServiceErrorType type = ServiceErrorType.UNKNOWN;

    public ServiceExeption(Exception ex) {
        super();
        this.ex = ex;
        ex.printStackTrace();
    }

    public ServiceExeption(Exception ex, ServiceErrorType type) {
        super();
        if (ex != null) {
            this.ex = ex;
            ex.printStackTrace();
        }
        this.type = type;
    }

    public ServiceErrorType getTypeErrorType() {
        return type;
    }

    public String getMessage() {
        if (ex != null && ex.getMessage() != null) {
            return ex.getMessage();
        }
        return "";
    }

    public static enum ServiceErrorType {
        ERROR_CONNECTION, ERROR_PARSE, UNKNOWN
    }

}
