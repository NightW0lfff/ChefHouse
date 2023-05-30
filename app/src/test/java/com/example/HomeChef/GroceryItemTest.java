package com.example.HomeChef;

import static org.junit.Assert.assertEquals;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GroceryItemTest {

    @Test
    public void testGroceryItemConstructor() {
        String title = "Test Grocery Item";
        ArrayList<String> items = new ArrayList<>();
        items.add("Item 1");
        items.add("Item 2");

        GroceryItem groceryItem = new GroceryItem(title, items);

        // Assert that the title and items are set correctly
        assertEquals(title, groceryItem.getTitle());
        assertEquals(items, groceryItem.getItems());
    }

    @Test
    public void testGroceryItemDefaultConstructor() {
        GroceryItem groceryItem = new GroceryItem();

        // Assert that the default constructor creates a non-null object
        Assert.assertNotNull(groceryItem);
    }

    @Test
    public void testGroceryItemKey() {
        String key = "test_key";

        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setKey(key);

        // Assert that the key is set correctly
        assertEquals(key, groceryItem.getKey());
    }

    @Test
    public void testGroceryItemItemKey() {
        ArrayList<String> itemKey = new ArrayList<>();
        itemKey.add("item_key_1");
        itemKey.add("item_key_2");

        GroceryItem groceryItem = new GroceryItem();
        groceryItem.setItemKey(itemKey);

        // Assert that the item key is set correctly
        assertEquals(itemKey, groceryItem.getItemKey());
    }
}