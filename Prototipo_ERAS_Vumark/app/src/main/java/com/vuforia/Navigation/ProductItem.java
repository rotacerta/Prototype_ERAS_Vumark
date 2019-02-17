package com.vuforia.Navigation;

public class ProductItem {
    String title;
    String description;
    int amount;
    String locate;

    ProductItem(String title, int amount,
                String locate, String description) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.locate = locate;
    }
}
