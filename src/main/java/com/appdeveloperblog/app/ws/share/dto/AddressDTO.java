package com.appdeveloperblog.app.ws.share.dto;

public class AddressDTO 
{
    private long id;
    private String city;
    private String state;
    private String street;
    private String zip;
    private String addressId;
    private String type;
    private UserDto userDetails;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getAddressId() {
        return addressId;
    }
    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }
    public String getZip() {
        return zip;
    }
    public void setZip(String zip) {
        this.zip = zip;
    }
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
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

    public UserDto getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDto userDetails) {
        this.userDetails = userDetails;
    }

    

    
}
