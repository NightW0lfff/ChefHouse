package com.example.HomeChef;

import java.util.ArrayList;


public class GroceryItem {
    private ArrayList<String> items;
    private String title;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String key;

    private ArrayList<String> itemKey;

    public ArrayList<String> getItemKey() {
        return itemKey;
    }

    public void setItemKey(ArrayList<String> itemKey) {
        this.itemKey = itemKey;
    }

    public GroceryItem() {
        // Default constructor required for calls to DataSnapshot.getValue(GroceryItem.class)
    }

    public GroceryItem(String title, ArrayList<String> items) {
        this.title = title;
        this.items = items;
    }


    public ArrayList<String> getItems() {
        return items;
    }

    public String getTitle() {
        return title;
    }

}

