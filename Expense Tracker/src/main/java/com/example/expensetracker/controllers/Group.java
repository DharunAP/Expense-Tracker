package com.example.expensetracker.controllers;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class Group {
    private final StringProperty group;
    public Group() {
        group = new SimpleStringProperty(this, "group");
    }
    public StringProperty groupProperty() { return group; }
    public String getGroup() { return group.get(); }
    public void setGroup(String newName) { group.set(newName); }
}