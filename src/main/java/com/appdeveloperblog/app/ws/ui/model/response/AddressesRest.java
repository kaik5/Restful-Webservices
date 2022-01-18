package com.appdeveloperblog.app.ws.ui.model.response;

import org.springframework.hateoas.RepresentationModel;

public class AddressesRest extends RepresentationModel<AddressesRest>
{
    private String city;
    private String state;
    private String street;
    private String zip;
    private String addressId;
    private String type;
    
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    
    
}
