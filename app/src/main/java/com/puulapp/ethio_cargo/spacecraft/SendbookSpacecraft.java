package com.puulapp.ethio_cargo.spacecraft;

public class SendbookSpacecraft {
    public String origin, destination, date, weight, pieces, length, width, height, volume, total_volume, agent, key
            , consignee_name, consignee_address, consignee_country, consignee_city, consignee_state, consignee_phone
            , consignee_zip, price, handling_code, commodity_code, status, action;

    public SendbookSpacecraft() {
    }

    public SendbookSpacecraft(String origin, String destination, String date, String weight, String pieces, String length, String width, String height, String volume, String total_volume, String agent, String key, String consignee_name, String consignee_address, String consignee_country, String consignee_city, String consignee_state, String consignee_phone, String consignee_zip, String price, String handling_code, String commodity_code, String status, String action) {
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
        this.key = key;
        this.consignee_name = consignee_name;
        this.consignee_address = consignee_address;
        this.consignee_country = consignee_country;
        this.consignee_city = consignee_city;
        this.consignee_state = consignee_state;
        this.consignee_phone = consignee_phone;
        this.consignee_zip = consignee_zip;
        this.price = price;
        this.handling_code = handling_code;
        this.commodity_code = commodity_code;
        this.status = status;
        this.action = action;
    }
}
