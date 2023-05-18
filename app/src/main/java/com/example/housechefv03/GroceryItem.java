package com.example.housechefv03;

public class GroceryItem {
    private String name;

    public GroceryItem() {
        // Default constructor required for calls to DataSnapshot.getValue(GroceryItem.class)
    }

    public GroceryItem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
