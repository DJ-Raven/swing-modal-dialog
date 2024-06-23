package raven.modal.demo.model;

import javax.swing.*;

public class ModelProfile {

    public Icon getIcon() {
        return icon;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ModelProfile(Icon icon, String name, String location) {
        this.icon = icon;
        this.name = name;
        this.location = location;
    }

    private Icon icon;
    private String name;
    private String location;
}
