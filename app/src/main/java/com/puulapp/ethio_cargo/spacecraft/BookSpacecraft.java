package com.puulapp.ethio_cargo.spacecraft;

public class BookSpacecraft {

    String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent;

    public BookSpacecraft(String origin, String destination, String date, String weight, String pieces, String length, String width, String height, String volume, String total_volume, String agent) {
        this.origin = origin;
        this.destination = destination;
        this.date = date;
        this.weight = weight;
        this.pieces = pieces;
        this.length = length;
        this.width = width;
        this.height = height;
        this.volume = volume;
        this.total_volume = total_volume;
        this.agent = agent;
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

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPieces() {
        return pieces;
    }

    public void setPieces(String pieces) {
        this.pieces = pieces;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTotal_volume() {
        return total_volume;
    }

    public void setTotal_volume(String total_volume) {
        this.total_volume = total_volume;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }
}
