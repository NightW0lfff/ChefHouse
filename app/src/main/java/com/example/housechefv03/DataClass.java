package com.example.housechefv03;

public class DataClass {
    private String dataTitle;
    private String dataDescription;
    private String dataIngredient;
    private String dataInstruction;
    private String dataImage;

    public DataClass(String dataTitle, String dataDescription, String dataIngredient, String dataInstruction, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDescription = dataDescription;
        this.dataIngredient = dataIngredient;
        this.dataInstruction = dataInstruction;
        this.dataImage = dataImage;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public String getDataIngredient() {
        return dataIngredient;
    }

    public String getDataInstruction() {
        return dataInstruction;
    }

    public String getDataImage() {
        return dataImage;
    }
}