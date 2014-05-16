package com.weatheraggregator.model;

public class DrawerMenuItem implements Comparable<DrawerMenuItem> {
    public final static int STATISTIC_ITEM = 0;
    public final static int CITY_ITEM = 1;
    public final static int SERVICE_ITEM = 2;
    public final static int HEADER = 3;
    public final static int ABOUT = 4;
    public final static int EXIT = 5;
    public final static int SETTING_ITEM = 6;
    public final static int SETTING_USER_CITY_LIST = 7;
    public final static int SETTING_USER_SERVICE_LIST = 8;
    public final static int MEASURE = 9;

    private int type;
    private int order;
    private String name;
    private String value;
    private String logo;
    private int idLogo;

    public DrawerMenuItem(int order, String name, String value, int type) {
        super();
        this.order = order;
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public int getIdLogo() {
        return idLogo;
    }

    public void setIdLogo(int idLogo) {
        this.idLogo = idLogo;
    }

    @Override
    public int compareTo(DrawerMenuItem another) {
        return this.getOrder() - another.getOrder();
    }
}
