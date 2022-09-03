package com.puulapp.ethio_cargo.spacecraft;

public class FlightsSpacecraft {

    public String departure_date, aircraft_type, flight_no, _from, _to;

    public FlightsSpacecraft(String departure_date, String aircraft_type, String flight_no, String _from, String _to) {
        this.departure_date = departure_date;
        this.aircraft_type = aircraft_type;
        this.flight_no = flight_no;
        this._from = _from;
        this._to = _to;
    }

    public String getDeparture_date() {
        return departure_date;
    }

    public void setDeparture_date(String departure_date) {
        this.departure_date = departure_date;
    }

    public String getAircraft_type() {
        return aircraft_type;
    }

    public void setAircraft_type(String aircraft_type) {
        this.aircraft_type = aircraft_type;
    }

    public String getFlight_no() {
        return flight_no;
    }

    public void setFlight_no(String flight_no) {
        this.flight_no = flight_no;
    }

    public String get_from() {
        return _from;
    }

    public void set_from(String _from) {
        this._from = _from;
    }

    public String get_to() {
        return _to;
    }

    public void set_to(String _to) {
        this._to = _to;
    }
}
