package test.validation;

import javax.swing.*;

public class User {

    public Icon getProfile() {
        return profile;
    }

    public void setProfile(Icon profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public User(Icon profile, String name, String description, String role) {
        this.profile = profile;
        this.name = name;
        this.description = description;
        this.role = role;
    }

    public User() {
    }

    private Icon profile;
    private String name;
    private String description;
    private String role;

    public boolean isAdmin() {
        return role == "admin";
    }
}
