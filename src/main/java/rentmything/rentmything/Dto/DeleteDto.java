package rentmything.rentmything.Dto;

import rentmything.rentmything.Model.Role;

public class DeleteDto {
    private String title;
    private String email;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
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
    private Role role; // optional, for extra validation

    // Getters and setters
}
