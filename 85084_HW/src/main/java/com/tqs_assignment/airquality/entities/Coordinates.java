package com.tqs_assignment.airquality.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "coordinates")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {
    @Id
    private String placename;
    private double latitude;
    private double longitude;

    public Coordinates(String placename, double latitude, double longitude) {
        this.placename = placename;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Coordinates() {
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Coordinates{" +
                "placename='" + placename + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }


}
