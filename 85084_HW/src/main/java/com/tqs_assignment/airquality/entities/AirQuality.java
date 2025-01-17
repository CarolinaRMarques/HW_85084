package com.tqs_assignment.airquality.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "airQuality")
@JsonIgnoreProperties(ignoreUnknown = true)
public class AirQuality {
    @Id
    private String place;
    private String domminant_pollutant;
    private String aqi;
    private String category;

    public AirQuality(String place, String domminant_pollutant, String aqi, String category) {
        this.place = place;
        this.domminant_pollutant = domminant_pollutant;
        this.aqi = aqi;
        this.category = category;
    }

    public AirQuality() {
    }

    @Override
    public String toString() {
        return "AirQuality{" +
                "place='" + place + '\'' +
                ", domminant_pollutant='" + domminant_pollutant + '\'' +
                ", aqi='" + aqi + '\'' +
                ", category='" + category + '\'' +
                '}';
    }

    public String getDomminant_pollutant() {
        return domminant_pollutant;
    }

    public void setDomminant_pollutant(String domminant_pollutant) {
        this.domminant_pollutant = domminant_pollutant;
    }

    public String getAqi() {
        return aqi;
    }

    public void setAqi(String aqi) {
        this.aqi = aqi;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


}
