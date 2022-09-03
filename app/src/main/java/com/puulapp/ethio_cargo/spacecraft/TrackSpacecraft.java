package com.puulapp.ethio_cargo.spacecraft;

public class TrackSpacecraft {

    public String _initial, _final, _status, _date, _key;

    public TrackSpacecraft(String _initial, String _final, String _status, String _date, String _key) {
        this._initial = _initial;
        this._final = _final;
        this._status = _status;
        this._date = _date;
        this._key = _key;
    }

    public String get_initial() {
        return _initial;
    }

    public void set_initial(String _initial) {
        this._initial = _initial;
    }

    public String get_final() {
        return _final;
    }

    public void set_final(String _final) {
        this._final = _final;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_date() {
        return _date;
    }

    public void set_date(String _date) {
        this._date = _date;
    }

    public String get_key() {
        return _key;
    }

    public void set_key(String _key) {
        this._key = _key;
    }
}
