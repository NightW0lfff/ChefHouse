package com.example.housechefv03;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class DataClassTest {

    @Test
    public void testDataClassConstructor() {
        // Create a DataClass object using the constructor
        DataClass data = new DataClass("Title", "Description", "Ingredient", "Instruction", true, "Image");

        // Verify that the object has been initialized correctly
        assertEquals("Title", data.getDataTitle());
        assertEquals("Description", data.getDataDescription());
        assertEquals("Ingredient", data.getDataIngredient());
        assertEquals("Instruction", data.getDataInstruction());
        assertTrue(data.getDataFavorite());
        assertEquals("Image", data.getDataImage());
    }

    @Test
    public void testDataClassSettersAndGetters() {
        // Create a DataClass object
        DataClass data = new DataClass();

        // Use setters to set the values
        data.setDataFavorite(false);

        // Verify that the values are correctly set and retrieved using getters
        assertFalse(data.getDataFavorite());
    }

    @Test
    public void testDataClassKey() {
        // Create a DataClass object
        DataClass data = new DataClass();

        // Set the key value
        data.setKey("12345");

        // Verify that the key is correctly set and retrieved
        assertEquals("12345", data.getKey());
    }
}