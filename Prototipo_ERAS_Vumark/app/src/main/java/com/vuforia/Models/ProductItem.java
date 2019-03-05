package com.vuforia.Models;

public class ProductItem {
    private String title;
    private String description;
    private int amount;
    private String locate;

    public ProductItem(String title, int amount, String locate, String description) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.locate = locate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getLocate() {
        return locate;
    }

    public void setLocate(String locate) {
        this.locate = locate;
    }
}
