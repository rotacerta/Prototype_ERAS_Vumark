package com.vuforia.Models;

public class Product
{
    private int ProductId;
    private String Name;
    private int LocationId;
    private int RequiredQuantity;
    private int QuantityCatched;

    public Product(int productId, String name, int locationId, int requiredQuantity, int quantityCatched)
    {
        ProductId = productId;
        Name = name;
        LocationId = locationId;
        RequiredQuantity = requiredQuantity;
        QuantityCatched = quantityCatched;
    }

    public int getProductId()
    {
        return ProductId;
    }

    public String getName()
    {
        return Name;
    }

    public int getLocationId()
    {
        return LocationId;
    }

    public int getRequiredQuantity()
    {
        return RequiredQuantity;
    }

    public int getQuantityCatched()
    {
        return QuantityCatched;
    }

    public void setQuantityCatched(int quantityCatched)
    {
        QuantityCatched = quantityCatched;
    }
}
