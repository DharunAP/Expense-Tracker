package com.example.expensetracker;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;



public class Participant {
    private final StringProperty participant;
    private final SimpleStringProperty amount;

    public Participant() {
        participant = new SimpleStringProperty(this, "participant");
        amount = new SimpleStringProperty(this, "amount");

    }
    public StringProperty participantProperty() { return participant; }
    public String getParticipant() { return participant.get(); }
    public void setParticipant(String newName) { participant.set(newName);}

    public SimpleStringProperty amountProperty() { return amount; }
    public String getAmount() { return amount.get(); }
    public void setAmount(String newName) { amount.set(newName);}


}