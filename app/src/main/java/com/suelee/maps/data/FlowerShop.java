package com.suelee.maps.data;

public class FlowerShop {
    private LocationMark mark;
    private String reviews;

    private String type;

    private String services;

    public FlowerShop() {
    }

    public LocationMark getMark() {
        return mark;
    }

    public String getReviews() {
        return reviews;
    }

    public String getType() {
        return type;
    }

    public String getServices() {
        return services;
    }

    public void setMark(LocationMark mark) {
        this.mark = mark;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setServices(String services) {
        this.services = services;
    }
}
