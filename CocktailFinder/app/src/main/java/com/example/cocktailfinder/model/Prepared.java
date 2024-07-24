package com.example.cocktailfinder.model;

public class Prepared {
    private String cocktailName;
    private float rating;
    private String imagePath;

    public Prepared(String cocktailName, float rating, String imagePath) {
        this.cocktailName = cocktailName;
        this.rating = rating;
        this.imagePath = imagePath;
    }

    // Getters and setters
    public String getCocktailName() { return cocktailName; }
    public void setCocktailName(String cocktailName) { this.cocktailName = cocktailName; }

    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
