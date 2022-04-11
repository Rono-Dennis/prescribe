package com.example.prescribe;

public class Drug {
    private String DrugName;
    private String ImageUrl;
    private String Price;

    public Drug(String drugName, String imageUrl, String price) {
        DrugName = drugName;
        ImageUrl = imageUrl;
        Price = price;
    }

    public Drug() {
    }

    public String getDrugName() {
        return DrugName;
    }

    public void setDrugName(String drugName) {
        DrugName = drugName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
