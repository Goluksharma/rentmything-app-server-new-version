package rentmything.rentmything.Dto;

import java.util.ArrayList;
import java.util.List;

import rentmything.rentmything.Model.Item;

public class ReturningUserDto {
    private String name;
    private String phoneNumber;
    private Double latitude;
    private Double longitude; 
    private List<Item> items = new ArrayList<>(); 
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public List<Item> getItems() {
        return items;
    }
    public void setItems(List<Item> items) {
        this.items = items;
    }
    public ReturningUserDto(String name, String phoneNumber, Double latitude, Double longitude, List<Item> items) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
        this.items = items;
    }
    public ReturningUserDto() {
    }


    
}
