package com.example.finance_tracker.dto;

import com.example.finance_tracker.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data

public class UserResponseDTO {
    private String id;
    private String username;
    private String email;
    private Set<Role> roles;

    public UserResponseDTO(String email, String username, Set<Role> roles, String id) {
        this.email = email;
        this.username = username;
        this.roles = roles;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
