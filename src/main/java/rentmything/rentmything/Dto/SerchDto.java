package rentmything.rentmything.Dto;

import rentmything.rentmything.Model.Category;
import rentmything.rentmything.Model.Role;

public class SerchDto {
    private String email;
    private Role role;
    private Category category;

    private Double radius;
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }
    public Category getCategory() {
        return category;
    }
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public Double getRadius() {
        return radius;
    }
    public void setRadius(Double radius) {
        this.radius = radius;
    }
    
}
