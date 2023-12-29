package com.example.expensetracker.controllers;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UserExpenses {
    private final StringProperty description;
    private final StringProperty date;
            ;
    private final StringProperty receiver;
    private final StringProperty Category;
    private final IntegerProperty amount;
    private final StringProperty type;


    UserExpenses() {
        type = new SimpleStringProperty(this,"type");
        receiver = new SimpleStringProperty(this,"receiver");
        Category = new SimpleStringProperty(this,"Category");
        amount = new SimpleIntegerProperty(this,"amount");
        date = new SimpleStringProperty(this,"date");
        description = new SimpleStringProperty(this,"description");

    }
    public IntegerProperty amountProperty(){return amount;}
    public int getAmount() { return amount.get(); }
    public void setAmount(int amount_id) { amount.set(amount_id); }


    public StringProperty dateProperty() { return date; }
    public String getDate() { return date.get(); }
    public void setDate(String Date) { date.set(Date); }

    public StringProperty descriptionProperty() { return description; }
    public String getDescription() { return description.get(); }
    public void setDescription(String desc) { description.set(desc); }


    public StringProperty typeProperty() { return type; }
    public String getType() { return type.get(); }
    public void setType(String newName) { type.set(newName); }

    public StringProperty receiverProperty() { return receiver; }
    public String getReceiver() { return receiver.get(); }
    public void setReceiver(String resid) { receiver.set(resid); }


    public StringProperty CategoryProperty() { return Category; }
    public String getCategory() { return Category.get(); }
    public void setCategory(String name_category) { Category.set(name_category); }


}