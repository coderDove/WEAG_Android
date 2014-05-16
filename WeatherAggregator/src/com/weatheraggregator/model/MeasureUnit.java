package com.weatheraggregator.model;

public enum MeasureUnit {
    NAN(0), FAHRENHEIT(1), CELSIUS(2), M_S(3), KM_H(4), MI_H(5), PERCENTAGE(6), MM(7), MM_HG(8), IN_HG(9), PA(10), KM_S(11), PSI(12), MBAR(13);
    private int code;

    MeasureUnit(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
