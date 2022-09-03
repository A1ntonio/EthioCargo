package com.puulapp.ethio_cargo.spacecraft;

public class RoutesListSpacecraft {
    public String flight_no, aircraft_type, origin, destination, date, appoint_key;

    public RoutesListSpacecraft(String flight_no, String aircraft_type, String origin, String destination, String date, String appoint_key) {
        this.flight_no = flight_no;
        this.aircraft_type = aircraft_type;
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.appoint_key = appoint_key;
    }

    public String getAppoint_key() {
        return appoint_key;
    }

    public void setAppoint_key(String appoint_key) {
        this.appoint_key = appoint_key;
    }

    public String getFlight_no() {
        return flight_no;
    }

    public void setFlight_no(String flight_no) {
        this.flight_no = flight_no;
    }

    public String getAircraft_type() {
        return aircraft_type;
    }

    public void setAircraft_type(String aircraft_type) {
        this.aircraft_type = aircraft_type;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
