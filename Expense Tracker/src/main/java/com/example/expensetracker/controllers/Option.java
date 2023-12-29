package com.example.expensetracker.controllers;

public class Option {
    private final String value;

    public Option(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return value;
    }
}
