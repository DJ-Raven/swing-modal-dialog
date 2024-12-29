package raven.modal.demo.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ModelEmployee {

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ModelProfile getProfile() {
        return profile;
    }

    public void setProfile(ModelProfile profile) {
        this.profile = profile;
    }

    public ModelEmployee(String date, double salary, String position, String description, ModelProfile profile) {
        this.date = date;
        this.salary = salary;
        this.position = position;
        this.description = description;
        this.profile = profile;
    }

    private String date;
    private double salary;
    private String position;
    private String description;
    private ModelProfile profile;

    public Object[] toTableRowBasic(int row) {
        NumberFormat nf = new DecimalFormat("$ #,##0.##");
        return new Object[]{ row, profile.getName(), profile.getLocation(), date, nf.format(salary), position, description};
    }

    public Object[] toTableRowCustom(int row) {
        NumberFormat nf = new DecimalFormat("$ #,##0.##");
        return new Object[]{false, row, profile, date, nf.format(salary), position, description, profile};
    }
}
