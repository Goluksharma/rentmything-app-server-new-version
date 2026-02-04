package rentmything.rentmything.Dto;

import java.util.List;

public class OwnerProfileDto {
    private String name;
    private String email;
    private String phoneNumber;
    private List<ItemDto> items;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name; 
    } 
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public List<ItemDto> getItems() {
        return items;
    }
    public void setItems(List<ItemDto> items) {
        this.items = items;
    }

    // Getters and setters
}

