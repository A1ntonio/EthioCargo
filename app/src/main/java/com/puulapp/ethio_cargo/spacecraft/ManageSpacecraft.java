package com.puulapp.ethio_cargo.spacecraft;

public class ManageSpacecraft {
    public String _origin, _dest, _status, _action, _date, _key;

    public ManageSpacecraft(String _origin, String _dest, String _status, String _action, String _date, String _key) {
        this._origin = _origin;
        this._dest = _dest;
        this._status = _status;
        this._action = _action;
        this._date = _date;
        this._key = _key;
    }

    public String get_origin() {
        return _origin;
    }

    public void set_origin(String _origin) {
        this._origin = _origin;
    }

    public String get_dest() {
        return _dest;
    }

    public void set_dest(String _dest) {
        this._dest = _dest;
    }

    public String get_status() {
        return _status;
    }

    public void set_status(String _status) {
        this._status = _status;
    }

    public String get_action() {
        return _action;
    }

    public void set_action(String _action) {
        this._action = _action;
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
