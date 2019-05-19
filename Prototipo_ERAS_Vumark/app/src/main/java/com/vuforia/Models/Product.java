package com.vuforia.Models;
//todo: ver se nenhuma parte do codigo quebrou dps da rafatoracao
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

    int getProductId()
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
