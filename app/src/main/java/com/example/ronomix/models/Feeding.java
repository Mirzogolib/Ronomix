package com.example.ronomix.models;

public class Feeding {
    private String day;
    private String weight;
    private String cum_all;
    private String cum_period;

    public Feeding() {
    }

    public Feeding(String day, String weight, String cum_all, String cum_period) {
        this.day = day;
        this.weight = weight;
        this.cum_all = cum_all;
        this.cum_period = cum_period;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCum_all() {
        return cum_all;
    }

    public void setCum_all(String cum_all) {
        this.cum_all = cum_all;
    }

    public String getCum_period() {
        return cum_period;
    }

    public void setCum_period(String cum_period) {
        this.cum_period = cum_period;
    }
}
